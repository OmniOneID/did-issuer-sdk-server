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
- 일자: 2026-10-29
- 버전: v1.0.0

| 버전 | 일자 | 변경 내용 |
| --- | --- | --- |
| v1.0.0 | 2026-10-29 | SDK 분리 후 API 문서 구성 |

<div style="page-break-after: always;"></div>

# 목차
- [1. VC 발급](#1-vc-발급)
- [2. VC 상태 및 폐기](#2-vc-상태-및-폐기)
- [3. Provider 연동](#3-provider-연동)

# 기능 목록
## 1. VC 발급

### requestOffer

#### Class Name
`IssueVcProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`requestOffer`

#### Function Description
VC 발급 오퍼를 생성합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `OfferIssueVcReqDto` | 요청 DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `OfferIssueVcResDto` | VC 발급 오퍼를 생성합니다. |

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
발급 제안을 검증합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectIssueProposeReqDto` | 요청 DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectIssueProposeResDto` | 발급 제안을 검증합니다. |

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
트랜잭션과 사용자에 대한 발급 프로파일을 생성합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `GenerateIssueProfileReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |
| `user` | `SdkUser` | 사용자 정보 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `GenerateIssueProfileResDto` | 트랜잭션과 사용자에 대한 발급 프로파일을 생성합니다. |

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
VC를 발급합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |
| `user` | `UserInfoDto` | 사용자 정보 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | VC를 발급합니다. |

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
프록시 발급 흐름에서 VC를 발급합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `IssueVcReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |
| `user` | `UserInfoDto` | 사용자 정보 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResDto` | 프록시 발급 흐름에서 VC를 발급합니다. |

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
VC 발급 트랜잭션을 완료합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteVcReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteVcResDto` | VC 발급 트랜잭션을 완료합니다. |

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
오퍼 ID로 발급 결과를 조회합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `offerId` | `String` | 발급 오퍼 ID | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `IssueVcResultResDto` | 오퍼 ID로 발급 결과를 조회합니다. |

#### Function Declaration

```java
IssueVcResultResDto issueVcResult(String offerId);
```

## 2. VC 상태 및 폐기

### inspectProposeRevoke

#### Class Name
`VcStatusProtocol` (`org.omnione.did.sdk.issuer.protocol.vc`)

#### Function Name
`inspectProposeRevoke`

#### Function Description
폐기 제안을 검증합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `InspectProposeRevokeReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `InspectProposeRevokeResDto` | 폐기 제안을 검증합니다. |

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
VC를 폐기합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `RevokeVcReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `RevokeVcResDto` | VC를 폐기합니다. |

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
VC 상태를 갱신합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `UpdateVcStatusReqDto` | 요청 DTO | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `UpdateVcStatusResDto` | VC 상태를 갱신합니다. |

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
VC 폐기 트랜잭션을 완료합니다.

#### Input Parameters

| Parameter | Type | Description | **M/O** |
| --- | --- | --- | --- |
| `request` | `CompleteRevokeReqDto` | 요청 DTO | M |
| `transaction` | `SdkTransaction` | SDK 트랜잭션 | M |

#### Output Parameters

| Type | Description |
| --- | --- |
| `CompleteRevokeResDto` | VC 폐기 트랜잭션을 완료합니다. |

#### Function Declaration

```java
CompleteRevokeResDto completeRevoke(CompleteRevokeReqDto request, SdkTransaction transaction);
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
IssueVcProtocol issue = new IssueVcProtocolImpl(issueDataProvider, walletProvider, storageProvider);
VcStatusProtocol status = new VcStatusProtocolImpl(vcStatusDataProvider, storageProvider);
```

`CertificateVcProvider`가 필요한 발급 흐름에서는 `IssueVcProtocolImpl#setCertificatePublisher`로 설정합니다. 세부 필드 계약은 `source/did-issuer-sdk/src/main/java/org/omnione/did/sdk/issuer/model/`을 참고하세요.
