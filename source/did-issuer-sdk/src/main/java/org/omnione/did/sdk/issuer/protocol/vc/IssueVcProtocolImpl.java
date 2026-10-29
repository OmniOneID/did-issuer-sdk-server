package org.omnione.did.sdk.issuer.protocol.vc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.omnione.did.sdk.issuer.model.domain.*;
import org.omnione.did.sdk.issuer.model.dto.*;
import org.omnione.did.sdk.issuer.provider.CertificateVcProvider;

import org.apache.logging.log4j.util.Strings;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.core.data.rest.ClaimInfo;
import org.omnione.did.core.data.rest.IssueVcParam;
import org.omnione.did.core.data.rest.SignatureParams;
import org.omnione.did.core.data.rest.SignatureVcParams;
import org.omnione.did.core.exception.CoreException;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.core.manager.VcManager;
import org.omnione.did.crypto.keypair.EcKeyPair;
import org.omnione.did.crypto.keypair.KeyPairInterface;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.data.model.enums.did.ProofPurpose;
import org.omnione.did.data.model.enums.did.ProofType;
import org.omnione.did.data.model.enums.profile.ProfileType;
import org.omnione.did.data.model.enums.vc.CredentialSchemaType;
import org.omnione.did.data.model.enums.vc.VcStatus;
import org.omnione.did.data.model.enums.vc.VcType;
import org.omnione.did.data.model.profile.ReqE2e;
import org.omnione.did.data.model.profile.issue.IssueProcess;
import org.omnione.did.data.model.provider.ProviderDetail;
import org.omnione.did.data.model.schema.ClaimDef;
import org.omnione.did.data.model.schema.SchemaClaims;
import org.omnione.did.data.model.vc.*;
import org.omnione.did.common.util.IdGenerator;
import org.omnione.did.common.util.crypto.BaseCryptoUtil;
import org.omnione.did.common.util.digest.BaseDigestUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.sdk.issuer.util.ValidationUtil;
import org.omnione.did.zkp.core.manager.ZkpCredentialManager;
import org.omnione.did.zkp.crypto.constant.ZkpCryptoConstants;
import org.omnione.did.zkp.crypto.util.BigIntegerUtil;
import org.omnione.did.zkp.datamodel.credential.*;
import org.omnione.did.zkp.datamodel.credentialoffer.CredentialOffer;
import org.omnione.did.zkp.datamodel.credentialoffer.KeyCorrectnessProof;
import org.omnione.did.zkp.datamodel.credentialrequest.CredentialRequest;
import org.omnione.did.zkp.datamodel.definition.CredentialDefinition;
import org.omnione.did.zkp.exception.ZkpException;

import org.omnione.did.sdk.issuer.model.data.zkp.CredentialInfo;
import org.omnione.did.sdk.issuer.model.data.zkp.ZkpInnerIssueProfile;
import org.omnione.did.sdk.issuer.model.data.zkp.ZkpIssueProfile;
import org.omnione.did.sdk.issuer.model.common.enums.EccCurveType;
import org.omnione.did.sdk.issuer.model.common.enums.OfferType;
import org.omnione.did.sdk.issuer.model.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.issuer.model.common.enums.SymmetricPaddingType;
import org.omnione.did.sdk.issuer.model.common.enums.SubTransactionType;
import org.omnione.did.sdk.issuer.exception.IssuerSdkErrorCode;
import org.omnione.did.sdk.issuer.exception.IssuerSdkException;
import org.omnione.did.sdk.issuer.provider.IssueDataProvider;
import org.omnione.did.sdk.issuer.provider.StorageProvider;
import org.omnione.did.sdk.issuer.provider.WalletProvider;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.omnione.did.sdk.issuer.model.data.AccE2e;
import org.omnione.did.sdk.issuer.model.data.DidAuth;
import org.omnione.did.sdk.issuer.model.data.E2e;
import org.omnione.did.sdk.issuer.model.data.Holder;
import org.omnione.did.sdk.issuer.model.data.IssueOfferPayload;

import org.omnione.did.sdk.issuer.model.data.ReqVc;

/**
 * SDK Core Class for issuing Verifiable Credentials (VCs).
 * Refactored from IssueServiceBase to use Provider Pattern.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueVcProtocolImpl implements IssueVcProtocol {

    private static final Logger log = LoggerFactory.getLogger(IssueVcProtocolImpl.class);

    private final IssueDataProvider issueDataProvider;
    protected final WalletProvider walletProvider;
    protected final StorageProvider storageProvider;
    protected CertificateVcProvider certificateVcProvider;

    public IssueVcProtocolImpl(IssueDataProvider issueDataProvider, WalletProvider walletProvider,
                               StorageProvider storageProvider) {
        this.issueDataProvider = issueDataProvider;
        this.walletProvider = walletProvider;
        this.storageProvider = storageProvider;
    }

    public void setCertificatePublisher(CertificateVcProvider certificateVcProvider) {
        this.certificateVcProvider = certificateVcProvider;
    }

    @Override
    public OfferIssueVcResDto requestOffer(OfferIssueVcReqDto request) {
        try {
            log.debug("=== Starting Request Offer ===");
            log.debug("\t--> Validating VC plan");
            validateVcPlanId(request.getVcPlanId());

            log.debug("\t--> Generating offer payload");
            String offerId = IdGenerator.generateUUID();
            String issuer = issueDataProvider.getIssuerInfo().getDid();
            String validUntil = DateTimeUtil.addToCurrentTimeString(3, ChronoUnit.MINUTES);

            IssueOfferPayload issueOfferPayload = IssueOfferPayload.builder()
                    .offerId(offerId)
                    .type(OfferType.ISSUE_OFFER)
                    .vcPlanId(request.getVcPlanId())
                    .issuer(issuer)
                    .validUntil(validUntil)
                    .build();

            log.debug("\t--> VC Offer save to DB");
            issueDataProvider.saveVcOffer(SdkVcOffer.builder()
                    .offerId(offerId)
                    .did(issuer)
                    .validUntil(Instant.now().plusSeconds(86400L))
                    .offerType(OfferType.ISSUE_OFFER)
                    .vcPlanId(request.getVcPlanId())
                    .build());
            log.debug("*** Finished Request Offer ***");

            return OfferIssueVcResDto.builder()
                    .issueOfferPayload(issueOfferPayload)
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during requestOffer: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during requestOffer: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_OFFER_FAILED);
        }
    }

    @Override
    public InspectIssueProposeResDto inspectIssuePropose(InspectIssueProposeReqDto request) {
        try {
            log.debug("=== Starting Inspect Issue Propose ===");

            log.debug("\t--> Validating VC plan");
            String vcPlanId = request.getVcPlanId();
            validateVcPlanId(vcPlanId);

            if (Strings.isNotBlank(request.getOfferId())) {
                log.debug("\t--> Validating Offer");
                validateOfferId(request.getOfferId());
            }

            String txId = IdGenerator.generateUUID();
            String refId = IdGenerator.generateRefId();

            log.debug("=== Finished Inspect Issue Propose ===");
            return InspectIssueProposeResDto.builder()
                    .txId(txId)
                    .refId(refId)
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during inspectIssuePropose: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during inspectIssuePropose: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    @Override
    public GenerateIssueProfileResDto generateIssueProfile(GenerateIssueProfileReqDto request,
            SdkTransaction transaction, SdkUser user) {
        try {
            log.debug("=== SDK: Starting Generate Issue Profile ===");

            String vcPlanId = transaction.getVcPlanId();

            SdkIssueProfile byVcPlanId = issueDataProvider.findIssueProfileByVcPlanId(vcPlanId);
            ZkpIssueProfile profile = getIssueProfile(byVcPlanId);

            IssueProcess process = profile.getProfile().getProcess();
            ReqE2e reqE2e = process.getReqE2e();
            profile.setId(IdGenerator.generateUUID());

            log.debug("\t--> Generate Key pair");
            EcKeyPair keyPair = generateEcKeyPair(reqE2e.getCurve());

            String nonce = BaseMultibaseUtil.encode(BaseCryptoUtil.generateNonce(16));

            setPublicKeyAndNonce(reqE2e, process, keyPair, nonce);
            signProfile(profile, "assert");
            String encodedSessionKey = encodedSessionKey((ECPrivateKey) keyPair.getPrivateKey());

            log.debug("\t--> Pre-fetched User used");

            log.debug("\t--> VC Profile save to DB");
            SdkVcProfile vcProfile = SdkVcProfile.builder()
                    .profileId(profile.getId())
                    .transactionId(transaction.getId())
                    .did(user.getDid())
                    .nonce(reqE2e.getNonce())
                    .userId(user.getId())
                    .build();

            if (profile.getProfile().getCredentialOffer() != null) {
                vcProfile.setZkpNonce(profile.getProfile().getCredentialOffer().getNonce().toString());
            }
            issueDataProvider.saveVcProfile(vcProfile);

            log.debug("\t--> E2E save to DB");
            issueDataProvider.saveE2E(SdkE2E.builder()
                    .curve(reqE2e.getCurve())
                    .cipher(reqE2e.getCipher())
                    .padding(reqE2e.getPadding())
                    .nonce(reqE2e.getNonce())
                    .sessionKey(encodedSessionKey)
                    .transactionId(transaction.getId())
                    .build());

            log.debug("=== SDK: Finished Generate Issue Profile ===");
            log.debug("\t--> Profile: {}", profile.toJson());
            return GenerateIssueProfileResDto.builder()
                    .txId(request.getTxId())
                    .profile(profile)
                    .authNonce(process.getIssuerNonce())
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during generateIssueProfile: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during generateIssueProfile: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_ISSUE_PROFILE_FAILED);
        }
    }

    @Override
    public IssueVcResDto issueVc(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user) {
        try {
            log.debug("=== SDK: Starting Issue VC ===");
            AccE2e accE2e = request.getAccE2e();

            log.debug("\t--> Validating AccE2E");
            validateAccE2e(accE2e);

            log.debug("\t--> Get E2E");
            SdkE2E e2e = issueDataProvider.findE2EByTransactionId(transaction.getId());

            log.debug("\t--> Get Issue Profile");
            SdkVcProfile vcProfile = issueDataProvider.findVcProfileByTransactionId(transaction.getId());

            log.debug("\t--> Generate SharedSecretKey");
            byte[] sharedSecretKey = generateSharedSecretKey(e2e, accE2e);

            log.debug("\t--> Generate MergeSharedSecretKey");
            byte[] mergeSharedSecretAndNonce = mergeSharedSecretAndNonce(sharedSecretKey, vcProfile.getNonce(),
                    e2e.getCipher());

            log.debug("\t--> Decrypt Request VC");
            String decryptedRequestVc = decryptRequestVc(request, mergeSharedSecretAndNonce,
                    BaseMultibaseUtil.decode(accE2e.getIv()), e2e);

            log.debug("\t--> Parse Request VC");
            ReqVc reqVc = parseRequestVc(decryptedRequestVc);
            validateRequestVc(transaction, reqVc);

            log.debug("\t--> User By VC Profile Pre-fetched");
            SdkIssueProfile issueProfile = issueDataProvider.findIssueProfileById(transaction.getIssueProfileId());

            VcManager vcManager = new VcManager();

            log.debug("\t--> Issuing VC");
            VerifiableCredential verifiableCredential = issueVerifiableCredential(vcManager,
                    vcProfile.getDid(), user, issueProfile.getVcSchemaId());
            log.debug("\t--> VerifiableCredential {}", verifiableCredential.toJson());

            log.debug("\t--> Registering VC to B/C");
            VcMeta vcMeta = vcManager.generateVcMetaData(verifiableCredential,
                    certificateVcProvider.getCertificateVcURL(issueDataProvider.getIssuerInfo()));

            storageProvider.registerVcMeta(vcMeta);

            log.debug("\t--> Generate IV");
            byte[] iv = BaseCryptoUtil.generateNonce(16);

            log.debug("\t--> Issuing Credential");
            Credential credential = issueCredential(reqVc.getCredentialRequest(), user, vcMeta.getId(),
                    vcProfile.getZkpNonce());

            log.debug("\t--> Encrypt VC");
            String encVc = encryptVerifiableCredential(verifiableCredential, credential, mergeSharedSecretAndNonce, iv,
                    e2e);

            SdkVc vc = handleVcCreationOrUpdate(user, vcProfile.getDid(), transaction, vcMeta);

            log.debug("\t--> VC_ID, Holder info save to DB");
            issueDataProvider.saveVc(vc);

            log.debug("=== SDK: Finished Issue VC ===");
            return IssueVcResDto.builder()
                    .txId(request.getTxId())
                    .e2e(E2e.builder()
                            .encVc(encVc)
                            .iv(BaseMultibaseUtil.encode(iv))
                            .build())
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during issueVc: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during issueVc: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_ISSUE_FAILED);
        }
    }

    @Override
    public IssueVcResDto issueVcFromProxy(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user) {
        log.debug("=== Starting Issue VC (proxy) ===");
        DidAuth didAuth = request.getDidAuth();
        Proof proof = didAuth.getProof();

        if (Objects.nonNull(proof)) {
            String did = didAuth.getDid();
            DidDocument holderDidDoc = storageProvider.findDidDoc(did);
            ValidationUtil.verifySign(didAuth, holderDidDoc);
        }

        SdkVcProfile vcProfile = issueDataProvider.findVcProfileByTransactionId(transaction.getId());
        validateAuthNonce(didAuth.getAuthNonce(), vcProfile.getNonce());
        vcProfile.setDid(didAuth.getDid());
        issueDataProvider.saveVcProfile(vcProfile);

        return issueVc(request, transaction, user);
    }

    private void validateAuthNonce(String authNonce, String nonce) {
        if (!authNonce.equals(nonce)) {
            throw new IssuerSdkException(IssuerSdkErrorCode.ISSUER_NONCE_INVALID);
        }
    }

    private Credential issueCredential(CredentialRequest credentialRequest, UserInfoDto user, String vcId,
            String issuerNonce) {
        if (credentialRequest == null) {
            return null;
        }
        try {
            SdkZkpCredentialDefinition definition = issueDataProvider
                    .findZkpCredentialDefinitionByDefinitionId(credentialRequest.getCredDefId());
            SdkZkpSchema zkpSchema = issueDataProvider.findZkpSchemaBySchemaId(definition.getSchemaId());

            org.omnione.did.zkp.datamodel.schema.CredentialSchema credentialSchema = new Gson()
                    .fromJson(zkpSchema.getSchema(), org.omnione.did.zkp.datamodel.schema.CredentialSchema.class);

            LinkedHashMap<String, AttributeValue> attributeInfo = getAttributeInfo(credentialSchema.getAttrNames(),
                    user.getData());
            CredentialDefinition credentialDefinition = new Gson().fromJson(definition.getDefinition(),
                    CredentialDefinition.class);
            CredentialValues credentialValues = new CredentialValues();
            credentialValues.setValues(attributeInfo);

            CredentialSignature credentialSignature = walletProvider.credSignature(definition.getAlias(),
                    credentialRequest, credentialValues);
            SignatureCorrectnessProof signatureCorrectnessProof = walletProvider
                    .signatureCorrectnessProof(definition.getAlias(), credentialRequest, credentialSignature);

            BigInteger nonce = new BigInteger(issuerNonce);

            return new ZkpCredentialManager().createCredential(credentialDefinition, credentialSignature,
                    signatureCorrectnessProof, attributeInfo, credentialRequest, nonce, vcId);
        } catch (ZkpException e) {
            throw new IssuerSdkException(IssuerSdkErrorCode.FAILED_TO_ISSUE_CREDENTIAL);
        }
    }

    @Override
    public CompleteVcResDto completeVc(CompleteVcReqDto request, SdkTransaction transaction) {
        try {
            log.debug("=== SDK: Starting Complete VC ===");
            SdkVc vc = issueDataProvider.findVcByTxId(request.getTxId());
            if (!vc.getVcId().equals(request.getVcId())) {
                throw new IssuerSdkException(IssuerSdkErrorCode.VC_ID_NOT_MATCH);
            }

            log.debug("=== SDK: Finished Complete VC ===");
            return CompleteVcResDto.builder()
                    .txId(request.getTxId())
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during completeVc: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during completeVc: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_ISSUE_COMPLETE_FAILED);
        }
    }

    public IssueVcResultResDto issueVcResult(String offerId) {
        try {
            log.debug("=== Starting Issue VC Result ===");
            SdkVcOffer vcOffer = issueDataProvider.findVcOfferByOfferId(offerId);

            SdkTransaction transaction = issueDataProvider.findTransactionById(vcOffer.getTransactionId());
            if (transaction == null) {
                return IssueVcResultResDto.builder()
                        .offerId(offerId)
                        .result(false)
                        .build();
            }
            SdkSubTransaction subTransaction = issueDataProvider
                    .findSubTransactionByTransactionIdOrderByStepDesc(transaction.getId());

            log.debug("=== Finished Issue VC Result ===");
            return IssueVcResultResDto.builder()
                    .txId(transaction.getTxId())
                    .offerId(offerId)
                    .result(SubTransactionType.COMPLETE_VC.equals(subTransaction.getType()))
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during issueVcResult: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during issueVcResult: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_ISSUE_RESULT_FAILED);
        }
    }

    private byte[] mergeSharedSecretAndNonce(byte[] sharedSecretKey, String nonce, String cipherType) {
        return BaseCryptoUtil.mergeSharedSecretAndNonce(sharedSecretKey,
                BaseMultibaseUtil.decode(nonce),
                SymmetricCipherType.fromDisplayName(cipherType).toCommonSdkSymmetricCipherType());
    }

    private SdkIssueProfile validateVcPlanId(String vcPlanId) {
        return issueDataProvider.findIssueProfileByVcPlanId(vcPlanId);
    }

    private SdkVcOffer validateOfferId(String offerId) {
        SdkVcOffer vcOffer = issueDataProvider.findVcOfferByOfferId(offerId);

        if (ValidationUtil.isExpiredDate(vcOffer.getValidUntil())) {
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_OFFER_EXPIRED);
        }
        return vcOffer;
    }

    private void setPublicKeyAndNonce(ReqE2e reqE2e, IssueProcess process, EcKeyPair keyPair, String nonce) {
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublicKey();
        byte[] encodedPublicKey = BaseCryptoUtil.compressPublicKey(publicKey.getEncoded(),
                EccCurveType.fromValue(reqE2e.getCurve()).toCommonSdkEccCurveType());
        reqE2e.setPublicKey(BaseMultibaseUtil.encode(encodedPublicKey));

        process.setIssuerNonce(nonce);
        reqE2e.setNonce(nonce);
    }

    private String encodedSessionKey(ECPrivateKey privateKey) {
        return BaseMultibaseUtil.encode(privateKey.getEncoded());
    }

    private SdkVc handleVcCreationOrUpdate(UserInfoDto user, String holderDid, SdkTransaction transaction,
            VcMeta vcMeta) {
        String vcPlanId = transaction.getVcPlanId();
        String txId = transaction.getTxId();
        return issueDataProvider.findVcByUserIdAndVcPlanId(user.getId(), vcPlanId)
                .map(existingVc -> {
                    revokeVc(existingVc);
                    return SdkVc.builder()
                            .issuedAt(DateTimeUtil.parseUtcTimeStringToInstant(vcMeta.getIssuanceDate()))
                            .expiredAt(DateTimeUtil.parseUtcTimeStringToInstant(vcMeta.getValidUntil()))
                            .did(holderDid)
                            .userId(user.getId())
                            .vcPlanId(vcPlanId)
                            .txId(txId)
                            .vcId(vcMeta.getId())
                            .vcSchemaId(vcMeta.getCredentialSchema().getId())
                            .status(vcMeta.getStatus())
                            .vcType("VC")
                            .build();
                })
                .orElseGet(() -> SdkVc.builder()
                        .issuedAt(DateTimeUtil.parseUtcTimeStringToInstant(vcMeta.getIssuanceDate()))
                        .expiredAt(DateTimeUtil.parseUtcTimeStringToInstant(vcMeta.getValidUntil()))
                        .did(holderDid)
                        .userId(user.getId())
                        .vcPlanId(vcPlanId)
                        .txId(txId)
                        .vcId(vcMeta.getId())
                        .vcSchemaId(vcMeta.getCredentialSchema().getId())
                        .status(vcMeta.getStatus())
                        .vcType("VC")
                        .build());
    }

    private String encryptVerifiableCredential(VerifiableCredential verifiableCredential, Credential credential,
            byte[] sharedSecretKey, byte[] iv, SdkE2E e2e) {
        CredentialInfo credentialInfo = CredentialInfo.builder()
                .vc(verifiableCredential)
                .credential(credential)
                .build();
        String credentialJson = credentialInfo.toJson();

        byte[] encrypt = BaseCryptoUtil.encrypt(credentialJson.getBytes(StandardCharsets.UTF_8),
                sharedSecretKey,
                iv,
                SymmetricCipherType.fromDisplayName(e2e.getCipher()).toCommonSdkSymmetricCipherType(),
                SymmetricPaddingType.fromDisplayName(e2e.getPadding()).toCommonSdkSymmetricPaddingType());

        return BaseMultibaseUtil.encode(encrypt);
    }

    private void validateAccE2e(AccE2e accE2e) {
        Proof proof = accE2e.getProof();

        if (Objects.nonNull(proof)) {
            String verificationMethod = proof.getVerificationMethod();
            DidDocument holderDidDoc = storageProvider.findDidDoc(verificationMethod);

            ValidationUtil.verifySign(accE2e, holderDidDoc);
        }
    }

    private byte[] generateSharedSecretKey(SdkE2E e2e, AccE2e accE2e) {
        byte[] decodePubKey = BaseMultibaseUtil.decode(accE2e.getPublicKey());
        byte[] decodePriKey = BaseMultibaseUtil.decode(e2e.getSessionKey());

        return BaseCryptoUtil.generateSharedSecret(decodePubKey, decodePriKey,
                EccCurveType.fromValue(e2e.getCurve()).toCommonSdkEccCurveType());
    }

    private String decryptRequestVc(IssueVcReqDto request, byte[] sharedSecretKey, byte[] iv, SdkE2E e2e) {
        byte[] decrypt = BaseCryptoUtil.decrypt(
                BaseMultibaseUtil.decode(request.getEncReqVc()),
                sharedSecretKey,
                iv,
                SymmetricCipherType.fromDisplayName(e2e.getCipher()).toCommonSdkSymmetricCipherType(),
                SymmetricPaddingType.fromDisplayName(e2e.getPadding()).toCommonSdkSymmetricPaddingType());
        return new String(decrypt);
    }

    private ReqVc parseRequestVc(String requestVc) {
        ReqVc reqVc = new ReqVc();
        reqVc.fromJson(requestVc);
        return reqVc;
    }

    private void validateRequestVc(SdkTransaction transaction, ReqVc reqVc) {
        if (!transaction.getRefId().equals(reqVc.getRefId())) {
            throw new IssuerSdkException(IssuerSdkErrorCode.REF_ID_INVALID);
        }
        SdkVcProfile vcProfile = issueDataProvider.findVcProfileByTransactionId(transaction.getId());

        if (!reqVc.getProfile().getId().equals(vcProfile.getProfileId())) {
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_PROFILE_INVALID);
        }

        if (!reqVc.getProfile().getIssuerNonce().equals(vcProfile.getNonce())) {
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_PROFILE_ISSUER_NONCE_INVALID);
        }
    }

    private VerifiableCredential issueVerifiableCredential(VcManager vcManager, String holderDid, UserInfoDto user,
            Long vcSchemaId) {
        log.debug("\t--> Issue Verifiable Credential");
        try {

            IssueVcParam issueVcParam = new IssueVcParam();

            DidDocument didDocument = getDidDocument();
            org.omnione.did.data.model.schema.VcSchema vcSchema = getVcSchema(vcSchemaId);
            BaseCoreVcUtil.setVcSchema(issueVcParam, vcSchema.toJson());
            BaseCoreVcUtil.setClaimInfo(issueVcParam,
                    getClaimInfo(vcSchema.getCredentialSubject().getClaims(), user.getData()));
            ProviderDetail issuer = getIssuer();
            BaseCoreVcUtil.setIssuer(issueVcParam, issuer.getDid(), issuer.getName(), issuer.getCertVcRef());
            BaseCoreVcUtil.setVcTypes(issueVcParam, getVcType());
            BaseCoreVcUtil.setEvidence(issueVcParam, getEvidence());
            issueVcParam.setValidFrom(user.getValidFrom());
            issueVcParam.setValidUntil(user.getValidUntil());

            VerifiableCredential verifiableCredential = vcManager.issueCredential(issueVcParam, holderDid);

            List<SignatureVcParams> signatureParams = vcManager.getOriginDataForSign("assert", didDocument,
                    verifiableCredential);
            signVc(signatureParams);

            verifiableCredential = vcManager.addProof(verifiableCredential, signatureParams);

            return verifiableCredential;
        } catch (CoreException e) {
            log.error("Failed to issue Verifiable Credential: {}", e.getMessage());
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_ISSUE_FAILED);
        }
    }

    private void revokeVc(SdkVc vc) {
        VcMeta vcMetByVcId = storageProvider.getVcMetaByVcId(vc.getVcId());
        if (!VcStatus.REVOKED.getRawValue().equals(vcMetByVcId.getStatus())) {
            storageProvider.updateVcStatus(vc.getVcId(), VcStatus.REVOKED);
            vc.setStatus(VcStatus.REVOKED.getRawValue());
            issueDataProvider.saveVc(vc);
        }
    }

    private EcKeyPair generateEcKeyPair(String curve) {
        return (EcKeyPair) generateKeyPair(curve);
    }

    private KeyPairInterface generateKeyPair(String curve) {
        EccCurveType eccCurveType = EccCurveType.fromValue(curve);
        return BaseCryptoUtil.generateKeyPair(eccCurveType.toCommonSdkEccCurveType());
    }

    private void signProfile(ZkpIssueProfile profile, String keyId) {
        Proof proof = new Proof();
        proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
        proof.setProofPurpose(ProofPurpose.ASSERTION_METHOD.getRawValue());
        proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
        proof.setVerificationMethod(issueDataProvider.getIssuerInfo().getDid() + "#" + "assert");
        profile.setProof(proof);

        String source = JsonUtil.serializeAndSort(profile);
        byte[] bytes = walletProvider.generateCompactSignature(keyId, source);

        proof.setProofValue(BaseMultibaseUtil.encode(bytes));
    }

    private List<VcType> getVcType() {
        return List.of(VcType.VERIFIABLE_CREDENTIAL);
    }

    private DidDocument getDidDocument() {
        return storageProvider.findDidDoc(issueDataProvider.getIssuerInfo().getDid());
    }

    private DocumentVerificationEvidence getEvidence() {
        DocumentVerificationEvidence evidence = new DocumentVerificationEvidence();
        evidence.fromJson("""
                {
                    "evidenceDocument":"BusinessLicense",
                    "subjectPresence":"Physical",
                    "documentPresence":"Physical",
                    "type":"DocumentVerification",
                    "verifier":"did:omn:tas"
                }
                """);
        return evidence;
    }

    private void signVc(List<SignatureVcParams> signatureParams) {
        for (SignatureParams signatureParam : signatureParams) {
            String originData = signatureParam.getOriginData();
            byte[] sign = walletProvider.generateCompactSignature(signatureParam.getKeyId(), originData);
            String signatureValue = BaseMultibaseUtil.encode(sign);
            signatureParam.setSignatureValue(signatureValue);
        }
    }

    private LinkedHashMap<String, AttributeValue> getAttributeInfo(List<String> attrNames, String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();

        return attrNames.stream()
                .map(attrName -> {
                    AttributeValue attributeValue = createAttributeValue(attrName, jsonObject);
                    return attributeValue != null ? new AbstractMap.SimpleEntry<>(attrName, attributeValue) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    private AttributeValue createAttributeValue(String id, JsonObject jsonObject) {
        if (!jsonObject.has(id) || jsonObject.get(id).isJsonNull()) {
            return null;
        }
        try {
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setRaw(jsonObject.get(id).getAsString());
            return attributeValue;
        } catch (ZkpException e) {
            throw new IssuerSdkException(IssuerSdkErrorCode.FAILED_TO_GENERATE_ZKP_ATTRIBUTE_VALUE);
        }
    }

    private HashMap<String, ClaimInfo> getClaimInfo(List<SchemaClaims> schemaClaimList, String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        return schemaClaimList.stream()
                .flatMap(schemaClaim -> schemaClaim.getItems().stream()
                        .map(item -> createClaimInfo(schemaClaim.getNamespace().getId(), item, jsonObject)))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ClaimInfo::getCode, claimInfo -> claimInfo,
                        (existing, replacement) -> existing, HashMap::new));
    }

    private ClaimInfo createClaimInfo(String namespace, ClaimDef item, JsonObject jsonObject) {
        String claimCode = namespace + "." + item.getId();
        if (!jsonObject.has(claimCode) || jsonObject.get(claimCode).isJsonNull()) {
            return null;
        }
        ClaimInfo claimInfo = new ClaimInfo();
        claimInfo.setCode(claimCode);
        claimInfo.setValue(jsonObject.get(claimCode).getAsString().getBytes(StandardCharsets.UTF_8));
        return claimInfo;
    }

    private ZkpIssueProfile getIssueProfile(SdkIssueProfile byVcPlanId) {
        ZkpIssueProfile issueProfile = new ZkpIssueProfile();
        issueProfile.setType(ProfileType.ISSUE_PROFILE.getRawValue());
        issueProfile.setEncoding(StandardCharsets.UTF_8.name());
        issueProfile.setDescription(byVcPlanId.getDescription());
        issueProfile.setTitle(byVcPlanId.getTitle());
        issueProfile.setLanguage(byVcPlanId.getLanguage());

        ZkpInnerIssueProfile innerIssueProfile = new ZkpInnerIssueProfile();
        innerIssueProfile.setIssuer(getIssuer());
        innerIssueProfile.setCredentialSchema(createCredentialSchema(byVcPlanId.getVcSchemaId()));
        innerIssueProfile.setProcess(createIssueProcess(byVcPlanId));

        String definitionId = byVcPlanId.getDefinitionId();
        if (Optional.ofNullable(definitionId).isPresent()) {
            CredentialOffer credentialOffer = createCredentialOffer(definitionId);
            innerIssueProfile.setCredentialOffer(credentialOffer);
        }

        issueProfile.setProfile(innerIssueProfile);
        return issueProfile;
    }

    private CredentialOffer createCredentialOffer(String definitionId) {
        SdkZkpCredentialDefinition definition = issueDataProvider
                .findZkpCredentialDefinitionByDefinitionId(definitionId);

        BigInteger issuerNonce = new BigIntegerUtil().createRandomBigInteger(ZkpCryptoConstants.LARGE_NONCE);
        KeyCorrectnessProof correctnessProof = walletProvider.getCorrectnessProof(definition.getAlias());

        try {
            return new ZkpCredentialManager().createCredentialOffer(correctnessProof,
                    definition.getSchemaId(), definition.getDefinitionId(), issuerNonce);
        } catch (ZkpException e) {
            log.error("Failed Create Credential Offer: {}", e.getMessage());
            throw new IssuerSdkException(IssuerSdkErrorCode.FAILED_TO_GENERATE_CREDENTIAL_OFFER);
        }
    }

    private ProviderDetail getIssuer() {
        SdkIssuerInfo issuerInfo = issueDataProvider.getIssuerInfo();

        ProviderDetail issuer = new ProviderDetail();
        issuer.setCertVcRef(certificateVcProvider.getCertificateVcURL(issuerInfo));
        issuer.setDid(issuerInfo.getDid());
        issuer.setName(issuerInfo.getName());
        return issuer;
    }

    private org.omnione.did.data.model.vc.CredentialSchema createCredentialSchema(Long vcSchemaId) {
        org.omnione.did.data.model.schema.VcSchema vcSchemaById = issueDataProvider.getVcSchemaById(vcSchemaId);
        org.omnione.did.data.model.vc.CredentialSchema credentialSchema = new org.omnione.did.data.model.vc.CredentialSchema();
        credentialSchema.setId(vcSchemaById.getId());
        credentialSchema.setType(CredentialSchemaType.OSD_SCHEMA_CREDENTIAL.getRawValue());
        return credentialSchema;
    }

    private IssueProcess createIssueProcess(SdkIssueProfile byVcPlanId) {
        IssueProcess issueProcess = new IssueProcess();
        issueProcess.setEndpoints(byVcPlanId.getEndpoints());
        issueProcess.setReqE2e(createReqE2e(byVcPlanId));
        return issueProcess;
    }

    private ReqE2e createReqE2e(SdkIssueProfile byVcPlanId) {
        ReqE2e reqE2e = new ReqE2e();
        reqE2e.setCurve(byVcPlanId.getCurve());
        reqE2e.setPadding(byVcPlanId.getPadding());
        reqE2e.setCipher(byVcPlanId.getCipher());
        return reqE2e;
    }

    private org.omnione.did.data.model.schema.VcSchema getVcSchema(Long vcSchemaId) {
        return issueDataProvider.getVcSchemaById(vcSchemaId);
    }

}
