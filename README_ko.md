# Server Issuer SDK

OpenDID VC 발급, 상태 변경, 폐기 프로토콜을 제공하는 Java 21 SDK입니다. 저장소·지갑·영속성 연동은 사용하는 애플리케이션이 구현합니다.

## 폴더 구조

```text
did-issuer-sdk-server
├── CHANGELOG.md
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── README_ko.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── api
│       ├── Issuer_API.md
│       ├── Issuer_API_ko.md
│       └── IssuerErrorCode.md
└── source
    └── did-issuer-sdk
        ├── README.md
        ├── README_ko.md
        ├── build.gradle
        ├── gradle
        ├── gradlew
        ├── gradlew.bat
        ├── libs
        ├── settings.gradle
        └── src
```

| 경로 | 설명 |
| --- | --- |
| `docs/api` | Java 프로토콜 API 및 SDK 오류 코드 |
| `source/did-issuer-sdk` | Gradle 라이브러리 프로젝트와 OpenDID 의존 JAR |

## 빌드와 사용

JDK 21에서 빌드합니다.

```bash
cd source/did-issuer-sdk
./gradlew clean build
```

결과물은 `build/libs/did-issuer-sdk-1.0.0.jar`입니다. 형제 폴더의 `did-issuer-server`를 갱신할 때 이 JAR을 서버의 `source/did-issuer-server/libs/`에 복사합니다. 서버에는 `did-common-sdk-server-2.0.0.jar` 등 SDK가 사용하는 OpenDID 의존 JAR도 필요합니다.

의존성과 Provider 연동 방법은 [소스 프로젝트 README](source/did-issuer-sdk/README_ko.md)를 참고하세요.

## API 문서

- [Issuer SDK API](docs/api/Issuer_API.md)
- [Issuer SDK API 국문](docs/api/Issuer_API_ko.md)
- [Issuer SDK 오류 코드](docs/api/IssuerErrorCode.md)

## 변경 내역

[CHANGELOG.md](CHANGELOG.md)를 참고하세요.

## 기여

[CONTRIBUTING.md](CONTRIBUTING.md)와 [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)를 참고하세요.

## 라이선스

[Apache License 2.0](LICENSE)
