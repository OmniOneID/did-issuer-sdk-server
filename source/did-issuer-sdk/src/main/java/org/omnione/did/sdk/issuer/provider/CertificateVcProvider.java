package org.omnione.did.sdk.issuer.provider;

import org.omnione.did.sdk.issuer.model.domain.SdkIssuerInfo;

/**
 * Interface to provide the certificate Vc URL.
 * This interface decouples the SDK from the Issuer package's helper class.
 */
public interface CertificateVcProvider {
    String getCertificateVcURL(SdkIssuerInfo issuerInfo);
}
