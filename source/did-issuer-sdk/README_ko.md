# Issuer SDK 소스 프로젝트

`org.omnione.did:did-issuer-sdk:1.0.0`을 빌드하는 Gradle `java-library` 모듈입니다. VC 발급·폐기 프로토콜, DTO, 전송 모델, Provider 인터페이스, 검증 및 SDK 오류를 포함합니다. HTTP 엔드포인트, Spring bean, 데이터베이스 스키마는 포함하지 않습니다.

## 빌드

JDK 21이 필요합니다. 이 폴더에서 실행합니다.

```bash
./gradlew clean build
```

JAR은 `build/libs/`에 생성됩니다. `libs/`의 OpenDID JAR은 SDK 컴파일에 필요합니다. SDK의 공개 인터페이스가 이 JAR들의 타입을 사용하므로 SDK를 사용하는 쪽에도 해당 의존성이 필요합니다.

## Issuer Server 연동

SDK를 빌드한 뒤 `build/libs/did-issuer-sdk-1.0.0.jar`을 서버의 `source/did-issuer-server/libs/`에 복사합니다. 서버는 이 JAR을 사용하며 SDK 소스를 직접 컴파일하지 않습니다. 서버 `libs/`에는 `did-common-sdk-server-2.0.0.jar`을 포함한 OpenDID 의존 JAR도 유지합니다.

## 구조

공개 진입점은 `IssueVcProtocol`과 `VcStatusProtocol`입니다. 구현체는 호스트가 제공하는 `IssueDataProvider`, `VcStatusDataProvider`, `WalletProvider`, `StorageProvider`와 필요한 경우 `CertificateVcProvider`를 사용합니다. 호스트는 자체 영속성 엔티티와 `Sdk*` 전송 모델을 변환합니다.

Provider 인터페이스 또는 DTO를 변경할 때는 사용하는 애플리케이션도 함께 수정해야 합니다. [API 문서](../../docs/api/Issuer_API_ko.md)와 [오류 코드](../../docs/api/IssuerErrorCode.md)를 참고하세요.
