package org.omnione.did.sdk.issuer.protocol.vc;

import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.vc.VcMeta;
import org.omnione.did.sdk.issuer.exception.IssuerSdkErrorCode;
import org.omnione.did.sdk.issuer.exception.IssuerSdkException;
import org.omnione.did.sdk.issuer.model.data.ReqRevokeVc;
import org.omnione.did.sdk.issuer.model.data.RequestProof;
import org.omnione.did.sdk.issuer.model.domain.SdkRevokeVc;
import org.omnione.did.sdk.issuer.model.domain.SdkTransaction;
import org.omnione.did.sdk.issuer.model.domain.SdkVc;
import org.omnione.did.sdk.issuer.model.dto.*;
import org.omnione.did.sdk.issuer.model.enums.VerifyAuthType;
import org.omnione.did.sdk.issuer.provider.StorageProvider;
import org.omnione.did.sdk.issuer.provider.VcStatusDataProvider;
import org.omnione.did.common.util.crypto.BaseCryptoUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.sdk.issuer.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * SDK Core Class for managing Verifiable Credentials (VCs) status operations.
 */
public class VcStatusProtocolImpl implements VcStatusProtocol {

    private static final Logger log = LoggerFactory.getLogger(VcStatusProtocolImpl.class);

    protected final VcStatusDataProvider vcStatusDataProvider;
    protected final StorageProvider storageProvider;

    public VcStatusProtocolImpl(VcStatusDataProvider vcStatusDataProvider, StorageProvider storageProvider) {
        this.vcStatusDataProvider = vcStatusDataProvider;
        this.storageProvider = storageProvider;
    }

    @Override
    public InspectProposeRevokeResDto inspectProposeRevoke(InspectProposeRevokeReqDto request,
            SdkTransaction transaction) {
        try {
            log.debug("=== Starting Inspect Propose Revoke ===");
            VcMeta vcMeta = storageProvider.getVcMetaByVcId(request.getVcId());
            if (Objects.isNull(vcMeta)) {
                throw new IssuerSdkException(IssuerSdkErrorCode.VC_NOT_FOUND);
            }
            if (isRevoked(vcMeta)) {
                throw new IssuerSdkException(IssuerSdkErrorCode.REVOKED_VC);
            }

            String txId = transaction.getTxId();
            String issuerNonce = BaseMultibaseUtil.encode(BaseCryptoUtil.generateNonce(16));
            VerifyAuthType verifyAuthType = vcStatusDataProvider.getRevokeAuthType();

            SdkRevokeVc revokeVc = vcStatusDataProvider.findRevokeVcByVcId(request.getVcId());

            revokeVc.setVcId(request.getVcId());
            revokeVc.setNonce(issuerNonce);
            revokeVc.setTransactionId(transaction.getId());
            vcStatusDataProvider.saveRevokeVc(revokeVc);

            return InspectProposeRevokeResDto.builder()
                    .txId(txId)
                    .issuerNonce(issuerNonce)
                    .authType(verifyAuthType)
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during inspectProposeRevoke: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during inspectProposeRevoke: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_REVOKE_PROPOSE_FAILED);
        }
    }

    @Override
    public RevokeVcResDto revokeVc(RevokeVcReqDto request, SdkTransaction transaction) {
        try {
            log.debug("=== Starting Revoke VC ===");

            SdkRevokeVc revokeVc = vcStatusDataProvider.findRevokeVcByTransactionId(transaction.getId());
            isValidRequest(revokeVc, request.getRequest());
            verifySign(request.getRequest());

            vcStatusDataProvider.updateVcStatus(revokeVc.getVcId(),
                    org.omnione.did.data.model.enums.vc.VcStatus.REVOKED);

            revokeVc.setStatus(org.omnione.did.data.model.enums.vc.VcStatus.REVOKED);
            vcStatusDataProvider.saveRevokeVc(revokeVc);

            log.debug("=== Finished Revoke VC ===");
            return RevokeVcResDto.builder()
                    .txId(request.getTxId())
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during revokeVc: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during revokeVc: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_REVOKE_FAILED);
        }
    }

    @Override
    public UpdateVcStatusResDto updateVcStatus(UpdateVcStatusReqDto request) {
        try {
            log.debug("=== Starting Update VC Status ===");
            VcMeta vcMeta = storageProvider.getVcMetaByVcId(request.getVcId());
            if (Objects.isNull(vcMeta)) {
                throw new IssuerSdkException(IssuerSdkErrorCode.VC_NOT_FOUND);
            }
            if (isRevoked(vcMeta)) {
                throw new IssuerSdkException(IssuerSdkErrorCode.REVOKED_VC);
            }

            SdkVc sdkVc = vcStatusDataProvider.findVcByVcId(request.getVcId());
            if (sdkVc != null) {
                sdkVc.setStatus(request.getVcStatus().getRawValue());
                vcStatusDataProvider.saveVc(sdkVc);
            }
            vcStatusDataProvider.updateVcStatus(request.getVcId(), request.getVcStatus());

            return UpdateVcStatusResDto.builder()
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during updateVcStatus: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during updateVcStatus: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_UPDATE_STATUS_FAILED);
        }
    }

    @Override
    public CompleteRevokeResDto completeRevoke(CompleteRevokeReqDto request, SdkTransaction transaction) {
        try {
            log.debug("=== Starting Complete Revoke ===");

            return CompleteRevokeResDto.builder()
                    .txId(request.getTxId())
                    .build();
        } catch (IssuerSdkException e) {
            log.error("SdkException occurred during completeRevoke: {}", e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred during completeRevoke: {}", e.getMessage(), e);
            throw new IssuerSdkException(IssuerSdkErrorCode.TR_VC_REVOKE_COMPLETE_FAILED);
        }
    }

    private void verifySign(RequestProof request) {
        Proof proof = request.getProof();
        String verificationMethod = proof.getVerificationMethod();
        DidDocument holderDidDoc = storageProvider.findDidDoc(verificationMethod);

        ValidationUtil.verifySign(request, holderDidDoc);
    }

    private void isValidRequest(SdkRevokeVc revokeVc, ReqRevokeVc reqRevokeVc) {
        if (!revokeVc.getVcId().equals(reqRevokeVc.getVcId())) {
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_ID_NOT_MATCH);
        }
        byte[] revokeVcIssuerNonce = BaseMultibaseUtil.decode(revokeVc.getNonce());
        byte[] requestIssuerNonce = BaseMultibaseUtil.decode(reqRevokeVc.getIssuerNonce());

        if (!Arrays.equals(revokeVcIssuerNonce, requestIssuerNonce)) {
            throw new IssuerSdkException(IssuerSdkErrorCode.ISSUER_NONCE_INVALID);
        }
    }

    protected boolean isRevoked(VcMeta vcMeta) {
        return org.omnione.did.data.model.enums.vc.VcStatus.REVOKED.getRawValue().equals(vcMeta.getStatus());
    }
}
