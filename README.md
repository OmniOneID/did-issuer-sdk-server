# Server Issuer SDK

The Server Issuer SDK provides Java 21 protocol logic for OpenDID VC issuance, VC status updates, and revocation. The host application supplies storage, wallet, and persistence implementations.

## Folder Structure

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

| Path | Description |
| --- | --- |
| `docs/api` | Java protocol API and SDK error reference |
| `source/did-issuer-sdk` | Gradle library project and OpenDID dependencies |

## Build and use

Build the SDK with JDK 21:

```bash
cd source/did-issuer-sdk
./gradlew clean build
```

The output is `build/libs/did-issuer-sdk-1.0.0.jar`. To update a sibling `did-issuer-server` checkout, copy this JAR into its `source/did-issuer-server/libs/` directory. The server also needs the OpenDID dependency JARs used by the SDK, including `did-common-sdk-server-2.0.0.jar`.

For build dependencies and host integration, see the [source project README](source/did-issuer-sdk/README.md).

## API Reference

- [Issuer SDK API](docs/api/Issuer_API.md)
- [Issuer SDK API (Korean)](docs/api/Issuer_API_ko.md)
- [Issuer SDK error codes](docs/api/IssuerErrorCode.md)

## Change Log

See [CHANGELOG.md](CHANGELOG.md).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

## License

[Apache License 2.0](LICENSE)
