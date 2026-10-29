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
- Date: 2026-10-29
- Version: v1.0.0

| Version | Date | Changes |
| --- | --- | --- |
| v1.0.0 | 2026-10-29 | API reference for extracted SDK |

<div style="page-break-after: always;"></div>

# Table of Contents
- [1. VC issuance](#1-vc-issuance)
- [2. VC status and revocation](#2-vc-status-and-revocation)
- [3. Provider integration](#3-provider-integration)

# Feature List
## 1. VC issuance

### requestOffer

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`requestOffer`

#### Function Description
Creates a VC issuance offer.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `OfferIssueVcReqDto` | Request DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `OfferIssueVcResDto` | Creates a VC issuance offer. |

#### Function Declaration

```java
OfferIssueVcResDto requestOffer(OfferIssueVcReqDto request);
```

### inspectIssuePropose

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectIssuePropose`

#### Function Description
Validates the issuance proposal.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectIssueProposeReqDto` | Request DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectIssueProposeResDto` | Validates the issuance proposal. |

#### Function Declaration

```java
InspectIssueProposeResDto inspectIssuePropose(InspectIssueProposeReqDto request);
```

### generateIssueProfile

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`generateIssueProfile`

#### Function Description
Generates the issue profile for a transaction and user.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `GenerateIssueProfileReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |
| `user` | `SdkUser` | User information | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `GenerateIssueProfileResDto` | Generates the issue profile for a transaction and user. |

#### Function Declaration

```java
GenerateIssueProfileResDto generateIssueProfile(GenerateIssueProfileReqDto request, SdkTransaction transaction, SdkUser user);
```

### issueVc

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVc`

#### Function Description
Issues a VC.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |
| `user` | `UserInfoDto` | User information | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | Issues a VC. |

#### Function Declaration

```java
IssueVcResDto issueVc(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user);
```

### issueVcFromProxy

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVcFromProxy`

#### Function Description
Issues a VC through the proxy flow.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |
| `user` | `UserInfoDto` | User information | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | Issues a VC through the proxy flow. |

#### Function Declaration

```java
IssueVcResDto issueVcFromProxy(IssueVcReqDto request, SdkTransaction transaction, UserInfoDto user);
```

### completeVc

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`completeVc`

#### Function Description
Completes a VC issuance transaction.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteVcReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteVcResDto` | Completes a VC issuance transaction. |

#### Function Declaration

```java
CompleteVcResDto completeVc(CompleteVcReqDto request, SdkTransaction transaction);
```

### issueVcResult

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`issueVcResult`

#### Function Description
Returns the issuance result for an offer ID.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | Issuance offer ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResultResDto` | Returns the issuance result for an offer ID. |

#### Function Declaration

```java
IssueVcResultResDto issueVcResult(String offerId);
```

## 2. VC status and revocation

### inspectProposeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectProposeRevoke`

#### Function Description
Validates a revocation proposal.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectProposeRevokeReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectProposeRevokeResDto` | Validates a revocation proposal. |

#### Function Declaration

```java
InspectProposeRevokeResDto inspectProposeRevoke(InspectProposeRevokeReqDto request, SdkTransaction transaction);
```

### revokeVc

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`revokeVc`

#### Function Description
Revokes a VC.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `RevokeVcReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `RevokeVcResDto` | Revokes a VC. |

#### Function Declaration

```java
RevokeVcResDto revokeVc(RevokeVcReqDto request, SdkTransaction transaction);
```

### updateVcStatus

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`updateVcStatus`

#### Function Description
Updates VC status.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `UpdateVcStatusReqDto` | Request DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `UpdateVcStatusResDto` | Updates VC status. |

#### Function Declaration

```java
UpdateVcStatusResDto updateVcStatus(UpdateVcStatusReqDto request);
```

### completeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`completeRevoke`

#### Function Description
Completes a VC revocation transaction.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteRevokeReqDto` | Request DTO | M |
| `transaction` | `SdkTransaction` | SDK transaction | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteRevokeResDto` | Completes a VC revocation transaction. |

#### Function Declaration

```java
CompleteRevokeResDto completeRevoke(CompleteRevokeReqDto request, SdkTransaction transaction);
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
IssueVcProtocol issue = new IssueVcProtocolImpl(issueDataProvider, walletProvider, storageProvider);
VcStatusProtocol status = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

For issuance flows requiring `CertificateVcProvider`, set it with `IssueVcProtocolImpl#setCertificatePublisher`. See `source/did-issuer-sdk/src/main/java/org/omnione/did/sdk/issuer/model/` for DTO field definitions.
