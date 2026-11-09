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

- 주제: Issuer SDK API
- 일자: 2026-11-09
- 버전: v3.0.0

| 버전 | 일자 | 변경 내용 |
| --- | --- | --- |
| v3.0.0 | 2026-11-09 | 공개 API 전체 문서화, 시그니처 정정 |
| v1.0.0 | 2026-10-29 | SDK 분리 후 API 문서 구성 |

<div style="page-break-after: always;"></div>

# 목차
- [1. VC 발급](#1-vc-발급)
- [2. VC 상태 및 폐기](#2-vc-상태-및-폐기)
- [3. Provider 연동](#3-provider-연동)
- [4. 데이터 모델](#4-데이터-모델)
- [5. 보조 유틸리티](#5-보조-유틸리티)

# 기능 목록
## 1. VC 발급

### requestOffer

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`requestOffer`

#### Function Description
VC 발급 오퍼를 생성합니다. SDK는 `IssueDataProvider.findIssueProfileByVcPlanId`를 호출해 VC 플랜을 확인합니다. 반환값은 검사하지 않으므로 없는 플랜이면 Provider가 예외를 던져야 합니다. 이어서 UUID 오퍼 ID를 만들고 `IssueDataProvider.saveVcOffer`로 `SdkVcOffer`를 저장합니다. 저장되는 오퍼의 만료 시각은 생성 후 24시간이고, 응답 페이로드의 `validUntil`은 생성 후 3분입니다. Provider가 던진 `IssuerSdkException`은 그대로 전달되고, 그 밖의 예외는 `TR_VC_OFFER_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `OfferIssueVcReqDto` | 요청 DTO. SDK는 `vcPlanId`를 읽습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `OfferIssueVcResDto` | `issueOfferPayload`에 `offerId`, `type`(`IssueOffer`), `vcPlanId`, `issuer`(`getIssuerInfo`의 Issuer DID), `validUntil`(UTC 문자열)이 들어 있습니다. `txId`는 채우지 않습니다. |

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
발급 제안을 검증하고 트랜잭션 식별자를 만듭니다. VC 플랜은 `requestOffer`와 같은 방식으로 확인합니다. `offerId`가 비어 있지 않으면 `IssueDataProvider.findVcOfferByOfferId`로 오퍼를 읽고, `SdkVcOffer.validUntil`이 지났으면 `VC_OFFER_EXPIRED`를 던집니다. 이어서 UUID `txId`와 17자리 `refId`를 만듭니다. SDK는 아무것도 저장하지 않습니다. 호스트가 이 값과 `vcPlanId`, 발급 프로파일 ID로 `SdkTransaction`을 만들고, 오퍼를 사용했다면 `SdkVcOffer.transactionId`를 채우고, `INSPECT_ISSUE_PROPOSE` 하위 트랜잭션을 기록해야 합니다. 예상하지 못한 예외는 `UNKNOWN_SERVER_ERROR`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectIssueProposeReqDto` | 요청 DTO. SDK는 `vcPlanId`(필수)와 `offerId`(선택)를 읽습니다. `id`와 `issuer`는 읽지 않습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectIssueProposeResDto` | `txId`(새 UUID)와 `refId`(새 참조 ID) |

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
// 호스트는 proposal.getTxId()와 proposal.getRefId()로 트랜잭션을 저장합니다.
```

### generateIssueProfile

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`generateIssueProfile`

#### Function Description
트랜잭션과 사용자에 대한 발급 프로파일을 만들고 서명합니다. SDK는 `findIssueProfileByVcPlanId(transaction.vcPlanId)`로 발급 프로파일을, `getVcSchemaById`로 VC 스키마를, `getIssuerInfo`와 `CertificateVcProvider.getCertificateVcURL`로 Issuer 정보를 읽습니다. `SdkIssueProfile.definitionId`가 있으면 `findZkpCredentialDefinitionByDefinitionId`와 `WalletProvider.getCorrectnessProof`로 ZKP 크리덴셜 오퍼도 추가합니다. 프로파일 커브로 임시 E2E 키 쌍과 16바이트 Issuer nonce를 만든 뒤 `WalletProvider.generateCompactSignature("assert", ...)`로 프로파일에 서명합니다(검증 방법 `<Issuer DID>#assert`). 트랜잭션의 `SdkVcProfile`과 `SdkE2E`를 저장합니다. 먼저 `setCertificatePublisher`를 호출해 두어야 합니다. Provider가 `IssuerSdkException`을 던진 경우가 아니면 실패는 `TR_VC_ISSUE_PROFILE_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `GenerateIssueProfileReqDto` | 요청 DTO. SDK는 `txId`만 읽어 그대로 돌려줍니다. `holder`와 `userId`는 호스트의 사용자 조회용입니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 찾은 트랜잭션. SDK는 `id`와 `vcPlanId`를 읽습니다. | M |
| `user` | `SdkUser` | 호스트가 찾은 사용자. SDK는 `id`와 `did`를 읽어 VC 프로파일에 저장합니다. 프록시 흐름에서는 `did`가 `null`일 수 있습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `GenerateIssueProfileResDto` | 요청의 `txId`, 서명된 `profile`(`ZkpIssueProfile`), `authNonce`(Issuer nonce, multibase) |

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
VC를 발급합니다. 단계는 다음과 같습니다.
1. `accE2e.proof`가 있으면 `StorageProvider.findDidDoc(proof.verificationMethod)`로 서명자의 DID 문서를 읽어 서명을 검증합니다. proof가 없으면 검증을 건너뜁니다.
2. 트랜잭션의 `SdkE2E`와 `SdkVcProfile`을 읽고, `accE2e.publicKey`, 저장된 세션 키, 프로파일 nonce로 공유 키를 만든 뒤 `accE2e.iv`로 `encReqVc`를 복호화합니다.
3. 복호화한 요청과 `transaction.refId`(`REF_ID_INVALID`), 프로파일 ID(`VC_PROFILE_INVALID`), Issuer nonce(`VC_PROFILE_ISSUER_NONCE_INVALID`)를 비교합니다.
4. VC 스키마(`findIssueProfileById`, `getVcSchemaById`)와 `user.data`로 VC를 만들고 Issuer `assert` 키로 서명한 뒤 `StorageProvider.registerVcMeta`로 `VcMeta`를 등록합니다.
5. 요청에 ZKP 크리덴셜 요청이 있으면 `findZkpCredentialDefinitionByDefinitionId`, `findZkpSchemaBySchemaId`, `WalletProvider.credSignature`, `WalletProvider.signatureCorrectnessProof`로 ZKP 크리덴셜을 발급합니다.
6. `findVcByUserIdAndVcPlanId`가 이전 VC를 돌려주고 그 메타데이터 상태가 `REVOKED`가 아니면 원장에서 폐기(`StorageProvider.updateVcStatus`)하고 상태를 `REVOKED`로 저장합니다. 그다음 새 `SdkVc`를 저장합니다.

먼저 `setCertificatePublisher`를 호출해 두어야 합니다. Provider가 `IssuerSdkException`을 던진 경우가 아니면 실패는 `TR_VC_ISSUE_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | 요청 DTO. SDK는 `txId`, `accE2e`(`publicKey`, `iv`, 선택 `proof`), `encReqVc`(multibase)를 읽습니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 찾은 트랜잭션. SDK는 `id`, `txId`, `refId`, `vcPlanId`, `issueProfileId`를 읽습니다. | M |
| `user` | `UserInfoDto` | 사용자 데이터. SDK는 `id`, `data`(클레임 값), `validFrom`, `validUntil`을 읽습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | 요청의 `txId`와 `e2e`: `encVc`(multibase, VC와 선택적 ZKP 크리덴셜을 담은 JSON의 암호문), `iv`(multibase) |

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
Holder DID를 발급 시점에야 알 수 있는 프록시 흐름에서 VC를 발급합니다. `didAuth.proof`가 있으면 SDK는 `didAuth.did`의 DID 문서를 읽어 서명을 검증합니다. VC 프로파일을 읽어 `didAuth.authNonce`가 `SdkVcProfile.nonce`와 다르면 `ISSUER_NONCE_INVALID`를 던지고, `SdkVcProfile.did`를 `didAuth.did`로 바꿔 `saveVcProfile`로 저장한 뒤 `issueVc`를 실행합니다. `issueVc`에 들어가기 전에 난 예외(서명 실패, `didAuth` 누락 등)는 `IssuerSdkException`으로 바뀌지 않습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | `issueVc`의 필드와 `didAuth`(`did`, `authNonce`, 선택 `proof`)를 담은 요청 DTO | M |
| `transaction` | `SdkTransaction` | `issueVc`와 같습니다. | M |
| `user` | `UserInfoDto` | `issueVc`와 같습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | `issueVc`와 같습니다. |

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
Holder가 발급된 VC를 받았는지 확인합니다. SDK는 `IssueDataProvider.findVcByTxId(request.txId)`로 VC를 읽고 그 `vcId`가 요청과 다르면 `VC_ID_NOT_MATCH`를 던집니다. 상태는 바꾸지 않으며 `transaction`도 사용하지 않습니다. 트랜잭션 종료 처리와 `COMPLETE_VC` 하위 트랜잭션 기록은 호스트가 하며, `issueVcResult`가 이 기록을 사용합니다. 그 밖의 실패는 `TR_VC_ISSUE_COMPLETE_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteVcReqDto` | 요청 DTO. SDK는 `txId`와 `vcId`를 읽습니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 찾은 트랜잭션. 현재 구현은 사용하지 않습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteVcResDto` | 요청의 `txId` |

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
오퍼 ID로 발급 결과를 조회합니다. SDK는 `findVcOfferByOfferId`로 오퍼를, `findTransactionById(SdkVcOffer.transactionId)`로 트랜잭션을 읽습니다. 트랜잭션이 없으면 결과는 `false`입니다. 있으면 `findSubTransactionByTransactionIdOrderByStepDesc`로 마지막 하위 트랜잭션을 읽어 그 유형이 `COMPLETE_VC`일 때만 `true`를 돌려줍니다. 실패는 `TR_VC_ISSUE_RESULT_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | 발급 오퍼 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResultResDto` | `offerId`, `result`, `txId`(연결된 트랜잭션이 없으면 `null`) |

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
`IssueVcProtocolImpl` (생성자)

#### Function Description
`IssueVcProtocolImpl`은 `IssueVcProtocol`을 구현합니다. 이 장의 앞 함수들을 참조하세요. 생성자는 Provider를 `null` 검사 없이 저장합니다. 인스턴스를 쓰기 전에 `setCertificatePublisher`를 호출하세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `issueDataProvider` | `IssueDataProvider` | 발급 데이터 Provider | M |
| `walletProvider` | `WalletProvider` | 서명 및 ZKP 지갑 Provider | M |
| `storageProvider` | `StorageProvider` | DID 문서와 VC 메타데이터 Provider | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcProtocolImpl` | 새 프로토콜 인스턴스 |

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
`CertificateVcProvider`를 설정합니다. 기본값은 없습니다. `generateIssueProfile`, `issueVc`, `issueVcFromProxy`가 Issuer 인증서 VC 참조를 만들 때 사용하므로, 설정하지 않으면 이 함수들은 `TR_VC_ISSUE_PROFILE_FAILED` 또는 `TR_VC_ISSUE_FAILED`로 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `certificateVcProvider` | `CertificateVcProvider` | 인증서 VC URL Provider | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

#### Function Declaration

```java
public void setCertificatePublisher(CertificateVcProvider certificateVcProvider)
```

#### Function Usage

```java
issueVcProtocol.setCertificatePublisher(issuerInfo -> issuerInfo.getCertificateUrl());
```

## 2. VC 상태 및 폐기

### inspectProposeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectProposeRevoke`

#### Function Description
폐기 제안을 검증합니다. SDK는 `StorageProvider.getVcMetaByVcId`로 VC 메타데이터를 읽어 `null`이면 `VC_NOT_FOUND`, 이미 폐기됐으면 `REVOKED_VC`를 던집니다. 16바이트 Issuer nonce를 만들고 `VcStatusDataProvider.getRevokeAuthType`으로 필요한 인증 유형을 읽은 뒤 폐기 기록을 저장합니다. `findRevokeVcByVcId`가 돌려준 객체에 `vcId`, `nonce`, `transactionId`를 채워 `saveRevokeVc`를 호출합니다. 호스트는 호출 전에 폐기 트랜잭션을 만들고 호출 후에 `INSPECT_REVOKE_PROPOSE` 하위 트랜잭션을 기록해야 합니다. 그 밖의 실패는 `TR_VC_REVOKE_PROPOSE_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectProposeRevokeReqDto` | 요청 DTO. SDK는 `vcId`를 읽습니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 만든 폐기 트랜잭션. SDK는 `id`와 `txId`를 읽습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectProposeRevokeResDto` | 트랜잭션의 `txId`, `issuerNonce`(multibase), `authType`(`VerifyAuthType`의 정수 값) |

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
VC를 폐기합니다. SDK는 `findRevokeVcByTransactionId(transaction.id)`로 폐기 기록을 읽고, `request.request.vcId`가 다르면 `VC_ID_NOT_MATCH`, 디코딩한 Issuer nonce가 다르면 `ISSUER_NONCE_INVALID`를 던집니다. 이어서 `StorageProvider.findDidDoc(proof.verificationMethod)`로 DID 문서를 읽어 `request.request`의 서명을 검증합니다. 마지막으로 `VcStatusDataProvider.updateVcStatus(vcId, REVOKED)`를 호출하고 기록의 상태를 `REVOKED`로 바꿔 저장합니다. 이 흐름에서 SDK는 `StorageProvider.updateVcStatus`를 호출하지 않습니다. 서명 실패와 그 밖의 오류는 `TR_VC_REVOKE_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `RevokeVcReqDto` | 요청 DTO. SDK는 `txId`와 `request`(`ReqRevokeVc`: `vcId`, `issuerNonce`, `proof`)를 읽습니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 찾은 폐기 트랜잭션. SDK는 `id`를 읽습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `RevokeVcResDto` | 요청의 `txId` |

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
Holder 요청 없이 VC 상태를 바꿉니다. SDK는 `StorageProvider.getVcMetaByVcId`로 VC 메타데이터를 읽어 `null`이면 `VC_NOT_FOUND`, 폐기된 VC면 `REVOKED_VC`를 던집니다. `VcStatusDataProvider.findVcByVcId`가 VC를 돌려주면 그 `status`를 새 상태의 원시 값으로 바꿔 `saveVc`로 저장합니다. 이어서 `VcStatusDataProvider.updateVcStatus(vcId, vcStatus)`를 호출합니다. SDK는 `StorageProvider.updateVcStatus`를 호출하지 않고 호출자도 확인하지 않으므로, 호출 권한은 호스트가 확인해야 합니다. 그 밖의 실패는 `TR_VC_UPDATE_STATUS_FAILED`가 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `UpdateVcStatusReqDto` | 요청 DTO. `vcId`와 `vcStatus`(`org.omnione.did.data.model.enums.vc.VcStatus`)가 필수입니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `UpdateVcStatusResDto` | 빈 응답. `txId`는 채우지 않습니다. |

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
VC 폐기 트랜잭션을 완료합니다. 현재 구현은 Provider를 호출하지 않고 `transaction`도 사용하지 않으며, 요청의 `txId`만 돌려줍니다. `COMPLETE_REVOKE` 하위 트랜잭션은 호스트가 기록합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteRevokeReqDto` | 요청 DTO. SDK는 `txId`를 읽습니다. | M |
| `transaction` | `SdkTransaction` | 호스트가 찾은 폐기 트랜잭션. 현재 구현은 사용하지 않습니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteRevokeResDto` | 요청의 `txId` |

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
`VcStatusProtocolImpl` (생성자)

#### Function Description
`VcStatusProtocolImpl`은 `VcStatusProtocol`을 구현합니다. 이 장의 앞 함수들을 참조하세요. 생성자는 Provider를 `null` 검사 없이 저장합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcStatusDataProvider` | `VcStatusDataProvider` | VC 상태 데이터 Provider | M |
| `storageProvider` | `StorageProvider` | DID 문서와 VC 메타데이터 Provider | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `VcStatusProtocolImpl` | 새 프로토콜 인스턴스 |

#### Function Declaration

```java
public VcStatusProtocolImpl(VcStatusDataProvider vcStatusDataProvider, StorageProvider storageProvider)
```

#### Function Usage

```java
VcStatusProtocol vcStatusProtocol = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

## 3. Provider 연동

호스트 애플리케이션은 다음 Provider를 구현합니다. SDK는 HTTP 서버나 데이터베이스 스키마를 제공하지 않습니다.

| Interface | Responsibility |
| --- | --- |
| `IssueDataProvider` | 발급 오퍼·트랜잭션·프로파일·VC 저장 및 조회 |
| `VcStatusDataProvider` | VC 상태·폐기 데이터 및 설정 |
| `WalletProvider` | 서명 및 ZKP 지갑 작업 |
| `StorageProvider` | DID 문서와 VC 메타데이터 저장소 |
| `CertificateVcProvider` | 인증서 VC URL 제공 |

```java
IssueVcProtocolImpl issue = new IssueVcProtocolImpl(issueDataProvider, walletProvider, storageProvider);
issue.setCertificatePublisher(certificateVcProvider);
VcStatusProtocol status = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

`IssueVcProtocolImpl`은 `generateIssueProfile`, `issueVc`, `issueVcFromProxy`에서 `CertificateVcProvider`가 필요하므로, 이 함수들을 부르기 전에 `IssueVcProtocolImpl#setCertificatePublisher`로 설정합니다. `Sdk*` 클래스는 [4. 데이터 모델](#4-데이터-모델)을 참고하세요. 요청·응답 DTO는 `source/did-issuer-sdk/src/main/java/org/omnione/did/sdk/issuer/model/dto/`에 있습니다.

모든 Provider에 공통인 규칙입니다.

- **오류**: 프로토콜 구현은 `IssuerSdkException`을 그대로 다시 던집니다. Provider가 던진 그 밖의 예외는, `null` 반환 때문에 생긴 `NullPointerException`까지 포함해, 해당 프로토콜 함수의 실패 코드(예: `TR_VC_ISSUE_FAILED`)를 가진 `IssuerSdkException`으로 바뀌고 원래 예외는 로그에만 남습니다. 자체 오류 코드를 유지하려면 `IssuerSdkException`을 던지세요. `issueVcFromProxy`는 `issueVc`에 넘기기 전에 난 예외를 바꾸지 않습니다.
- **없을 때**: SDK는 대부분의 반환값을 `null` 검사하지 않습니다. 요청한 항목이 없으면 예외를 던지세요. "없음"을 돌려줘도 되는 메서드는 `IssueDataProvider.findTransactionById`(`null`), `IssueDataProvider.findVcByUserIdAndVcPlanId`(`Optional.empty()`), `VcStatusDataProvider.findVcByVcId`(`null`), `StorageProvider.getVcMetaByVcId`(`null`)뿐입니다. `VcStatusDataProvider.findRevokeVcByVcId`는 `null` 대신 비어 있는 새 객체를 돌려줘야 합니다.
- **저장**: `save*` 메서드에는 새 객체(`id`가 `null`)와 앞서 `find*` 메서드가 돌려준 객체(`id`가 있음)가 들어옵니다. 앞의 것은 추가하고 뒤의 것은 갱신하세요.
- **트랜잭션**: SDK는 트랜잭션을 만들거나 하위 트랜잭션을 기록하지 않습니다. 호스트가 `SdkTransaction`을 만들고, 단계마다 `SdkSubTransaction`을 기록하고, 오퍼를 트랜잭션에 연결하고, 트랜잭션을 프로토콜 함수에 넘깁니다.

### IssueDataProvider

`IssueDataProvider`(`org.omnione.did.sdk.issuer.provider`)는 발급 데이터를 저장하고 조회합니다. 대상은 오퍼, 트랜잭션, 발급 프로파일, Issuer 정보, VC 프로파일, E2E 세션 데이터, 발급된 VC, ZKP 정의, VC 스키마입니다. `IssueVcProtocolImpl`이 호출합니다. 아래 예는 "없을 때"의 세 가지 동작을 보여 줍니다.

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
        return id == null ? null : transactions.get(id); // null: 아직 트랜잭션 없음
    }

    @Override
    public Optional<SdkVc> findVcByUserIdAndVcPlanId(Long userId, String vcPlanId) {
        List<SdkVc> vcs = vcsByUserAndPlan.getOrDefault(userId + "/" + vcPlanId, List.of());
        return vcs.isEmpty() ? Optional.empty() : Optional.of(vcs.get(vcs.size() - 1));
    }

    // 나머지 메서드는 생략합니다.
}
```

### IssueDataProvider.saveVcOffer

#### Class Name
`IssueDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`saveVcOffer`

#### Function Description
발급 오퍼를 저장합니다. `requestOffer`가 새 객체(`id`와 `transactionId`가 `null`)로 호출합니다. 오퍼를 트랜잭션에 연결할 때 호스트가 다시 호출합니다. SDK는 `transactionId`를 채우지 않습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcOffer` | `SdkVcOffer` | SDK가 `offerId`, `offerType`(`ISSUE_OFFER`), `vcPlanId`, `did`(Issuer DID), `validUntil`(24시간 뒤)을 채운 오퍼 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
오퍼 ID로 오퍼를 돌려줍니다. `inspectIssuePropose`(요청에 `offerId`가 있을 때, `validUntil`을 읽음)와 `issueVcResult`(`transactionId`를 읽음)가 호출합니다. 오퍼가 없으면 `IssuerSdkException(VC_OFFER_NOT_FOUND)` 같은 예외를 던지세요. `null`을 돌려주면 각각 `UNKNOWN_SERVER_ERROR`, `TR_VC_ISSUE_RESULT_FAILED`로 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | 오퍼 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVcOffer` | 오퍼. 최소한 `validUntil`과 `transactionId`를 채웁니다. |

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
`txId`로 트랜잭션을 찾고 다음 단계로 진행해도 되는지 확인합니다. SDK는 이 메서드를 호출하지 않습니다. 호스트가 프로토콜 함수에 넘길 `SdkTransaction`을 찾을 수 있도록 인터페이스에 들어 있습니다. 보통은 트랜잭션이 없거나, 만료됐거나, 마지막으로 완료된 하위 트랜잭션이 주어진 유형이 아니면 예외(예: `TRANSACTION_NOT_FOUND`, `TRANSACTION_INVALID`)를 던지도록 구현합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | 트랜잭션 ID | M |
| `type` | `SubTransactionType` | 마지막 하위 트랜잭션(직전 단계)에 기대하는 유형 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | 트랜잭션. 다음 프로토콜 함수가 읽는 필드를 채웁니다. |

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
키로 트랜잭션을 돌려줍니다. `issueVcResult`가 `SdkVcOffer.transactionId`로 호출하는데, 호스트가 오퍼를 연결하기 전에는 이 값이 `null`입니다. `id`가 `null`이거나 트랜잭션이 없으면 `null`을 돌려주세요. 그러면 SDK가 `result = false`로 응답합니다. 예외를 던지면 `issueVcResult`가 `TR_VC_ISSUE_RESULT_FAILED`로 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 트랜잭션 키(`SdkTransaction.id`). `null`일 수 있습니다. | O |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | 트랜잭션 또는 `null`. SDK는 `id`와 `txId`를 읽습니다. |

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
트랜잭션의 마지막 하위 트랜잭션(`step`이 가장 큰 것)을 돌려줍니다. `issueVcResult`가 호출하며, 돌려받은 `type`이 `COMPLETE_VC`일 때만 성공으로 응답합니다. `null`을 돌려주면 안 됩니다. `null`이면 `issueVcResult`가 `TR_VC_ISSUE_RESULT_FAILED`로 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | 트랜잭션 키 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkSubTransaction` | 마지막 하위 트랜잭션. SDK는 `type`을 읽습니다. |

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
트랜잭션 유효 시간을 돌려줍니다. SDK는 이 메서드를 호출하지 않습니다. 호스트가 발급 트랜잭션을 만들 때 `SdkTransaction.expiredAt`을 정하는 데 씁니다. 단위는 호스트가 정하며 보통 초를 씁니다.

#### Input Parameters

없음.

#### Output Parameters

| Type | Description |
| --- | --- |
| `long` | 트랜잭션 유효 시간 |

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
VC 플랜의 발급 프로파일을 돌려줍니다. `requestOffer`와 `inspectIssuePropose`는 이 메서드를 VC 플랜 확인에 쓰고 반환값은 보지 않으므로, 플랜이 없거나 발급하면 안 되는 경우 예외(예: `IssuerSdkException(VC_PLAN_ID_INVALID)`)를 던져야 합니다. `null`을 돌려주면 두 함수가 그 플랜을 통과시킵니다. `generateIssueProfile`은 `SdkTransaction.vcPlanId`로 호출해 돌려받은 객체로 프로파일을 만듭니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcPlanId` | `String` | VC 플랜 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssueProfile` | 발급 프로파일. SDK는 `vcSchemaId`, `title`, `description`, `language`, `endpoints`, `curve`, `cipher`, `padding`, `definitionId`를 읽습니다. |

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
키로 발급 프로파일을 돌려줍니다. `issueVc`가 `SdkTransaction.issueProfileId`로 호출합니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 발급 프로파일 키(`SdkIssueProfile.id`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssueProfile` | 발급 프로파일. SDK는 `vcSchemaId`를 읽습니다. |

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
Issuer 자신의 정보를 돌려줍니다. `requestOffer`, `generateIssueProfile`, `issueVc`가 호출합니다. DID는 오퍼의 issuer, 프로파일 proof(`<did>#assert`), Issuer DID 문서 조회에 쓰이고, 이름은 프로파일과 VC에 들어갑니다. 객체 전체가 `CertificateVcProvider.getCertificateVcURL`에 넘어갑니다. `null`을 돌려주면 안 됩니다.

#### Input Parameters

없음.

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkIssuerInfo` | Issuer 정보. SDK는 `did`와 `name`을 읽습니다. |

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
트랜잭션의 VC 프로파일을 저장합니다. `generateIssueProfile`은 새 객체로 호출합니다. `issueVcFromProxy`는 `findVcProfileByTransactionId`가 돌려준 객체의 `did`를 바꿔 호출하므로, `id`가 있으면 기존 기록을 갱신하세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcProfile` | `SdkVcProfile` | `profileId`, `transactionId`, `did`, `nonce`, `userId`와 ZKP 플랜이면 `zkpNonce`를 채운 프로파일 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
트랜잭션에 저장된 VC 프로파일을 돌려줍니다. `issueVc`(두 번)와 `issueVcFromProxy`가 호출합니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | 트랜잭션 키 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVcProfile` | VC 프로파일. SDK는 `profileId`, `did`, `nonce`, `zkpNonce`를 읽습니다. 이후 저장이 갱신으로 처리되도록 `id`도 돌려주세요. |

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
트랜잭션의 E2E 세션 데이터를 저장합니다. `generateIssueProfile`이 새 객체로 호출합니다. `sessionKey`는 Issuer의 임시 개인 키이므로 보호해서 저장하고 트랜잭션이 끝나면 지우세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `e2e` | `SdkE2E` | `curve`, `cipher`, `padding`, `nonce`, `sessionKey`, `transactionId`를 채운 세션 데이터 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
트랜잭션에 저장된 E2E 세션 데이터를 돌려줍니다. `issueVc`가 공유 키를 만들 때 호출합니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | 트랜잭션 키 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkE2E` | 세션 데이터. SDK는 `sessionKey`, `curve`, `cipher`, `padding`을 읽습니다. |

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
발급된 VC 기록을 저장합니다. `issueVc`가 새 VC(새 객체)에 대해 호출하고, 같은 사용자·플랜의 이전 VC가 있으면 그 VC(`findVcByUserIdAndVcPlanId`가 돌려준 객체)를 `status`를 `REVOKED`로 바꿔 호출합니다. 이전 VC의 메타데이터 상태가 이미 `REVOKED`면 이 호출은 하지 않습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vc` | `SdkVc` | VC 기록. 새 VC면 SDK가 `did`, `userId`, `vcPlanId`, `txId`, `vcId`, `vcSchemaId`, `status`, `vcType`(`VC`), `issuedAt`, `expiredAt`을 채웁니다. | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
트랜잭션에서 발급된 VC를 돌려줍니다. `completeVc`가 요청의 `txId`로 호출해 `vcId`를 비교합니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | 트랜잭션 ID(`SdkVc.txId`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVc` | VC 기록. SDK는 `vcId`를 읽습니다. |

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
사용자에게 해당 플랜으로 이전에 발급한 VC를 돌려줍니다. `issueVc`가 새 VC를 저장하기 전에 호출합니다. VC를 돌려받으면 SDK는 `StorageProvider.getVcMetaByVcId`로 메타데이터를 읽고, 메타데이터 상태가 이미 `REVOKED`가 아니면 원장에서 폐기하고 `status`를 `REVOKED`로 저장합니다. 없으면 `null`이 아니라 `Optional.empty()`를 돌려주세요. SDK는 돌려받은 한 건만 처리합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `userId` | `Long` | 사용자 키(`UserInfoDto.id`) | M |
| `vcPlanId` | `String` | VC 플랜 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `Optional<SdkVc>` | 이전 VC 또는 빈 값. SDK는 `vcId`를 읽고 객체를 다시 저장하므로 `id`도 돌려주세요. |

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
ZKP 크리덴셜 정의를 돌려줍니다. `generateIssueProfile`은 `SdkIssueProfile.definitionId`로, `issueVc`는 Holder의 ZKP 크리덴셜 요청에 든 `credDefId`로 호출합니다. ZKP 플랜에서만 호출됩니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `definitionId` | `String` | 크리덴셜 정의 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkZkpCredentialDefinition` | 정의. SDK는 `definitionId`, `schemaId`, `alias`, `definition`을 읽습니다. |

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
ZKP 크리덴셜 스키마를 돌려줍니다. `issueVc`가 ZKP 크리덴셜을 발급할 때 `SdkZkpCredentialDefinition.schemaId`로 호출하며, 스키마의 속성 이름으로 `UserInfoDto.data`에서 값을 읽습니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `schemaId` | `String` | ZKP 스키마 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkZkpSchema` | 스키마. SDK는 `schema`를 읽습니다. |

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
VC 스키마를 돌려줍니다. `generateIssueProfile`은 그 `id`를 프로파일의 크리덴셜 스키마 ID로 쓰고, `issueVc`는 클레임 정의를 포함한 스키마 전체로 VC를 만듭니다. 둘 다 `SdkIssueProfile.vcSchemaId`로 호출합니다. 없으면 `IssuerSdkException(VC_SCHEMA_NOT_FOUND)` 같은 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | VC 스키마 키 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `org.omnione.did.data.model.schema.VcSchema` | OpenDID 데이터 모델의 VC 스키마 |

#### Function Declaration

```java
org.omnione.did.data.model.schema.VcSchema getVcSchemaById(Long id);
```

### VcStatusDataProvider

`VcStatusDataProvider`(`org.omnione.did.sdk.issuer.provider`)는 `VcStatusProtocolImpl`을 위해 폐기 기록과 VC 상태를 저장하고, 폐기 인증 유형을 제공합니다.

```java
public class InMemoryVcStatusDataProvider implements VcStatusDataProvider {
    private final Map<String, SdkRevokeVc> revokeVcs = new ConcurrentHashMap<>();
    private final Map<String, SdkVc> vcs = new ConcurrentHashMap<>();

    @Override
    public SdkRevokeVc findRevokeVcByVcId(String vcId) {
        SdkRevokeVc revokeVc = revokeVcs.get(vcId);
        return revokeVc != null ? revokeVc : new SdkRevokeVc(); // null 금지
    }

    @Override
    public SdkVc findVcByVcId(String vcId) {
        return vcs.get(vcId); // null 허용
    }

    @Override
    public VerifyAuthType getRevokeAuthType() {
        return VerifyAuthType.PIN_OR_BIO;
    }

    // 나머지 메서드는 생략합니다.
}
```

### VcStatusDataProvider.findRevokeVcByVcId

#### Class Name
`VcStatusDataProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findRevokeVcByVcId`

#### Function Description
VC의 폐기 기록을 돌려줍니다. `inspectProposeRevoke`가 호출해 돌려받은 객체에 `vcId`, `nonce`, `transactionId`를 채우고 `saveRevokeVc`로 저장합니다. 기록이 없으면 `null`이 아니라 비어 있는 새 `SdkRevokeVc`를 돌려주세요. `null`이면 `TR_VC_REVOKE_PROPOSE_FAILED`로 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | 기존 기록(`id` 포함) 또는 비어 있는 새 객체 |

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
폐기 기록을 저장합니다. `inspectProposeRevoke`는 `vcId`, `nonce`, `transactionId`를 채운 뒤, `revokeVc`는 `status`를 `REVOKED`로 바꾼 뒤 호출합니다. `id` 기준으로 추가하거나 갱신하세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `revokeVc` | `SdkRevokeVc` | 폐기 기록 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | 저장된 기록. SDK는 반환값을 쓰지 않습니다. |

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
트랜잭션에 연결된 폐기 기록을 돌려줍니다. `revokeVc`가 `SdkTransaction.id`로 호출해 `vcId`와 `nonce`를 Holder 요청과 비교합니다. 없으면 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `transactionId` | `Long` | 트랜잭션 키 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkRevokeVc` | 폐기 기록. SDK는 `vcId`와 `nonce`를 읽습니다. 이후 저장이 갱신으로 처리되도록 `id`도 돌려주세요. |

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
`txId`로 폐기 트랜잭션을 찾고 다음 단계로 진행해도 되는지 확인합니다. SDK는 이 메서드를 호출하지 않습니다. 호스트가 `revokeVc`와 `completeRevoke`에 넘길 `SdkTransaction`을 찾는 데 씁니다. 기대 동작은 `IssueDataProvider.validateAndFindTransaction`과 같습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `txId` | `String` | 트랜잭션 ID | M |
| `type` | `SubTransactionType` | 마지막 하위 트랜잭션에 기대하는 유형 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkTransaction` | 트랜잭션. `revokeVc`는 `id`를 읽습니다. |

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
새 VC 상태를 반영합니다. `revokeVc`는 `REVOKED`로, `updateVcStatus`는 검사를 마친 뒤 요청한 상태로 호출합니다. 이 흐름들에서 SDK는 `StorageProvider.updateVcStatus`를 호출하지 않으므로, 원장의 VC 메타데이터 상태도 바꿔야 한다면 여기서 처리하거나 호출 후 호스트가 처리하세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |
| `status` | `VcStatus` | 새 상태(`org.omnione.did.data.model.enums.vc.VcStatus`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
호스트의 VC 기록을 돌려줍니다. `updateVcStatus`가 호출하며, 기록을 돌려받으면 SDK가 `status`를 바꿔 `saveVc`로 저장합니다. 호스트에 기록이 없으면 `null`을 돌려주세요. 그러면 SDK는 저장을 건너뜁니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SdkVc` | `id`를 포함한 VC 기록 또는 `null` |

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
VC 기록을 저장합니다. `updateVcStatus`가 `findVcByVcId`에서 받은 객체의 `status`를 새 상태의 원시 값(`ACTIVE`, `INACTIVE`, `REVOKED`)으로 바꿔 호출합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vc` | `SdkVc` | 갱신할 VC 기록 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
Holder가 폐기 요청에 서명할 때 써야 하는 인증 유형을 돌려줍니다. `inspectProposeRevoke`가 그 정수 값을 `authType`으로 돌려줍니다. `revokeVc`가 서명을 검증할 때 SDK는 이 값을 확인하지 않습니다. `null`을 돌려주면 안 됩니다.

#### Input Parameters

없음.

#### Output Parameters

| Type | Description |
| --- | --- |
| `VerifyAuthType` | 필요한 인증 유형 |

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
폐기 트랜잭션 유효 시간을 돌려줍니다. SDK는 이 메서드를 호출하지 않습니다. 호스트가 폐기 트랜잭션을 만들 때 `SdkTransaction.expiredAt`을 정하는 데 씁니다. 단위는 호스트가 정하며 보통 초를 씁니다.

#### Input Parameters

없음.

#### Output Parameters

| Type | Description |
| --- | --- |
| `long` | 트랜잭션 유효 시간 |

#### Function Declaration

```java
long getTransactionExpirationTime();
```

### WalletProvider

`WalletProvider`(`org.omnione.did.sdk.issuer.provider`)는 Issuer 키로 서명합니다. `generateCompactSignature`는 파일 지갑을, 나머지 세 메서드는 ZKP 지갑을 쓰며 ZKP 크리덴셜 정의가 있는 플랜에서만 호출됩니다.

```java
public class FileWalletProvider implements WalletProvider {
    private final WalletManagerInterface walletManager;

    public FileWalletProvider(WalletManagerInterface walletManager) {
        this.walletManager = walletManager;
    }

    @Override
    public byte[] generateCompactSignature(String keyId, String source) {
        // UTF-8 바이트의 SHA-256 해시에 대한 compact 서명
        return BaseWalletUtil.generateCompactSignature(walletManager, keyId, source);
    }

    // ZKP 메서드는 생략합니다.
}
```

### WalletProvider.generateCompactSignature

#### Class Name
`WalletProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`generateCompactSignature`

#### Function Description
Issuer 키로 문자열에 서명합니다. `generateIssueProfile`은 키 ID `assert`와 프로파일의 정렬된 JSON으로, `issueVc`는 코어 SDK가 VC proof마다 준비한 키 ID와 서명 입력(역시 `assert` 키)으로 호출합니다. `source`의 UTF-8 바이트를 SHA-256으로 해시하고, 그 해시에 대한 compact 서명을 OpenDID common SDK의 `BaseCryptoUtil.verifySignature`가 검증하는 형식으로 돌려주세요. 키는 Issuer DID 문서의 `assert` 검증 방법과 짝이 맞아야 합니다. SDK가 결과를 multibase로 인코딩합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `keyId` | `String` | Issuer 지갑의 키 ID(`assert`) | M |
| `source` | `String` | 서명할 데이터 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `byte[]` | compact 서명 |

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
크리덴셜 정의의 개인 키로 ZKP 크리덴셜 서명을 만듭니다. Holder 요청에 ZKP 크리덴셜 요청이 있을 때 `issueVc`가 호출합니다. `alias`는 `SdkZkpCredentialDefinition.alias`이고, `credentialValues`에는 `UserInfoDto.data`에서 읽은 속성 값이 들어 있습니다. `null`을 돌려주면 안 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | ZKP 지갑에 있는 크리덴셜 정의의 키 별칭 | M |
| `credentialRequest` | `CredentialRequest` | Holder의 ZKP 크리덴셜 요청 | M |
| `credentialValues` | `CredentialValues` | 서명할 속성 값 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CredentialSignature` | ZKP 크리덴셜 서명(`org.omnione.did.zkp.datamodel.credential`) |

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
ZKP 크리덴셜 서명이 올바르다는 증명을 만듭니다. `issueVc`가 `credSignature` 직후 같은 별칭과 요청으로 호출합니다. `null`을 돌려주면 안 됩니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | 크리덴셜 정의의 키 별칭 | M |
| `credentialRequest` | `CredentialRequest` | Holder의 ZKP 크리덴셜 요청 | M |
| `credentialSignature` | `CredentialSignature` | `credSignature`가 돌려준 서명 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `SignatureCorrectnessProof` | 서명 정확성 증명 |

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
크리덴셜 정의의 키 정확성 증명을 돌려줍니다. 발급 프로파일에 `definitionId`가 있으면 `generateIssueProfile`이 `SdkZkpCredentialDefinition.alias`로 호출해 ZKP 크리덴셜 오퍼에 넣습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `alias` | `String` | 크리덴셜 정의의 키 별칭 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `KeyCorrectnessProof` | 키 정확성 증명(`org.omnione.did.zkp.datamodel.credentialoffer`) |

#### Function Declaration

```java
KeyCorrectnessProof getCorrectnessProof(String alias);
```

### StorageProvider

`StorageProvider`(`org.omnione.did.sdk.issuer.provider`)는 DID 문서를 읽고 VC 메타데이터를 읽고 씁니다. 보통 원장을 대상으로 합니다. 두 프로토콜 구현이 모두 사용합니다.

```java
public class LedgerStorageProvider implements StorageProvider {
    private final Map<String, DidDocument> didDocs = new ConcurrentHashMap<>();
    private final Map<String, VcMeta> vcMetas = new ConcurrentHashMap<>();

    @Override
    public DidDocument findDidDoc(String did) {
        // did는 did:example:holder?versionId=1#pin 같은 DID URL일 수 있습니다.
        DidDocument didDocument = didDocs.get(did.split("[?#]")[0]);
        if (didDocument == null) {
            throw new IssuerSdkException(IssuerSdkErrorCode.DID_DOC_NOT_FOUND);
        }
        return didDocument;
    }

    @Override
    public VcMeta getVcMetaByVcId(String vcId) {
        return vcMetas.get(vcId); // null: 등록되지 않음
    }

    // 나머지 메서드는 생략합니다.
}
```

### StorageProvider.findDidDoc

#### Class Name
`StorageProvider` (`org.omnione.did.sdk.issuer.provider`)

#### Function Name
`findDidDoc`

#### Function Description
DID 문서를 돌려줍니다. SDK는 Issuer DID(`issueVc`), `didAuth.did`(`issueVcFromProxy`), proof의 `verificationMethod`(`issueVc`의 `accE2e`, `revokeVc`)로 호출합니다. `verificationMethod`는 키 프래그먼트가 붙고 `versionId` 쿼리가 있을 수 있는 DID URL이므로 두 형태를 모두 받아야 합니다. 문서가 없으면 `IssuerSdkException(DID_DOC_NOT_FOUND)` 같은 예외를 던지세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `did` | `String` | DID 또는 DID URL | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `DidDocument` | DID 문서(`org.omnione.did.data.model.did`) |

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
새로 발급한 VC의 메타데이터를 등록합니다. `issueVc`가 VC마다 한 번, 서명 후 응답을 암호화하기 전에 호출합니다. 예외가 나면 `issueVc`가 실패합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcMeta` | `VcMeta` | 코어 SDK가 만든 VC 메타데이터(`org.omnione.did.data.model.vc`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
VC 메타데이터를 돌려줍니다. `inspectProposeRevoke`와 `updateVcStatus`는 `null`을 받으면 `VC_NOT_FOUND`로 응답합니다. `issueVc`는 `findVcByUserIdAndVcPlanId`가 돌려준 이전 VC에 대해 호출하며, 이때는 `null`을 받아들이지 않습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `VcMeta` | VC 메타데이터 또는 `null`. SDK는 `status`를 읽습니다. |

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
VC 메타데이터의 상태를 바꿉니다. `issueVc`가 같은 사용자·플랜의 이전 VC 중 메타데이터 상태가 아직 `REVOKED`가 아닌 것에 대해 `REVOKED`로 호출합니다. `VcStatusProtocol`의 폐기·상태 변경 흐름은 이 메서드를 호출하지 않습니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `vcId` | `String` | VC ID | M |
| `status` | `VcStatus` | 새 상태(`org.omnione.did.data.model.enums.vc.VcStatus`) | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `void` | 없음 |

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
Issuer 인증서 VC의 URL을 돌려줍니다. `generateIssueProfile`과 `issueVc`가 Issuer의 `certVcRef`에 넣고, `issueVc`는 VC 메타데이터에도 넘깁니다. SDK는 돌려받은 값을 그대로 씁니다. 메서드가 하나뿐이라 람다로 구현할 수 있습니다. `IssueVcProtocolImpl#setCertificatePublisher`로 등록하세요.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `issuerInfo` | `SdkIssuerInfo` | `IssueDataProvider.getIssuerInfo`의 결과 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `String` | 인증서 VC URL |

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

## 4. 데이터 모델

공개 시그니처에 쓰이는 데이터형입니다. `Sdk*` 클래스와 `UserInfoDto`에는 인자 없는 생성자, 전체 인자 생성자, getter, setter, 정적 `builder()`가 있습니다. 표의 **M**은 SDK가 읽는 필드라는 뜻으로, Provider가 객체를 돌려줄 때 채우거나 호스트가 프로토콜 함수에 넘길 때 채워야 합니다. **O**는 SDK가 읽지 않는 필드입니다. SDK가 `save*` 메서드를 부르기 전에 직접 채우는 필드는 설명에 적었습니다.

### SdkTransaction

패키지 `org.omnione.did.sdk.issuer.model.domain`. 호스트가 만들어 저장하고 프로토콜 함수에 넘기는 트랜잭션입니다. `validateAndFindTransaction`과 `findTransactionById`도 이 형을 돌려줍니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키. SDK가 VC 프로파일, E2E 데이터, 폐기 기록, 하위 트랜잭션을 찾는 데 쓰고 `transactionId`로 저장합니다. | M |
| `txId` | `String` | 지갑과 주고받는 트랜잭션 ID. `SdkVc.txId`로 저장되고 `inspectProposeRevoke`와 `issueVcResult`가 돌려줍니다. | M |
| `refId` | `String` | `inspectIssuePropose`가 만든 참조 ID. `issueVc`가 복호화한 요청과 비교합니다. 발급 전용. | M |
| `type` | `TransactionType` | `ISSUE_VC` 또는 `REVOKE_VC` | O |
| `status` | `TransactionStatus` | `COMPLETED`, `FAILED`, `CANCELED`, `PENDING`, `FINISH` | O |
| `vcPlanId` | `String` | VC 플랜 ID. `generateIssueProfile`과 `issueVc`가 씁니다. 발급 전용. | M |
| `expiredAt` | `Instant` | 만료 시각. 호스트가 확인합니다. | O |
| `issueProfileId` | `Long` | 발급 프로파일 키. `issueVc`가 `findIssueProfileById`에 넘깁니다. 발급 전용. | M |

### SdkUser

패키지 `org.omnione.did.sdk.issuer.model.domain`. `generateIssueProfile`에 넘기는 사용자입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 사용자 키. `SdkVcProfile.userId`로 저장됩니다. | M |
| `pii` | `String` | 호스트가 사용자를 찾을 때 쓰는 개인 식별자 | O |
| `did` | `String` | Holder DID. `SdkVcProfile.did`로 저장되고 VC 주체가 됩니다. 프록시 흐름에서는 `null`일 수 있습니다. | M |
| `vcSchemaId` | `Long` | VC 스키마 키 | O |
| `data` | `String` | 사용자 데이터 | O |

### UserInfoDto

패키지 `org.omnione.did.sdk.issuer.model.dto`. `issueVc`와 `issueVcFromProxy`에 넘기는 사용자 데이터입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 사용자 키. `findVcByUserIdAndVcPlanId`에 쓰이고 `SdkVc.userId`로 저장됩니다. | M |
| `pii` | `String` | 개인 식별자 | O |
| `did` | `String` | Holder DID. 읽지 않습니다. VC 주체는 `SdkVcProfile.did`를 씁니다. | O |
| `vcSchemaId` | `Long` | VC 스키마 키 | O |
| `data` | `String` | 클레임 값을 담은 JSON 객체. 키는 VC 스키마의 클레임 코드 `<namespace id>.<claim id>`이고, ZKP면 ZKP 스키마의 속성 이름도 키로 씁니다. 값은 문자열로 읽으며 없는 키는 건너뜁니다. | M |
| `validFrom` | `ZonedDateTime` | VC 유효 시작. `null`이면 발급 시각을 씁니다. | O |
| `validUntil` | `ZonedDateTime` | VC 유효 종료. `null`이면 `9999-12-31T23:59:59Z`를 씁니다. | O |

### SdkVcOffer

패키지 `org.omnione.did.sdk.issuer.model.domain`. `requestOffer`가 만드는 발급 오퍼입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `offerId` | `String` | 오퍼 ID(UUID). SDK가 채웁니다. | O |
| `offerType` | `OfferType` | `ISSUE_OFFER`(`IssueOffer`) 또는 `VERIFY_OFFER`(`VerifyOffer`). SDK는 `ISSUE_OFFER`를 채웁니다. | O |
| `vcPlanId` | `String` | VC 플랜 ID. SDK가 채웁니다. | O |
| `did` | `String` | Issuer DID. SDK가 채웁니다. | O |
| `validUntil` | `Instant` | 만료 시각. SDK가 생성 후 24시간으로 채우며, 지나면 `inspectIssuePropose`가 `VC_OFFER_EXPIRED`를 던집니다. | M |
| `transactionId` | `Long` | 연결된 트랜잭션 키. 호스트가 채웁니다. `issueVcResult`가 `findTransactionById`에 넘깁니다. | M |

### SdkIssueProfile

패키지 `org.omnione.did.sdk.issuer.model.domain`. VC 플랜의 발급 프로파일로, `findIssueProfileByVcPlanId`와 `findIssueProfileById`가 돌려줍니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키. 호스트가 `SdkTransaction.issueProfileId`로 저장합니다. | O |
| `vcSchemaId` | `Long` | `getVcSchemaById`에 넘기는 VC 스키마 키 | M |
| `vcPlanId` | `String` | VC 플랜 ID | O |
| `title` | `String` | 프로파일 제목 | M |
| `description` | `String` | 프로파일 설명 | M |
| `endpoints` | `List<String>` | 프로파일 process에 들어가는 Issuer 엔드포인트 | M |
| `cipher` | `String` | E2E 암호: `AES-128-CBC`, `AES-128-ECB`, `AES-256-CBC`, `AES-256-ECB` | M |
| `curve` | `String` | E2E 키 커브: `Secp256k1`, `Secp256r1` | M |
| `padding` | `String` | E2E 패딩: `NOPAD`, `PKCS5` | M |
| `language` | `String` | 프로파일 언어 | M |
| `issuanceMode` | `VcIssuanceMode` | `DIRECT` 또는 `PROXY` | O |
| `initiateType` | `InitiateType` | `USER_INIT`(`user_init`) 또는 `ISSUER_INIT`(`issuer_init`) | O |
| `tags` | `List<String>` | 태그 | O |
| `zkpEnabled` | `Boolean` | ZKP 여부. 읽지 않으며 SDK는 `definitionId`로 판단합니다. | O |
| `definitionId` | `String` | ZKP 크리덴셜 정의 ID. 있으면 `generateIssueProfile`이 ZKP 크리덴셜 오퍼를 추가합니다. | O |
| `userQueryType` | `UserQueryType` | `API`, `DB`, `TEST`. 호스트의 사용자 조회용 | O |
| `userQueryUrl` | `String` | 사용자 조회 URL | O |
| `userQueryHeaders` | `List<HeaderEntry>` | 사용자 조회 헤더. `HeaderEntry`는 `key`와 `value`를 가집니다. | O |

### SdkIssuerInfo

패키지 `org.omnione.did.sdk.issuer.model.domain`. `getIssuerInfo`가 돌려주고 `CertificateVcProvider`에 넘기는 Issuer 정보입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `did` | `String` | Issuer DID | M |
| `name` | `String` | 프로파일과 VC에 표시되는 Issuer 이름 | M |
| `status` | `IssuerStatus` | `ACTIVATE`, `DEACTIVATE`, `DID_DOCUMENT_REQUIRED`, `DID_DOCUMENT_REQUESTED`, `CERTIFICATE_VC_REQUIRED` | O |
| `serverUrl` | `String` | Issuer 서버 URL | O |
| `certificateUrl` | `String` | 인증서 VC URL. `CertificateVcProvider`가 쓸 수 있습니다. | O |
| `publishedCertificateUrl` | `String` | 게시된 인증서 VC URL. `CertificateVcProvider`가 쓸 수 있습니다. | O |

### SdkVcProfile

패키지 `org.omnione.did.sdk.issuer.model.domain`. `generateIssueProfile`이 저장하는 트랜잭션별 발급 상태입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키. `issueVcFromProxy`에서 기록을 갱신할 때 필요합니다. | O |
| `profileId` | `String` | 발행한 프로파일의 ID. `issueVc`가 요청과 비교합니다(`VC_PROFILE_INVALID`). | M |
| `did` | `String` | Holder DID. VC 주체로 쓰입니다. | M |
| `nonce` | `String` | Issuer nonce(multibase). E2E 키에 쓰이고 요청의 nonce, `didAuth.authNonce`와 비교됩니다. | M |
| `zkpNonce` | `String` | ZKP 크리덴셜 오퍼의 nonce(10진수). ZKP 플랜에서만 채워지며 ZKP 발급에 필요합니다. | O |
| `userId` | `Long` | 사용자 키. SDK가 채웁니다. | O |
| `transactionId` | `Long` | 트랜잭션 키. SDK가 채웁니다. | O |

### SdkE2E

패키지 `org.omnione.did.sdk.issuer.model.domain`. `generateIssueProfile`이 저장하는 E2E 세션 데이터입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `sessionKey` | `String` | Issuer의 임시 개인 키(인코딩 후 multibase). 비밀 값입니다. | M |
| `nonce` | `String` | E2E nonce. `SdkVcProfile.nonce`와 같은 값입니다. | O |
| `curve` | `String` | 키 커브 | M |
| `cipher` | `String` | 암호 | M |
| `padding` | `String` | 패딩 | M |
| `transactionId` | `Long` | 트랜잭션 키. SDK가 채웁니다. | O |

### SdkVc

패키지 `org.omnione.did.sdk.issuer.model.domain`. `issueVc`가 저장하고 `completeVc`와 `updateVcStatus`가 쓰는 발급 VC 기록입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키. SDK가 돌려받은 기록을 다시 저장할 때 필요합니다. | O |
| `did` | `String` | Holder DID | O |
| `vcId` | `String` | VC ID | M |
| `txId` | `String` | 발급 트랜잭션 ID. `findVcByTxId`의 조회 키 | O |
| `issuedAt` | `Instant` | 발급 시각 | O |
| `expiredAt` | `Instant` | 유효 종료 | O |
| `vcPlanId` | `String` | VC 플랜 ID | O |
| `userId` | `Long` | 사용자 키 | O |
| `vcSchemaId` | `String` | VC에 들어 있는 VC 스키마 ID(URL) | O |
| `status` | `String` | `VcStatus` 원시 값: `ACTIVE`, `INACTIVE`, `REVOKED` | O |
| `vcType` | `String` | SDK가 발급한 VC는 `VC` | O |

### SdkRevokeVc

패키지 `org.omnione.did.sdk.issuer.model.domain`. `inspectProposeRevoke`와 `revokeVc`가 쓰는 폐기 기록입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `nonce` | `String` | `inspectProposeRevoke`가 채운 Issuer nonce(multibase). `revokeVc`가 디코딩한 바이트를 요청과 비교합니다. | M |
| `vcId` | `String` | VC ID. `revokeVc`가 요청과 비교합니다. | M |
| `status` | `VcStatus` | `org.omnione.did.data.model.enums.vc.VcStatus`. `revokeVc`가 `REVOKED`로 채웁니다. | O |
| `transactionId` | `Long` | `inspectProposeRevoke`가 채운 트랜잭션 키. `findRevokeVcByTransactionId`의 조회 키 | O |

### SdkSubTransaction

패키지 `org.omnione.did.sdk.issuer.model.domain`. 호스트가 프로토콜 호출마다 저장하는 단계 기록입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `step` | `Integer` | 단계 번호. 마지막 기록이 가장 큰 값을 가집니다. | O |
| `type` | `SubTransactionType` | 단계 유형. `issueVcResult`가 읽습니다. | M |
| `status` | `SubTransactionStatus` | `COMPLETED`, `FAILED`, `CANCELED` | O |
| `transactionId` | `Long` | 트랜잭션 키 | O |

### SdkZkpCredentialDefinition

패키지 `org.omnione.did.sdk.issuer.model.domain`. `findZkpCredentialDefinitionByDefinitionId`가 돌려주는 ZKP 크리덴셜 정의입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `definitionId` | `String` | 크리덴셜 정의 ID. 크리덴셜 오퍼에 들어갑니다. | M |
| `schemaId` | `String` | ZKP 스키마 ID. 크리덴셜 오퍼에 들어가고 `findZkpSchemaBySchemaId`에 넘어갑니다. | M |
| `type` | `CredentialType` | ZKP SDK의 크리덴셜 유형 | O |
| `alias` | `String` | ZKP 지갑에 있는 정의의 키 별칭. `WalletProvider`에 넘어갑니다. | M |
| `tag` | `String` | 태그 | O |
| `version` | `String` | 버전 | O |
| `definition` | `String` | ZKP `CredentialDefinition`의 JSON. `issueVc`가 씁니다. | M |
| `zkpSchemaId` | `Long` | ZKP 스키마의 호스트 키 | O |
| `status` | `ZkpCredentialDefinitionStatus` | `ACTIVATE`, `NEED_BLOCKCHAIN_REGISTRATION`, `NEED_LIST_PROVIDER_REGISTRATION` | O |

### SdkZkpSchema

패키지 `org.omnione.did.sdk.issuer.model.domain`. `findZkpSchemaBySchemaId`가 돌려주는 ZKP 크리덴셜 스키마입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `id` | `Long` | 호스트 키 | O |
| `schemaId` | `String` | ZKP 스키마 ID | O |
| `name` | `String` | 스키마 이름 | O |
| `version` | `String` | 버전 | O |
| `tag` | `String` | 태그 | O |
| `status` | `ZkpSchemaStatus` | `ACTIVATE`, `NEED_BLOCKCHAIN_REGISTRATION`, `NEED_LIST_PROVIDER_REGISTRATION` | O |
| `schema` | `String` | ZKP `CredentialSchema`의 JSON. `issueVc`가 속성 이름을 읽습니다. | M |

### RequestProof

패키지 `org.omnione.did.sdk.issuer.model.data`. 서명된 요청 객체 `AccE2e`(`IssueVcReqDto.accE2e`), `DidAuth`(`IssueVcReqDto.didAuth`), `ReqRevokeVc`(`RevokeVcReqDto.request`)의 추상 상위 클래스입니다. 서명 대상은 `proof`에서 `proofValue`를 뺀 객체를 정렬된 JSON으로 만든 뒤의 SHA-256 해시입니다.

| Field | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `proof` | `Proof` | `org.omnione.did.data.model.did.Proof`. `type`, `created`, `verificationMethod`(서명 키의 DID URL), `proofPurpose`, `proofValue`(multibase compact 서명)를 가집니다. `ReqRevokeVc`에서는 필수입니다. `AccE2e`와 `DidAuth`에서는 `null`이면 서명 검증을 건너뜁니다. | O |

### SubTransactionType

패키지 `org.omnione.did.sdk.issuer.model.common.enums`. `SdkSubTransaction`의 단계 유형입니다. 호스트가 기록하며 SDK는 `COMPLETE_VC`만 읽습니다.

| Value | Description |
| --- | --- |
| `OFFER_ISSUE_VC` | 오퍼 단계 |
| `INSPECT_ISSUE_PROPOSE` | `inspectIssuePropose` 이후 |
| `GENERATE_ISSUE_PROFILE` | `generateIssueProfile` 이후 |
| `ISSUE_VC` | `issueVc` 또는 `issueVcFromProxy` 이후 |
| `COMPLETE_VC` | `completeVc` 이후. 마지막 하위 트랜잭션이 이 유형일 때만 `issueVcResult`가 `true`를 돌려줍니다. |
| `INSPECT_REVOKE_PROPOSE` | `inspectProposeRevoke` 이후 |
| `REVOKE_VC` | `revokeVc` 이후 |
| `COMPLETE_REVOKE` | `completeRevoke` 이후 |

### VcStatus

패키지 `org.omnione.did.data.model.enums.vc`(OpenDID 데이터 모델 SDK). `UpdateVcStatusReqDto`, `SdkRevokeVc`, `VcStatusDataProvider.updateVcStatus`, `StorageProvider.updateVcStatus`가 쓰는 형입니다. `getRawValue()`는 문자열 값을 돌려주고, `fromString(String)`은 대소문자를 무시하고 값에 맞는 상수를 돌려주며 없으면 `null`입니다. SDK에는 상수가 같은 별도의 `org.omnione.did.sdk.issuer.model.enums.VcStatus`도 있지만 프로토콜·Provider 메서드는 이를 쓰지 않습니다.

| Value | Description |
| --- | --- |
| `ACTIVE` | 원시 값 `ACTIVE`. 유효한 VC |
| `INACTIVE` | 원시 값 `INACTIVE`. 비활성 VC |
| `REVOKED` | 원시 값 `REVOKED`. 폐기된 VC로, `VcStatusProtocol`로는 더 이상 상태를 바꿀 수 없습니다. |

### VerifyAuthType

패키지 `org.omnione.did.sdk.issuer.model.enums`. `getRevokeAuthType`이 돌려주는 인증 유형입니다. `getValue()`와 `toJson()`은 정수 값을 돌려주고, `fromJson(String)`은 상수 이름만 받으며 그 밖의 값이면 `IllegalArgumentException`을 던집니다.

| Value | Description |
| --- | --- |
| `NO_RESTRICTIONS_AUTHENTICATION` | `0x00000000`, 제한 없음 |
| `NO_AUTHENTICATION` | `0x00000001`, 인증 없음 |
| `PIN` | `0x00000002`, PIN |
| `BIO` | `0x00000004`, 생체 인증 |
| `PIN_OR_BIO` | `0x00000006`, PIN 또는 생체 인증 |
| `PIN_AND_BIO` | `0x00008006`, PIN과 생체 인증 |

## 5. 보조 유틸리티

SDK 구현을 돕는 보조 클래스입니다. 같은 일을 하는 주요 API가 있으면 주요 API를 쓰는 것을 권장합니다.

### ValidationUtil

패키지 `org.omnione.did.sdk.issuer.util`. 프로토콜 구현이 쓰는 정적 검사 메서드입니다. 프로토콜 함수가 요청 서명을 이미 검증하므로, SDK 흐름을 위해 호스트가 따로 호출할 필요는 없습니다.

| Method | Description |
| --- | --- |
| `public static boolean isExpiredDate(Instant expiredAt)` | 현재 시각이 `expiredAt` 이후면 `true`를 돌려줍니다. `expiredAt`이 `null`이면 `NullPointerException`을 던집니다. |
| `public static void verifySign(RequestProof request, DidDocument didDocument)` | `didDocument`에서 ID가 `proof.verificationMethod`의 프래그먼트인 공개 키로 `request.proof`를 검증합니다. 서명 대상은 `proofValue`를 뺀 `request`의 정렬된 JSON에 대한 SHA-256 해시이고, 키는 항상 Secp256r1로 취급합니다. 서명이 맞으면 그대로 반환하고, 틀리면 `CommonSdkException`(`SIGNATURE_VERIFICATION_FAILED`)을, proof나 키가 없으면 `NullPointerException`을 던집니다. 이 메서드는 `request.proof`를 `proofValue`가 없는 사본으로 바꿉니다. |
