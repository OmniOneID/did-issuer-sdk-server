---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
---

Issuer SDK API
==

- Topic: Issuer SDK API
- Date: 2026-11-09
- Version: v3.0.0

| Version | Date | Changes |
| --- | --- | --- |
| v3.0.0 | 2026-11-09 | Documented all public APIs and corrected signatures |
| v1.0.0 | 2026-10-29 | API reference for extracted SDK |

<div style="page-break-after: always;"></div>

# Table of Contents
- [1. VC issuance](#1-vc-issuance)
- [2. VC status and revocation](#2-vc-status-and-revocation)
- [3. Provider integration](#3-provider-integration)
- [4. Data models](#4-data-models)
- [5. Auxiliary Utilities](#5-auxiliary-utilities)

# Feature List
## 1. VC issuance

### requestOffer

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`requestOffer`

#### Function Description
Creates a VC issuance offer. The SDK checks the VC plan by calling `IssueDataProvider.findIssueProfileByVcPlanId`. It does not inspect the returned value, so the provider must throw for an unknown plan. The SDK then generates a UUID offer ID and saves an `SdkVcOffer` through `IssueDataProvider.saveVcOffer`. The saved offer expires 24 hours after creation, while the `validUntil` in the returned payload is 3 minutes after creation. An `IssuerSdkException` from a provider is rethrown; any other exception becomes `TR_VC_OFFER_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `OfferIssueVcReqDto` | Request DTO. The SDK reads `vcPlanId`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `OfferIssueVcResDto` | `issueOfferPayload` holds `offerId`, `type` (`IssueOffer`), `vcPlanId`, `issuer` (issuer DID from `getIssuerInfo`) and `validUntil` (UTC string). `txId` is not set. |

#### Function Declaration

```java
OfferIssueVcResDto requestOffer(OfferIssueVcReqDto request);
```

#### Function Usage

```java
OfferIssueVcResDto offer = issueVcProtocol.requestOffer(
        OfferIssueVcReqDto.builder().vcPlanId("vcplan.example").build());
String offerId = offer.getIssueOfferPayload().getOfferId();
```

### inspectIssuePropose

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectIssuePropose`

#### Function Description
Validates the issuance proposal and generates the transaction identifiers. The SDK checks the VC plan in the same way as `requestOffer`. If `offerId` is not blank, it loads the offer with `IssueDataProvider.findVcOfferByOfferId` and throws `VC_OFFER_EXPIRED` when `SdkVcOffer.validUntil` has passed. It then generates a UUID `txId` and a 17-character `refId`. The SDK saves nothing. The host must create the `SdkTransaction` with these values, the `vcPlanId` and the issue profile ID, set `SdkVcOffer.transactionId` when an offer was used, and record an `INSPECT_ISSUE_PROPOSE` sub-transaction. Unexpected exceptions become `UNKNOWN_SERVER_ERROR`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectIssueProposeReqDto` | Request DTO. The SDK reads `vcPlanId` (required) and `offerId` (optional). `id` and `issuer` are not read. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectIssueProposeResDto` | `txId` (new UUID) and `refId` (new reference ID). |

#### Function Declaration

```java
InspectIssueProposeResDto inspectIssuePropose(InspectIssueProposeReqDto request);
```

#### Function Usage

```java
InspectIssueProposeResDto proposal = issueVcProtocol.inspectIssuePropose(
        InspectIssueProposeReqDto.builder()
                .vcPlanId("vcplan.example")
                .offerId(offerId)
                .build());
// The host stores a transaction with proposal.getTxId() and proposal.getRefId().
```

### generateIssueProfile

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`generateIssueProfile`

#### Function Description
Generates and signs the issue profile for a transaction and user. The SDK loads the issue profile with `findIssueProfileByVcPlanId(transaction.vcPlanId)`, the VC schema with `getVcSchemaById`, and the issuer with `getIssuerInfo` and `CertificateVcProvider.getCertificateVcURL`. If `SdkIssueProfile.definitionId` is set, it also adds a ZKP credential offer built from `findZkpCredentialDefinitionByDefinitionId` and `WalletProvider.getCorrectnessProof`. It creates an ephemeral E2E key pair on the profile curve and a 16-byte issuer nonce, then signs the profile with `WalletProvider.generateCompactSignature("assert", ...)` (verification method `<issuer DID>#assert`). It saves an `SdkVcProfile` and an `SdkE2E` for the transaction. `setCertificatePublisher` must have been called first. Failures become `TR_VC_ISSUE_PROFILE_FAILED` unless a provider throws `IssuerSdkException`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `GenerateIssueProfileReqDto` | Request DTO. The SDK only reads `txId` and returns it. `holder` and `userId` are for the host's user lookup. | M |
| `transaction` | `SdkTransaction` | Transaction found by the host. The SDK reads `id` and `vcPlanId`. | M |
| `user` | `SdkUser` | User found by the host. The SDK reads `id` and `did` and stores them in the VC profile. `did` may be `null` in the proxy flow. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `GenerateIssueProfileResDto` | `txId` from the request, the signed `profile` (`ZkpIssueProfile`) and `authNonce` (the issuer nonce, multibase). |

#### Function Declaration

```java
GenerateIssueProfileResDto generateIssueProfile(GenerateIssueProfileReqDto request, SdkTransaction transaction, SdkUser user);
```

#### Function Usage

```java
SdkTransaction tx = issueDataProvider.validateAndFindTransaction(
        request.getTxId(), SubTransactionType.INSPECT_ISSUE_PROPOSE);
SdkUser user = SdkUser.builder().id(1L).did("did:example:holder").build();
GenerateIssueProfileResDto res = issueVcProtocol.generateIssueProfile(request, tx, user);
```

### issueVc

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVc`

#### Function Description
Issues a VC. Steps:
1. If `accE2e.proof` is present, loads the signer's DID document with `StorageProvider.findDidDoc(proof.verificationMethod)` and verifies the signature. Without a proof, the check is skipped.
2. Loads `SdkE2E` and `SdkVcProfile` for the transaction, derives the shared key from `accE2e.publicKey`, the stored session key and the profile nonce, and decrypts `encReqVc` with `accE2e.iv`.
3. Checks `transaction.refId` (`REF_ID_INVALID`), the profile ID (`VC_PROFILE_INVALID`) and the issuer nonce (`VC_PROFILE_ISSUER_NONCE_INVALID`) against the decrypted request.
4. Builds the VC from the VC schema (`findIssueProfileById`, `getVcSchemaById`) and `user.data`, signs it with the issuer `assert` key, and registers its `VcMeta` with `StorageProvider.registerVcMeta`.
5. If the request contains a ZKP credential request, issues the ZKP credential with `findZkpCredentialDefinitionByDefinitionId`, `findZkpSchemaBySchemaId`, `WalletProvider.credSignature` and `WalletProvider.signatureCorrectnessProof`.
6. If `findVcByUserIdAndVcPlanId` returns a previous VC whose metadata status is not `REVOKED`, revokes it on the ledger (`StorageProvider.updateVcStatus`) and saves it with status `REVOKED`. Then saves the new `SdkVc`.

`setCertificatePublisher` must have been called first. Failures become `TR_VC_ISSUE_FAILED` unless a provider throws `IssuerSdkException`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | Request DTO. The SDK reads `txId`, `accE2e` (`publicKey`, `iv`, optional `proof`) and `encReqVc` (multibase). | M |
| `transaction` | `SdkTransaction` | Transaction found by the host. The SDK reads `id`, `txId`, `refId`, `vcPlanId` and `issueProfileId`. | M |
| `user` | `UserInfoDto` | User data. The SDK reads `id`, `data` (claim values), `validFrom` and `validUntil`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | `txId` from the request and `e2e`: `encVc` (multibase, encrypted JSON with the VC and the optional ZKP credential) and `iv` (multibase). |

#### Function Declaration

```java
IssueVcResDto issueVc(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user);
```

#### Function Usage

```java
SdkTransaction tx = issueDataProvider.validateAndFindTransaction(
        request.getTxId(), SubTransactionType.GENERATE_ISSUE_PROFILE);
UserInfoDto user = UserInfoDto.builder()
        .id(1L)
        .data("{\"org.example.name\":\"Jane Doe\"}")
        .build();
IssueVcResDto res = issueVcProtocol.issueVc(request, tx, user);
```

### issueVcFromProxy

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVcFromProxy`

#### Function Description
Issues a VC in the proxy flow, where the holder DID is known only at issuance. If `didAuth.proof` is present, the SDK loads the DID document of `didAuth.did` and verifies the signature. It loads the VC profile, throws `ISSUER_NONCE_INVALID` if `didAuth.authNonce` differs from `SdkVcProfile.nonce`, sets `SdkVcProfile.did` to `didAuth.did`, saves the profile with `saveVcProfile`, and then runs `issueVc`. Exceptions raised before `issueVc` starts, such as a signature failure or a missing `didAuth`, are not converted to `IssuerSdkException`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | Request DTO with the `issueVc` fields and `didAuth` (`did`, `authNonce`, optional `proof`). | M |
| `transaction` | `SdkTransaction` | Same as `issueVc`. | M |
| `user` | `UserInfoDto` | Same as `issueVc`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | Same as `issueVc`. |

#### Function Declaration

```java
IssueVcResDto issueVcFromProxy(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user);
```

#### Function Usage

```java
IssueVcResDto res = issueVcProtocol.issueVcFromProxy(request, tx, user);
```

### completeVc

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`completeVc`

#### Function Description
Confirms that the holder received the issued VC. The SDK loads the VC with `IssueDataProvider.findVcByTxId(request.txId)` and throws `VC_ID_NOT_MATCH` if its `vcId` differs from the request. It changes no state and does not use `transaction`; the host marks the transaction finished and records a `COMPLETE_VC` sub-transaction, which `issueVcResult` relies on. Other failures become `TR_VC_ISSUE_COMPLETE_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteVcReqDto` | Request DTO. The SDK reads `txId` and `vcId`. | M |
| `transaction` | `SdkTransaction` | Transaction found by the host. Not used by the current implementation. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteVcResDto` | `txId` from the request. |

#### Function Declaration

```java
CompleteVcResDto completeVc(CompleteVcReqDto request, SdkTransaction transaction);
```

#### Function Usage

```java
SdkTransaction tx = issueDataProvider.validateAndFindTransaction(
        request.getTxId(), SubTransactionType.ISSUE_VC);
CompleteVcResDto res = issueVcProtocol.completeVc(request, tx);
```

### issueVcResult

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVcResult`

#### Function Description
Returns the issuance result for an offer ID. The SDK loads the offer with `findVcOfferByOfferId` and the transaction with `findTransactionById(SdkVcOffer.transactionId)`. If no transaction is returned, the result is `false`. Otherwise it loads the latest sub-transaction with `findSubTransactionByTransactionIdOrderByStepDesc`; the result is `true` only when its type is `COMPLETE_VC`. Failures become `TR_VC_ISSUE_RESULT_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | Issuance offer ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResultResDto` | `offerId`, `result` and `txId` (`null` when no transaction is linked). |

#### Function Declaration

```java
IssueVcResultResDto issueVcResult(String offerId);
```

#### Function Usage

```java
boolean issued = issueVcProtocol.issueVcResult(offerId).getResult();
```

### IssueVcProtocolImpl

#### Class Name
`IssueVcProtocolImpl` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`IssueVcProtocolImpl` (constructor)

#### Function Description
`IssueVcProtocolImpl` implements `IssueVcProtocol`; see the functions above in this chapter. The constructor stores the providers without checking them for `null`. Call `setCertificatePublisher` before using the instance.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `issueDataProvider` | `IssueDataProvider` | Issuance data provider | M |
| `walletProvider` | `WalletProvider` | Signing and ZKP wallet provider | M |
| `storageProvider` | `StorageProvider` | DID document and VC metadata provider | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcProtocolImpl` | New protocol instance |

#### Function Declaration

```java
public IssueVcProtocolImpl(IssueDataProvider issueDataProvider, WalletProvider walletProvider, StorageProvider storageProvider)
```

#### Function Usage

```java
IssueVcProtocolImpl issueVcProtocol = new IssueVcProtocolImpl(issueDataProvider, walletProvider, storageProvider);
issueVcProtocol.setCertificatePublisher(certificateVcProvider);
```

### setCertificatePublisher

#### Class Name
`IssueVcProtocolImpl` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`setCertificatePublisher`

#### Function Description
Sets the `CertificateVcProvider`. There is no default. `generateIssueProfile`, `issueVc` and `issueVcFromProxy` use it for the issuer's certificate VC reference; without it they fail with `TR_VC_ISSUE_PROFILE_FAILED` or `TR_VC_ISSUE_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `certificateVcProvider` | `CertificateVcProvider` | Provider of the certificate VC URL | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
public void setCertificatePublisher(CertificateVcProvider certificateVcProvider)
```

#### Function Usage

```java
issueVcProtocol.setCertificatePublisher(issuerInfo -> issuerInfo.getCertificateUrl());
```

## 2. VC status and revocation

### inspectProposeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectProposeRevoke`

#### Function Description
Validates a revocation proposal. The SDK loads the VC metadata with `StorageProvider.getVcMetaByVcId` and throws `VC_NOT_FOUND` if it is `null` or `REVOKED_VC` if it is already revoked. It generates a 16-byte issuer nonce, reads the required authentication type with `VcStatusDataProvider.getRevokeAuthType`, and saves the revocation record: it takes the object from `findRevokeVcByVcId`, sets `vcId`, `nonce` and `transactionId`, and calls `saveRevokeVc`. The host must create the revocation transaction before the call and record an `INSPECT_REVOKE_PROPOSE` sub-transaction after it. Other failures become `TR_VC_REVOKE_PROPOSE_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectProposeRevokeReqDto` | Request DTO. The SDK reads `vcId`. | M |
| `transaction` | `SdkTransaction` | Revocation transaction created by the host. The SDK reads `id` and `txId`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectProposeRevokeResDto` | `txId` of the transaction, `issuerNonce` (multibase) and `authType` (the integer value of the `VerifyAuthType`). |

#### Function Declaration

```java
InspectProposeRevokeResDto inspectProposeRevoke(InspectProposeRevokeReqDto request, SdkTransaction transaction);
```

#### Function Usage

```java
SdkTransaction tx = SdkTransaction.builder()
        .id(10L)
        .txId("3f2c6e1a-0000-4000-8000-000000000001")
        .type(TransactionType.REVOKE_VC)
        .build();
InspectProposeRevokeResDto res = vcStatusProtocol.inspectProposeRevoke(
        InspectProposeRevokeReqDto.builder().vcId("urn:uuid:vc-example-0001").build(), tx);
```

### revokeVc

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`revokeVc`

#### Function Description
Revokes a VC. The SDK loads the revocation record with `findRevokeVcByTransactionId(transaction.id)`, throws `VC_ID_NOT_MATCH` if `request.request.vcId` differs and `ISSUER_NONCE_INVALID` if the decoded issuer nonces differ. It then loads the DID document with `StorageProvider.findDidDoc(proof.verificationMethod)` and verifies the signature of `request.request`. Finally it calls `VcStatusDataProvider.updateVcStatus(vcId, REVOKED)`, sets the record status to `REVOKED` and saves it. The SDK does not call `StorageProvider.updateVcStatus` in this flow. A signature failure and other errors become `TR_VC_REVOKE_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `RevokeVcReqDto` | Request DTO. The SDK reads `txId` and `request` (`ReqRevokeVc`: `vcId`, `issuerNonce`, `proof`). | M |
| `transaction` | `SdkTransaction` | Revocation transaction found by the host. The SDK reads `id`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `RevokeVcResDto` | `txId` from the request. |

#### Function Declaration

```java
RevokeVcResDto revokeVc(RevokeVcReqDto request, SdkTransaction transaction);
```

#### Function Usage

```java
SdkTransaction tx = vcStatusDataProvider.validateAndFindTransaction(
        request.getTxId(), SubTransactionType.INSPECT_REVOKE_PROPOSE);
RevokeVcResDto res = vcStatusProtocol.revokeVc(request, tx);
```

### updateVcStatus

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`updateVcStatus`

#### Function Description
Changes the status of a VC without a holder request. The SDK loads the VC metadata with `StorageProvider.getVcMetaByVcId` and throws `VC_NOT_FOUND` if it is `null` or `REVOKED_VC` if the VC is revoked. If `VcStatusDataProvider.findVcByVcId` returns a VC, the SDK sets its `status` to the raw value of the new status and saves it with `saveVc`. It then calls `VcStatusDataProvider.updateVcStatus(vcId, vcStatus)`. The SDK does not call `StorageProvider.updateVcStatus` and does not check who calls this function, so the host must authorize the caller. Other failures become `TR_VC_UPDATE_STATUS_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `UpdateVcStatusReqDto` | Request DTO. `vcId` and `vcStatus` (`org.omnione.did.data.model.enums.vc.VcStatus`) are required. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `UpdateVcStatusResDto` | Empty response; `txId` is not set. |

#### Function Declaration

```java
UpdateVcStatusResDto updateVcStatus(UpdateVcStatusReqDto request);
```

#### Function Usage

```java
vcStatusProtocol.updateVcStatus(UpdateVcStatusReqDto.builder()
        .vcId("urn:uuid:vc-example-0001")
        .vcStatus(VcStatus.INACTIVE)
        .build());
```

### completeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`completeRevoke`

#### Function Description
Completes a VC revocation transaction. The current implementation calls no provider, does not use `transaction` and only returns the request `txId`. The host records the `COMPLETE_REVOKE` sub-transaction.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteRevokeReqDto` | Request DTO. The SDK reads `txId`. | M |
| `transaction` | `SdkTransaction` | Revocation transaction found by the host. Not used by the current implementation. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteRevokeResDto` | `txId` from the request. |

#### Function Declaration

```java
CompleteRevokeResDto completeRevoke(CompleteRevokeReqDto request, SdkTransaction transaction);
```

#### Function Usage

```java
SdkTransaction tx = vcStatusDataProvider.validateAndFindTransaction(
        request.getTxId(), SubTransactionType.REVOKE_VC);
CompleteRevokeResDto res = vcStatusProtocol.completeRevoke(request, tx);
```

### VcStatusProtocolImpl

#### Class Name
`VcStatusProtocolImpl` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`VcStatusProtocolImpl` (constructor)

#### Function Description
`VcStatusProtocolImpl` implements `VcStatusProtocol`; see the functions above in this chapter. The constructor stores the providers without checking them for `null`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcStatusDataProvider` | `VcStatusDataProvider` | VC status data provider | M |
| `storageProvider` | `StorageProvider` | DID document and VC metadata provider | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `VcStatusProtocolImpl` | New protocol instance |

#### Function Declaration

```java
public VcStatusProtocolImpl(VcStatusDataProvider vcStatusDataProvider, StorageProvider storageProvider)
```

#### Function Usage

```java
VcStatusProtocol vcStatusProtocol = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

## 3. Provider integration

The host application implements these providers. The SDK does not provide an HTTP server or database schema.

| Interface | Responsibility |
| --- | --- |
| `IssueDataProvider` | Offers, transactions, profiles and VCs |
| `VcStatusDataProvider` | VC status, revocation data and configuration |
| `WalletProvider` | Signing and ZKP wallet operations |
| `StorageProvider` | DID documents and VC metadata |
| `CertificateVcProvider` | Certificate VC URL |

```java
IssueVcProtocolImpl issue = new IssueVcProtocolImpl(issueDataProvider, walletProvider, storageProvider);
issue.setCertificatePublisher(certificateVcProvider);
VcStatusProtocol status = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

`IssueVcProtocolImpl` needs a `CertificateVcProvider` for `generateIssueProfile`, `issueVc` and `issueVcFromProxy`, so set it with `IssueVcProtocolImpl#setCertificatePublisher` before those calls. The `Sdk*` classes are described in [4. Data models](#4-data-models). Request and response DTOs are in `source/did-issuer-sdk/src/main/java/org/omnione/did/sdk/issuer/model/dto/`.

Rules for all providers:

- **Errors**: The protocol implementations rethrow `IssuerSdkException` unchanged. Any other exception from a provider, including a `NullPointerException` caused by a `null` return, is replaced by an `IssuerSdkException` with the failure code of the protocol function (for example `TR_VC_ISSUE_FAILED`), and the original exception is only logged. Throw `IssuerSdkException` to keep your own error code. `issueVcFromProxy` does not convert exceptions raised before it delegates to `issueVc`.
- **Not found**: The SDK does not check most return values for `null`. When the requested item does not exist, throw an exception. Only these methods may return "nothing": `IssueDataProvider.findTransactionById` (`null`), `IssueDataProvider.findVcByUserIdAndVcPlanId` (`Optional.empty()`), `VcStatusDataProvider.findVcByVcId` (`null`) and `StorageProvider.getVcMetaByVcId` (`null`). `VcStatusDataProvider.findRevokeVcByVcId` must return a new empty object instead of `null`.
- **Saving**: The `save*` methods receive new objects (`id` is `null`) and objects that a `find*` method returned earlier (`id` is set). Insert the first kind and update the second.
- **Transactions**: The SDK does not create transactions or record sub-transactions. The host creates each `SdkTransaction`, records an `SdkSubTransaction` after each step, links the offer to the transaction, and passes the transaction to the protocol functions.

### IssueDataProvider

`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`) stores and reads the issuance data: offers, transactions, issue profiles, issuer information, VC profiles, E2E session data, issued VCs, ZKP definitions and VC schemas. `IssueVcProtocolImpl` calls it. The following sketch shows the three kinds of "not found" behavior.

```java
public class InMemoryIssueDataProvider implements IssueDataProvider {
    private final Map<String, SdkVcOffer> offers = new ConcurrentHashMap<>();
    private final Map<Long, SdkTransaction> transactions = new ConcurrentHashMap<>();
    private final Map<String, List<SdkVc>> vcsByUserAndPlan = new ConcurrentHashMap<>();

    @Override
    public SdkVcOffer findVcOfferByOfferId(String offerId) {
        SdkVcOffer offer = offers.get(offerId);
        if (offer == null) {
            throw new IssuerSdkException(IssuerSdkErrorCode.VC_OFFER_NOT_FOUND);
        }
        return offer;
    }

    @Override
    public SdkTransaction findTransactionById(Long id) {
        return id == null ? null : transactions.get(id); // null: no transaction yet
    }

    @Override
    public Optional<SdkVc> findVcByUserIdAndVcPlanId(Long userId, String vcPlanId) {
        List<SdkVc> vcs = vcsByUserAndPlan.getOrDefault(userId + "/" + vcPlanId, List.of());
        return vcs.isEmpty() ? Optional.empty() : Optional.of(vcs.get(vcs.size() - 1));
    }

    // The other methods are omitted.
}
```

### IssueDataProvider.saveVcOffer

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveVcOffer`

#### Function Description
Saves an issuance offer. `requestOffer` calls it with a new object (`id` and `transactionId` are `null`). The host calls it again when it links the offer to a transaction; the SDK never sets `transactionId`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcOffer` | `SdkVcOffer` | Offer with `offerId`, `offerType` (`ISSUE_OFFER`), `vcPlanId`, `did` (issuer DID) and `validUntil` (24 hours later) set by the SDK | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void saveVcOffer(SdkVcOffer vcOffer);
```

### IssueDataProvider.findVcOfferByOfferId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findVcOfferByOfferId`

#### Function Description
Returns the offer with the given offer ID. Called by `inspectIssuePropose` (when the request has an `offerId`; reads `validUntil`) and by `issueVcResult` (reads `transactionId`). If the offer does not exist, throw an exception, for example `IssuerSdkException(VC_OFFER_NOT_FOUND)`. A `null` return makes those functions fail with `UNKNOWN_SERVER_ERROR` or `TR_VC_ISSUE_RESULT_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | Offer ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVcOffer` | Offer. Fill at least `validUntil` and `transactionId`. |

#### Function Declaration

```java
SdkVcOffer findVcOfferByOfferId(String offerId);
```

### IssueDataProvider.validateAndFindTransaction

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`validateAndFindTransaction`

#### Function Description
Finds a transaction by `txId` and checks that it may continue. The SDK does not call this method; it is part of the interface so that the host can find the `SdkTransaction` it passes to the protocol functions. A typical implementation throws (for example `TRANSACTION_NOT_FOUND`, `TRANSACTION_INVALID`) when the transaction does not exist, has expired, or its latest completed sub-transaction is not of the given type.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | Transaction ID | M |
| `type` | `SubTransactionType` | Expected type of the latest sub-transaction (the previous step) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | Transaction. Fill the fields that the next protocol function reads. |

#### Function Declaration

```java
SdkTransaction validateAndFindTransaction(String txId, SubTransactionType type);
```

### IssueDataProvider.findTransactionById

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findTransactionById`

#### Function Description
Returns the transaction with the given key. `issueVcResult` calls it with `SdkVcOffer.transactionId`, which is `null` until the host links the offer. Return `null` when `id` is `null` or the transaction does not exist; the SDK then reports `result = false`. Throwing an exception makes `issueVcResult` fail with `TR_VC_ISSUE_RESULT_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Transaction key (`SdkTransaction.id`). May be `null`. | O |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | Transaction, or `null`. The SDK reads `id` and `txId`. |

#### Function Declaration

```java
SdkTransaction findTransactionById(Long id);
```

### IssueDataProvider.findSubTransactionByTransactionIdOrderByStepDesc

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findSubTransactionByTransactionIdOrderByStepDesc`

#### Function Description
Returns the latest sub-transaction (highest `step`) of a transaction. `issueVcResult` calls it and reports success only if the returned `type` is `COMPLETE_VC`. Must not return `null`; a `null` return makes `issueVcResult` fail with `TR_VC_ISSUE_RESULT_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | Transaction key | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkSubTransaction` | Latest sub-transaction. The SDK reads `type`. |

#### Function Declaration

```java
SdkSubTransaction findSubTransactionByTransactionIdOrderByStepDesc(Long transactionId);
```

### IssueDataProvider.getTransactionExpirationTime

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getTransactionExpirationTime`

#### Function Description
Returns the transaction lifetime. The SDK does not call this method; the host uses it to set `SdkTransaction.expiredAt` when it creates an issuance transaction. The unit is the host's choice; seconds are typical.

#### Input Parameters

None.

#### Output Parameters

| Type | Description |
| --- | --- |
| `long` | Transaction lifetime |

#### Function Declaration

```java
long getTransactionExpirationTime();
```

### IssueDataProvider.findIssueProfileByVcPlanId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findIssueProfileByVcPlanId`

#### Function Description
Returns the issue profile of a VC plan. `requestOffer` and `inspectIssuePropose` use it as the VC plan check and ignore the return value, so the method must throw (for example `IssuerSdkException(VC_PLAN_ID_INVALID)`) when the plan does not exist or must not be issued. Returning `null` lets those functions accept the plan. `generateIssueProfile` calls it with `SdkTransaction.vcPlanId` and builds the profile from the returned object.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcPlanId` | `String` | VC plan ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssueProfile` | Issue profile. The SDK reads `vcSchemaId`, `title`, `description`, `language`, `endpoints`, `curve`, `cipher`, `padding` and `definitionId`. |

#### Function Declaration

```java
SdkIssueProfile findIssueProfileByVcPlanId(String vcPlanId);
```

### IssueDataProvider.findIssueProfileById

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findIssueProfileById`

#### Function Description
Returns the issue profile with the given key. `issueVc` calls it with `SdkTransaction.issueProfileId`. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Issue profile key (`SdkIssueProfile.id`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssueProfile` | Issue profile. The SDK reads `vcSchemaId`. |

#### Function Declaration

```java
SdkIssueProfile findIssueProfileById(Long id);
```

### IssueDataProvider.getIssuerInfo

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getIssuerInfo`

#### Function Description
Returns the issuer's own information. Called by `requestOffer`, `generateIssueProfile` and `issueVc`. The DID is used as the offer issuer, in the profile proof (`<did>#assert`) and to load the issuer DID document; the name goes into the profile and the VC. The whole object is passed to `CertificateVcProvider.getCertificateVcURL`. Must not return `null`.

#### Input Parameters

None.

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssuerInfo` | Issuer information. The SDK reads `did` and `name`. |

#### Function Declaration

```java
SdkIssuerInfo getIssuerInfo();
```

### IssueDataProvider.saveVcProfile

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveVcProfile`

#### Function Description
Saves the VC profile of a transaction. `generateIssueProfile` calls it with a new object. `issueVcFromProxy` calls it with the object returned by `findVcProfileByTransactionId` after replacing `did`, so update the existing record when `id` is set.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcProfile` | `SdkVcProfile` | Profile with `profileId`, `transactionId`, `did`, `nonce`, `userId` and, for ZKP plans, `zkpNonce` | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void saveVcProfile(SdkVcProfile vcProfile);
```

### IssueDataProvider.findVcProfileByTransactionId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findVcProfileByTransactionId`

#### Function Description
Returns the VC profile saved for a transaction. Called by `issueVc` (twice) and `issueVcFromProxy`. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | Transaction key | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVcProfile` | VC profile. The SDK reads `profileId`, `did`, `nonce` and `zkpNonce`; return `id` as well so that the later save updates the record. |

#### Function Declaration

```java
SdkVcProfile findVcProfileByTransactionId(Long transactionId);
```

### IssueDataProvider.saveE2E

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveE2E`

#### Function Description
Saves the E2E session data of a transaction. `generateIssueProfile` calls it with a new object. `sessionKey` is the issuer's ephemeral private key, so store it protected and delete it when the transaction ends.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `e2e` | `SdkE2E` | Session data with `curve`, `cipher`, `padding`, `nonce`, `sessionKey` and `transactionId` | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void saveE2E(SdkE2E e2e);
```

### IssueDataProvider.findE2EByTransactionId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findE2EByTransactionId`

#### Function Description
Returns the E2E session data saved for a transaction. `issueVc` calls it to derive the shared key. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | Transaction key | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkE2E` | Session data. The SDK reads `sessionKey`, `curve`, `cipher` and `padding`. |

#### Function Declaration

```java
SdkE2E findE2EByTransactionId(Long transactionId);
```

### IssueDataProvider.saveVc

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveVc`

#### Function Description
Saves an issued VC record. `issueVc` calls it for the new VC (a new object) and, when a previous VC of the same user and plan exists, for that VC with `status` set to `REVOKED` (the object returned by `findVcByUserIdAndVcPlanId`), unless its metadata status is already `REVOKED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vc` | `SdkVc` | VC record. For a new VC the SDK sets `did`, `userId`, `vcPlanId`, `txId`, `vcId`, `vcSchemaId`, `status`, `vcType` (`VC`), `issuedAt` and `expiredAt`. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void saveVc(SdkVc vc);
```

### IssueDataProvider.findVcByTxId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findVcByTxId`

#### Function Description
Returns the VC issued in a transaction. `completeVc` calls it with the request `txId` and compares `vcId`. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | Transaction ID (`SdkVc.txId`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVc` | VC record. The SDK reads `vcId`. |

#### Function Declaration

```java
SdkVc findVcByTxId(String txId);
```

### IssueDataProvider.findVcByUserIdAndVcPlanId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findVcByUserIdAndVcPlanId`

#### Function Description
Returns a VC previously issued to the user for the plan. `issueVc` calls it before saving the new VC. If a VC is returned, the SDK loads its metadata with `StorageProvider.getVcMetaByVcId`, and, unless the metadata status is already `REVOKED`, revokes it on the ledger and saves it with `status` `REVOKED`. Return `Optional.empty()` when there is none, never `null`. The SDK handles only the one VC returned.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `userId` | `Long` | User key (`UserInfoDto.id`) | M |
| `vcPlanId` | `String` | VC plan ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `Optional<SdkVc>` | Previous VC, or empty. The SDK reads `vcId` and saves the object back, so return `id` too. |

#### Function Declaration

```java
Optional<SdkVc> findVcByUserIdAndVcPlanId(Long userId, String vcPlanId);
```

### IssueDataProvider.findZkpCredentialDefinitionByDefinitionId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findZkpCredentialDefinitionByDefinitionId`

#### Function Description
Returns a ZKP credential definition. `generateIssueProfile` calls it with `SdkIssueProfile.definitionId`, and `issueVc` calls it with the `credDefId` of the holder's ZKP credential request. Only ZKP plans reach this method. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `definitionId` | `String` | Credential definition ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkZkpCredentialDefinition` | Definition. The SDK reads `definitionId`, `schemaId`, `alias` and `definition`. |

#### Function Declaration

```java
SdkZkpCredentialDefinition findZkpCredentialDefinitionByDefinitionId(String definitionId);
```

### IssueDataProvider.findZkpSchemaBySchemaId

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findZkpSchemaBySchemaId`

#### Function Description
Returns a ZKP credential schema. `issueVc` calls it with `SdkZkpCredentialDefinition.schemaId` when it issues a ZKP credential, and uses the schema's attribute names to read the values from `UserInfoDto.data`. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `schemaId` | `String` | ZKP schema ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkZkpSchema` | Schema. The SDK reads `schema`. |

#### Function Declaration

```java
SdkZkpSchema findZkpSchemaBySchemaId(String schemaId);
```

### IssueDataProvider.getVcSchemaById

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getVcSchemaById`

#### Function Description
Returns a VC schema. `generateIssueProfile` uses its `id` as the credential schema ID in the profile, and `issueVc` uses the full schema, including the claim definitions, to build the VC. Both call it with `SdkIssueProfile.vcSchemaId`. Throw an exception if it does not exist, for example `IssuerSdkException(VC_SCHEMA_NOT_FOUND)`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | VC schema key | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `org.omnione.did.data.model.schema.VcSchema` | VC schema from the OpenDID data model |

#### Function Declaration

```java
org.omnione.did.data.model.schema.VcSchema getVcSchemaById(Long id);
```

### VcStatusDataProvider

`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`) stores revocation records and VC status for `VcStatusProtocolImpl`, and supplies the revocation authentication type.

```java
public class InMemoryVcStatusDataProvider implements VcStatusDataProvider {
    private final Map<String, SdkRevokeVc> revokeVcs = new ConcurrentHashMap<>();
    private final Map<String, SdkVc> vcs = new ConcurrentHashMap<>();

    @Override
    public SdkRevokeVc findRevokeVcByVcId(String vcId) {
        SdkRevokeVc revokeVc = revokeVcs.get(vcId);
        return revokeVc != null ? revokeVc : new SdkRevokeVc(); // never null
    }

    @Override
    public SdkVc findVcByVcId(String vcId) {
        return vcs.get(vcId); // null is allowed
    }

    @Override
    public VerifyAuthType getRevokeAuthType() {
        return VerifyAuthType.PIN_OR_BIO;
    }

    // The other methods are omitted.
}
```

### VcStatusDataProvider.findRevokeVcByVcId

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findRevokeVcByVcId`

#### Function Description
Returns the revocation record of a VC. `inspectProposeRevoke` calls it, sets `vcId`, `nonce` and `transactionId` on the returned object and saves it with `saveRevokeVc`. If no record exists, return a new empty `SdkRevokeVc`, not `null`; a `null` return makes the call fail with `TR_VC_REVOKE_PROPOSE_FAILED`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | Existing record (with `id`) or a new empty object |

#### Function Declaration

```java
SdkRevokeVc findRevokeVcByVcId(String vcId);
```

### VcStatusDataProvider.saveRevokeVc

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveRevokeVc`

#### Function Description
Saves a revocation record. `inspectProposeRevoke` calls it after setting `vcId`, `nonce` and `transactionId`; `revokeVc` calls it after setting `status` to `REVOKED`. Insert or update by `id`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `revokeVc` | `SdkRevokeVc` | Revocation record | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | Saved record. The SDK ignores the return value. |

#### Function Declaration

```java
SdkRevokeVc saveRevokeVc(SdkRevokeVc revokeVc);
```

### VcStatusDataProvider.findRevokeVcByTransactionId

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findRevokeVcByTransactionId`

#### Function Description
Returns the revocation record linked to a transaction. `revokeVc` calls it with `SdkTransaction.id` and compares `vcId` and `nonce` with the holder's request. Throw an exception if it does not exist.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | Transaction key | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | Revocation record. The SDK reads `vcId` and `nonce`; return `id` as well so that the later save updates the record. |

#### Function Declaration

```java
SdkRevokeVc findRevokeVcByTransactionId(Long transactionId);
```

### VcStatusDataProvider.validateAndFindTransaction

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`validateAndFindTransaction`

#### Function Description
Finds a revocation transaction by `txId` and checks that it may continue. The SDK does not call this method; the host uses it to find the `SdkTransaction` for `revokeVc` and `completeRevoke`. The expected behavior is the same as `IssueDataProvider.validateAndFindTransaction`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | Transaction ID | M |
| `type` | `SubTransactionType` | Expected type of the latest sub-transaction | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | Transaction. `revokeVc` reads `id`. |

#### Function Declaration

```java
SdkTransaction validateAndFindTransaction(String txId, SubTransactionType type);
```

### VcStatusDataProvider.updateVcStatus

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`updateVcStatus`

#### Function Description
Applies a new VC status. `revokeVc` calls it with `REVOKED`, and `updateVcStatus` calls it with the requested status after its checks. The SDK does not call `StorageProvider.updateVcStatus` in these flows, so if the status must also change in the ledger's VC metadata, do it here or in the host after the call.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |
| `status` | `VcStatus` | New status (`org.omnione.did.data.model.enums.vc.VcStatus`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void updateVcStatus(String vcId, VcStatus status);
```

### VcStatusDataProvider.findVcByVcId

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findVcByVcId`

#### Function Description
Returns the host's VC record. `updateVcStatus` calls it; if a record is returned, the SDK sets its `status` and saves it with `saveVc`. Return `null` when the host has no record; the SDK then skips the save.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVc` | VC record with `id`, or `null` |

#### Function Declaration

```java
SdkVc findVcByVcId(String vcId);
```

### VcStatusDataProvider.saveVc

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveVc`

#### Function Description
Saves a VC record. `updateVcStatus` calls it with the object from `findVcByVcId` after setting `status` to the raw value of the new status (`ACTIVE`, `INACTIVE` or `REVOKED`).

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vc` | `SdkVc` | VC record to update | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void saveVc(SdkVc vc);
```

### VcStatusDataProvider.getRevokeAuthType

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getRevokeAuthType`

#### Function Description
Returns the authentication type the holder must use to sign the revocation request. `inspectProposeRevoke` returns its integer value as `authType`. The SDK does not check it when `revokeVc` verifies the signature. Must not return `null`.

#### Input Parameters

None.

#### Output Parameters

| Type | Description |
| --- | --- |
| `VerifyAuthType` | Required authentication type |

#### Function Declaration

```java
VerifyAuthType getRevokeAuthType();
```

### VcStatusDataProvider.getTransactionExpirationTime

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getTransactionExpirationTime`

#### Function Description
Returns the revocation transaction lifetime. The SDK does not call this method; the host uses it to set `SdkTransaction.expiredAt` when it creates a revocation transaction. The unit is the host's choice; seconds are typical.

#### Input Parameters

None.

#### Output Parameters

| Type | Description |
| --- | --- |
| `long` | Transaction lifetime |

#### Function Declaration

```java
long getTransactionExpirationTime();
```

### WalletProvider

`WalletProvider` (`org.omnione.did.sdk.issuer.provider`) signs with the issuer's keys. `generateCompactSignature` uses the file wallet; the other three methods use the ZKP wallet and are called only for plans with a ZKP credential definition.

```java
public class FileWalletProvider implements WalletProvider {
    private final WalletManagerInterface walletManager;

    public FileWalletProvider(WalletManagerInterface walletManager) {
        this.walletManager = walletManager;
    }

    @Override
    public byte[] generateCompactSignature(String keyId, String source) {
        // SHA-256 of the UTF-8 bytes, then a compact signature
        return BaseWalletUtil.generateCompactSignature(walletManager, keyId, source);
    }

    // The ZKP methods are omitted.
}
```

### WalletProvider.generateCompactSignature

#### Class Name
`WalletProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`generateCompactSignature`

#### Function Description
Signs a string with an issuer key. `generateIssueProfile` calls it with key ID `assert` and the sorted JSON of the profile; `issueVc` calls it with the key ID and signing input that the core SDK prepares for each VC proof (also the `assert` key). Hash the UTF-8 bytes of `source` with SHA-256 and return the compact signature over the hash, in the format that `BaseCryptoUtil.verifySignature` of the OpenDID common SDK verifies. The key must match the `assert` verification method in the issuer DID document. The SDK multibase-encodes the result.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `keyId` | `String` | Key ID in the issuer wallet (`assert`) | M |
| `source` | `String` | Data to sign | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `byte[]` | Compact signature |

#### Function Declaration

```java
byte[] generateCompactSignature(String keyId, String source);
```

### WalletProvider.credSignature

#### Class Name
`WalletProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`credSignature`

#### Function Description
Creates the ZKP credential signature with the private key of a credential definition. `issueVc` calls it when the holder's request contains a ZKP credential request. `alias` is `SdkZkpCredentialDefinition.alias`, and `credentialValues` holds the attribute values read from `UserInfoDto.data`. Must not return `null`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | Key alias of the credential definition in the ZKP wallet | M |
| `credentialRequest` | `CredentialRequest` | Holder's ZKP credential request | M |
| `credentialValues` | `CredentialValues` | Attribute values to sign | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CredentialSignature` | ZKP credential signature (`org.omnione.did.zkp.datamodel.credential`) |

#### Function Declaration

```java
CredentialSignature credSignature(String alias, CredentialRequest credentialRequest, CredentialValues credentialValues);
```

### WalletProvider.signatureCorrectnessProof

#### Class Name
`WalletProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`signatureCorrectnessProof`

#### Function Description
Creates the proof that a ZKP credential signature is correct. `issueVc` calls it right after `credSignature` with the same alias and request. Must not return `null`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | Key alias of the credential definition | M |
| `credentialRequest` | `CredentialRequest` | Holder's ZKP credential request | M |
| `credentialSignature` | `CredentialSignature` | Signature returned by `credSignature` | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SignatureCorrectnessProof` | Signature correctness proof |

#### Function Declaration

```java
SignatureCorrectnessProof signatureCorrectnessProof(String alias, CredentialRequest credentialRequest, CredentialSignature credentialSignature);
```

### WalletProvider.getCorrectnessProof

#### Class Name
`WalletProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getCorrectnessProof`

#### Function Description
Returns the key correctness proof of a credential definition. `generateIssueProfile` calls it with `SdkZkpCredentialDefinition.alias` when the issue profile has a `definitionId`, and puts it in the ZKP credential offer.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | Key alias of the credential definition | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `KeyCorrectnessProof` | Key correctness proof (`org.omnione.did.zkp.datamodel.credentialoffer`) |

#### Function Declaration

```java
KeyCorrectnessProof getCorrectnessProof(String alias);
```

### StorageProvider

`StorageProvider` (`org.omnione.did.sdk.issuer.provider`) reads DID documents and reads and writes VC metadata, usually on the ledger. Both protocol implementations use it.

```java
public class LedgerStorageProvider implements StorageProvider {
    private final Map<String, DidDocument> didDocs = new ConcurrentHashMap<>();
    private final Map<String, VcMeta> vcMetas = new ConcurrentHashMap<>();

    @Override
    public DidDocument findDidDoc(String did) {
        // did may be a DID URL such as did:example:holder?versionId=1#pin
        DidDocument didDocument = didDocs.get(did.split("[?#]")[0]);
        if (didDocument == null) {
            throw new IssuerSdkException(IssuerSdkErrorCode.DID_DOC_NOT_FOUND);
        }
        return didDocument;
    }

    @Override
    public VcMeta getVcMetaByVcId(String vcId) {
        return vcMetas.get(vcId); // null: not registered
    }

    // The other methods are omitted.
}
```

### StorageProvider.findDidDoc

#### Class Name
`StorageProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findDidDoc`

#### Function Description
Returns a DID document. The SDK calls it with the issuer DID (`issueVc`), with `didAuth.did` (`issueVcFromProxy`), and with the `verificationMethod` of a proof (`issueVc` for `accE2e`, `revokeVc`), which is a DID URL with a key fragment and possibly a `versionId` query. Accept both forms. Throw an exception if the document does not exist, for example `IssuerSdkException(DID_DOC_NOT_FOUND)`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `did` | `String` | DID or DID URL | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `DidDocument` | DID document (`org.omnione.did.data.model.did`) |

#### Function Declaration

```java
DidDocument findDidDoc(String did);
```

### StorageProvider.registerVcMeta

#### Class Name
`StorageProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`registerVcMeta`

#### Function Description
Registers the metadata of a newly issued VC. `issueVc` calls it once per VC, after signing and before encrypting the response. An exception makes `issueVc` fail.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcMeta` | `VcMeta` | VC metadata (`org.omnione.did.data.model.vc`) generated by the core SDK | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void registerVcMeta(VcMeta vcMeta);
```

### StorageProvider.getVcMetaByVcId

#### Class Name
`StorageProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getVcMetaByVcId`

#### Function Description
Returns the metadata of a VC. `inspectProposeRevoke` and `updateVcStatus` call it and report `VC_NOT_FOUND` when it returns `null`. `issueVc` calls it for a previous VC returned by `findVcByUserIdAndVcPlanId` and does not accept `null` there.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `VcMeta` | VC metadata, or `null`. The SDK reads `status`. |

#### Function Declaration

```java
VcMeta getVcMetaByVcId(String vcId);
```

### StorageProvider.updateVcStatus

#### Class Name
`StorageProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`updateVcStatus`

#### Function Description
Changes the status in the VC metadata. `issueVc` calls it with `REVOKED` for a previous VC of the same user and plan whose metadata status is not already `REVOKED`. The revocation and status flows of `VcStatusProtocol` do not call it.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |
| `status` | `VcStatus` | New status (`org.omnione.did.data.model.enums.vc.VcStatus`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | None |

#### Function Declaration

```java
void updateVcStatus(String vcId, VcStatus status);
```

### CertificateVcProvider.getCertificateVcURL

#### Class Name
`CertificateVcProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`getCertificateVcURL`

#### Function Description
Returns the URL of the issuer's certificate VC. `generateIssueProfile` and `issueVc` put it in the issuer's `certVcRef`, and `issueVc` also passes it to the VC metadata. The SDK uses the value as returned. The interface has one method, so a lambda can implement it. Register the provider with `IssueVcProtocolImpl#setCertificatePublisher`.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `issuerInfo` | `SdkIssuerInfo` | Result of `IssueDataProvider.getIssuerInfo` | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `String` | Certificate VC URL |

#### Function Declaration

```java
String getCertificateVcURL(SdkIssuerInfo issuerInfo);
```

#### Function Usage

```java
CertificateVcProvider certificateVcProvider = issuerInfo ->
        issuerInfo.getPublishedCertificateUrl() != null
                ? issuerInfo.getPublishedCertificateUrl()
                : issuerInfo.getCertificateUrl();
```

## 4. Data models

Data types used in the public signatures. The `Sdk*` classes and `UserInfoDto` have a no-argument constructor, an all-arguments constructor, getters, setters and a static `builder()`. In the tables, **M** means the SDK reads the field, so the provider must fill it when it returns the object, or the host must set it when it passes the object to a protocol function. **O** means the SDK does not read it. Fields that the SDK sets before it calls a `save*` method are noted in the descriptions.

### SdkTransaction

Package `org.omnione.did.sdk.issuer.model.domain`. Transaction created and stored by the host and passed to the protocol functions. Also returned by `validateAndFindTransaction` and `findTransactionById`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key. The SDK uses it to find the VC profile, E2E data, revocation record and sub-transactions, and stores it as `transactionId`. | M |
| `txId` | `String` | Transaction ID shared with the wallet. Stored as `SdkVc.txId`; returned by `inspectProposeRevoke` and `issueVcResult`. | M |
| `refId` | `String` | Reference ID from `inspectIssuePropose`. `issueVc` compares it with the decrypted request. Issuance only. | M |
| `type` | `TransactionType` | `ISSUE_VC` or `REVOKE_VC` | O |
| `status` | `TransactionStatus` | `COMPLETED`, `FAILED`, `CANCELED`, `PENDING` or `FINISH` | O |
| `vcPlanId` | `String` | VC plan ID. Used by `generateIssueProfile` and `issueVc`. Issuance only. | M |
| `expiredAt` | `Instant` | Expiry time, checked by the host | O |
| `issueProfileId` | `Long` | Key of the issue profile; `issueVc` passes it to `findIssueProfileById`. Issuance only. | M |

### SdkUser

Package `org.omnione.did.sdk.issuer.model.domain`. User passed to `generateIssueProfile`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | User key, stored as `SdkVcProfile.userId` | M |
| `pii` | `String` | Personal identifier used by the host to find the user | O |
| `did` | `String` | Holder DID, stored as `SdkVcProfile.did` and used as the VC subject. May be `null` in the proxy flow. | M |
| `vcSchemaId` | `Long` | VC schema key | O |
| `data` | `String` | User data | O |

### UserInfoDto

Package `org.omnione.did.sdk.issuer.model.dto`. User data passed to `issueVc` and `issueVcFromProxy`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | User key. Used by `findVcByUserIdAndVcPlanId` and stored as `SdkVc.userId`. | M |
| `pii` | `String` | Personal identifier | O |
| `did` | `String` | Holder DID. Not read; the VC subject comes from `SdkVcProfile.did`. | O |
| `vcSchemaId` | `Long` | VC schema key | O |
| `data` | `String` | JSON object with the claim values. Keys are claim codes `<namespace id>.<claim id>` from the VC schema and, for ZKP, the attribute names of the ZKP schema. Values are read as strings; missing keys are skipped. | M |
| `validFrom` | `ZonedDateTime` | Start of VC validity. If `null`, the issuance time is used. | O |
| `validUntil` | `ZonedDateTime` | End of VC validity. If `null`, `9999-12-31T23:59:59Z` is used. | O |

### SdkVcOffer

Package `org.omnione.did.sdk.issuer.model.domain`. Issuance offer created by `requestOffer`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `offerId` | `String` | Offer ID (UUID), set by the SDK | O |
| `offerType` | `OfferType` | `ISSUE_OFFER` (`IssueOffer`) or `VERIFY_OFFER` (`VerifyOffer`). The SDK sets `ISSUE_OFFER`. | O |
| `vcPlanId` | `String` | VC plan ID, set by the SDK | O |
| `did` | `String` | Issuer DID, set by the SDK | O |
| `validUntil` | `Instant` | Expiry time. The SDK sets 24 hours after creation; `inspectIssuePropose` throws `VC_OFFER_EXPIRED` after it. | M |
| `transactionId` | `Long` | Key of the linked transaction, set by the host. `issueVcResult` passes it to `findTransactionById`. | M |

### SdkIssueProfile

Package `org.omnione.did.sdk.issuer.model.domain`. Issue profile of a VC plan, returned by `findIssueProfileByVcPlanId` and `findIssueProfileById`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key; the host stores it as `SdkTransaction.issueProfileId` | O |
| `vcSchemaId` | `Long` | VC schema key passed to `getVcSchemaById` | M |
| `vcPlanId` | `String` | VC plan ID | O |
| `title` | `String` | Profile title | M |
| `description` | `String` | Profile description | M |
| `endpoints` | `List<String>` | Issuer endpoints written to the profile process | M |
| `cipher` | `String` | E2E cipher: `AES-128-CBC`, `AES-128-ECB`, `AES-256-CBC` or `AES-256-ECB` | M |
| `curve` | `String` | E2E key curve: `Secp256k1` or `Secp256r1` | M |
| `padding` | `String` | E2E padding: `NOPAD` or `PKCS5` | M |
| `language` | `String` | Profile language | M |
| `issuanceMode` | `VcIssuanceMode` | `DIRECT` or `PROXY` | O |
| `initiateType` | `InitiateType` | `USER_INIT` (`user_init`) or `ISSUER_INIT` (`issuer_init`) | O |
| `tags` | `List<String>` | Tags | O |
| `zkpEnabled` | `Boolean` | ZKP flag. Not read; the SDK uses `definitionId`. | O |
| `definitionId` | `String` | ZKP credential definition ID. If set, `generateIssueProfile` adds a ZKP credential offer. | O |
| `userQueryType` | `UserQueryType` | `API`, `DB` or `TEST`; for the host's user lookup | O |
| `userQueryUrl` | `String` | User lookup URL | O |
| `userQueryHeaders` | `List<HeaderEntry>` | User lookup headers; `HeaderEntry` has `key` and `value` | O |

### SdkIssuerInfo

Package `org.omnione.did.sdk.issuer.model.domain`. Issuer information returned by `getIssuerInfo` and passed to `CertificateVcProvider`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `did` | `String` | Issuer DID | M |
| `name` | `String` | Issuer name shown in the profile and the VC | M |
| `status` | `IssuerStatus` | `ACTIVATE`, `DEACTIVATE`, `DID_DOCUMENT_REQUIRED`, `DID_DOCUMENT_REQUESTED` or `CERTIFICATE_VC_REQUIRED` | O |
| `serverUrl` | `String` | Issuer server URL | O |
| `certificateUrl` | `String` | Certificate VC URL. For use by `CertificateVcProvider`. | O |
| `publishedCertificateUrl` | `String` | Published certificate VC URL. For use by `CertificateVcProvider`. | O |

### SdkVcProfile

Package `org.omnione.did.sdk.issuer.model.domain`. Per-transaction issuance state saved by `generateIssueProfile`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key; needed to update the record in `issueVcFromProxy` | O |
| `profileId` | `String` | ID of the issued profile. `issueVc` compares it with the request (`VC_PROFILE_INVALID`). | M |
| `did` | `String` | Holder DID, used as the VC subject | M |
| `nonce` | `String` | Issuer nonce (multibase). Used for the E2E key and compared with the request nonce and `didAuth.authNonce`. | M |
| `zkpNonce` | `String` | Nonce of the ZKP credential offer as a decimal number. Set only for ZKP plans and required for ZKP issuance. | O |
| `userId` | `Long` | User key, set by the SDK | O |
| `transactionId` | `Long` | Transaction key, set by the SDK | O |

### SdkE2E

Package `org.omnione.did.sdk.issuer.model.domain`. E2E session data saved by `generateIssueProfile`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `sessionKey` | `String` | Issuer's ephemeral private key (encoded, multibase). Secret. | M |
| `nonce` | `String` | E2E nonce, same value as `SdkVcProfile.nonce` | O |
| `curve` | `String` | Key curve | M |
| `cipher` | `String` | Cipher | M |
| `padding` | `String` | Padding | M |
| `transactionId` | `Long` | Transaction key, set by the SDK | O |

### SdkVc

Package `org.omnione.did.sdk.issuer.model.domain`. Issued VC record saved by `issueVc` and used by `completeVc` and `updateVcStatus`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key; needed when the SDK saves a returned record back | O |
| `did` | `String` | Holder DID | O |
| `vcId` | `String` | VC ID | M |
| `txId` | `String` | Transaction ID of the issuance; lookup key of `findVcByTxId` | O |
| `issuedAt` | `Instant` | Issuance time | O |
| `expiredAt` | `Instant` | End of validity | O |
| `vcPlanId` | `String` | VC plan ID | O |
| `userId` | `Long` | User key | O |
| `vcSchemaId` | `String` | VC schema ID (URL) from the VC | O |
| `status` | `String` | Raw `VcStatus` value: `ACTIVE`, `INACTIVE` or `REVOKED` | O |
| `vcType` | `String` | `VC` for VCs issued by the SDK | O |

### SdkRevokeVc

Package `org.omnione.did.sdk.issuer.model.domain`. Revocation record used by `inspectProposeRevoke` and `revokeVc`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `nonce` | `String` | Issuer nonce (multibase) set by `inspectProposeRevoke`. `revokeVc` compares the decoded bytes with the request. | M |
| `vcId` | `String` | VC ID. `revokeVc` compares it with the request. | M |
| `status` | `VcStatus` | `org.omnione.did.data.model.enums.vc.VcStatus`; `revokeVc` sets `REVOKED` | O |
| `transactionId` | `Long` | Transaction key set by `inspectProposeRevoke`; lookup key of `findRevokeVcByTransactionId` | O |

### SdkSubTransaction

Package `org.omnione.did.sdk.issuer.model.domain`. Step record that the host saves after each protocol call.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `step` | `Integer` | Step number; the latest record has the highest value | O |
| `type` | `SubTransactionType` | Step type. `issueVcResult` reads it. | M |
| `status` | `SubTransactionStatus` | `COMPLETED`, `FAILED` or `CANCELED` | O |
| `transactionId` | `Long` | Transaction key | O |

### SdkZkpCredentialDefinition

Package `org.omnione.did.sdk.issuer.model.domain`. ZKP credential definition returned by `findZkpCredentialDefinitionByDefinitionId`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `definitionId` | `String` | Credential definition ID, written to the credential offer | M |
| `schemaId` | `String` | ZKP schema ID, written to the credential offer and passed to `findZkpSchemaBySchemaId` | M |
| `type` | `CredentialType` | Credential type from the ZKP SDK | O |
| `alias` | `String` | Key alias of the definition in the ZKP wallet, passed to `WalletProvider` | M |
| `tag` | `String` | Tag | O |
| `version` | `String` | Version | O |
| `definition` | `String` | JSON of the ZKP `CredentialDefinition`, used by `issueVc` | M |
| `zkpSchemaId` | `Long` | Host key of the ZKP schema | O |
| `status` | `ZkpCredentialDefinitionStatus` | `ACTIVATE`, `NEED_BLOCKCHAIN_REGISTRATION` or `NEED_LIST_PROVIDER_REGISTRATION` | O |

### SdkZkpSchema

Package `org.omnione.did.sdk.issuer.model.domain`. ZKP credential schema returned by `findZkpSchemaBySchemaId`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | Host key | O |
| `schemaId` | `String` | ZKP schema ID | O |
| `name` | `String` | Schema name | O |
| `version` | `String` | Version | O |
| `tag` | `String` | Tag | O |
| `status` | `ZkpSchemaStatus` | `ACTIVATE`, `NEED_BLOCKCHAIN_REGISTRATION` or `NEED_LIST_PROVIDER_REGISTRATION` | O |
| `schema` | `String` | JSON of the ZKP `CredentialSchema`; `issueVc` reads its attribute names | M |

### RequestProof

Package `org.omnione.did.sdk.issuer.model.data`. Abstract base class of the signed request objects `AccE2e` (`IssueVcReqDto.accE2e`), `DidAuth` (`IssueVcReqDto.didAuth`) and `ReqRevokeVc` (`RevokeVcReqDto.request`). The signed data is the SHA-256 hash of the sorted JSON of the object with `proofValue` removed from `proof`.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `proof` | `Proof` | `org.omnione.did.data.model.did.Proof` with `type`, `created`, `verificationMethod` (DID URL of the signing key), `proofPurpose` and `proofValue` (multibase compact signature). Required in `ReqRevokeVc`. In `AccE2e` and `DidAuth`, the signature check is skipped when it is `null`. | O |

### SubTransactionType

Package `org.omnione.did.sdk.issuer.model.common.enums`. Step types of `SdkSubTransaction`. The host records them; the SDK reads only `COMPLETE_VC`.

| Value | Description |
| --- | --- |
| `OFFER_ISSUE_VC` | Offer step |
| `INSPECT_ISSUE_PROPOSE` | After `inspectIssuePropose` |
| `GENERATE_ISSUE_PROFILE` | After `generateIssueProfile` |
| `ISSUE_VC` | After `issueVc` or `issueVcFromProxy` |
| `COMPLETE_VC` | After `completeVc`. `issueVcResult` returns `true` only when the latest sub-transaction has this type. |
| `INSPECT_REVOKE_PROPOSE` | After `inspectProposeRevoke` |
| `REVOKE_VC` | After `revokeVc` |
| `COMPLETE_REVOKE` | After `completeRevoke` |

### VcStatus

Package `org.omnione.did.data.model.enums.vc` (OpenDID data model SDK). The type used by `UpdateVcStatusReqDto`, `SdkRevokeVc`, `VcStatusDataProvider.updateVcStatus` and `StorageProvider.updateVcStatus`. `getRawValue()` returns the string value, and `fromString(String)` returns the constant for a value, ignoring case, or `null`. The SDK also contains a separate `org.omnione.did.sdk.issuer.model.enums.VcStatus` with the same constants, which no protocol or provider method uses.

| Value | Description |
| --- | --- |
| `ACTIVE` | Raw value `ACTIVE`. Valid VC. |
| `INACTIVE` | Raw value `INACTIVE`. Inactive VC. |
| `REVOKED` | Raw value `REVOKED`. Revoked VC; its status can no longer be changed through `VcStatusProtocol`. |

### VerifyAuthType

Package `org.omnione.did.sdk.issuer.model.enums`. Authentication type returned by `getRevokeAuthType`. `getValue()` and `toJson()` return the integer value; `fromJson(String)` accepts a constant name and throws `IllegalArgumentException` for any other value.

| Value | Description |
| --- | --- |
| `NO_RESTRICTIONS_AUTHENTICATION` | `0x00000000`, no restriction |
| `NO_AUTHENTICATION` | `0x00000001`, no authentication |
| `PIN` | `0x00000002`, PIN |
| `BIO` | `0x00000004`, biometrics |
| `PIN_OR_BIO` | `0x00000006`, PIN or biometrics |
| `PIN_AND_BIO` | `0x00008006`, PIN and biometrics |

## 5. Auxiliary Utilities

These are auxiliary classes that help implement the SDK. If a main API does the same job, using the main API is recommended.

### ValidationUtil

Package `org.omnione.did.sdk.issuer.util`. Static checks used by the protocol implementations. The protocol functions already verify the request signatures, so the host does not need to call these for the SDK flows.

| Method | Description |
| --- | --- |
| `public static boolean isExpiredDate(Instant expiredAt)` | Returns `true` if the current time is after `expiredAt`. Throws `NullPointerException` if `expiredAt` is `null`. |
| `public static void verifySign(RequestProof request, DidDocument didDocument)` | Verifies `request.proof` with the public key in `didDocument` whose ID is the fragment of `proof.verificationMethod`. The signed data is the SHA-256 hash of the sorted JSON of `request` without `proofValue`, and the key is always treated as Secp256r1. Returns normally if the signature is valid; throws `CommonSdkException` (`SIGNATURE_VERIFICATION_FAILED`) if not, and `NullPointerException` if the proof or the key is missing. The method replaces `request.proof` with a copy that has no `proofValue`. |
