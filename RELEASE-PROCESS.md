# Release process

1. Update `CHANGELOG.md` and the version in `source/did-issuer-sdk/build.gradle`.
2. Run `./gradlew clean build` from `source/did-issuer-sdk` with JDK 21.
3. Review changes to public protocol, provider, model and error APIs for compatibility with `did-issuer-server`.
4. Publish the validated JAR together with its declared dependencies and bundled OpenDID JAR requirements.
