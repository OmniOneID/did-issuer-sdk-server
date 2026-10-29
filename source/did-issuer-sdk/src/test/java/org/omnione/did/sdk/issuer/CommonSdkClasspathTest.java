package org.omnione.did.sdk.issuer;

import org.junit.jupiter.api.Test;
import org.omnione.did.common.util.IdGenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonSdkClasspathTest {
    @Test
    void usesCommonSdkWithIssuerIdMethods() throws NoSuchMethodException {
        assertNotNull(IdGenerator.class.getMethod("generateUUID"));
        assertNotNull(IdGenerator.class.getMethod("generateRefId"));
        assertTrue(IdGenerator.class.getProtectionDomain().getCodeSource().getLocation().toString()
                .contains("did-common-sdk-server-2.0.0.jar"));
    }
}
