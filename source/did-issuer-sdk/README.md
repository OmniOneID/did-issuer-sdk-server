# Issuer SDK source project

This Gradle `java-library` module builds `org.omnione.did:did-issuer-sdk:1.0.0`. It contains VC issuance and revocation protocols, DTOs, transfer models, provider interfaces, validation, and SDK errors. It has no HTTP endpoints, Spring beans, or database schema.

## Build

JDK 21 is required. From this directory:

```bash
./gradlew clean build
```

The JAR is written to `build/libs/`. The `libs/` directory supplies OpenDID SDK dependencies for compilation. The SDK's public interfaces refer to classes from those JARs, so consumers must provide the corresponding dependencies at compile time and runtime.

## Use with Issuer Server

Build this project, then copy `build/libs/did-issuer-sdk-1.0.0.jar` to the server's `source/did-issuer-server/libs/`. The server's Gradle build uses the JAR in `libs/`; it does not compile SDK sources. Keep its OpenDID dependency JARs, including `did-common-sdk-server-2.0.0.jar`, in the same server `libs/` directory.

## Architecture

`IssueVcProtocol` and `VcStatusProtocol` define the public protocol entry points. Their implementations call host-provided `IssueDataProvider`, `VcStatusDataProvider`, `WalletProvider`, `StorageProvider`, and, where needed, `CertificateVcProvider`. The host maps its own persistence entities to the `Sdk*` transfer models.

Changing a provider interface or DTO requires coordinated changes in host applications. See the [API reference](../../docs/api/Issuer_API.md) and [error codes](../../docs/api/IssuerErrorCode.md).
