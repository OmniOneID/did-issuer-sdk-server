package org.omnione.did.sdk.issuer.provider;

import org.omnione.did.zkp.datamodel.credential.CredentialValues;
import org.omnione.did.zkp.datamodel.credential.CredentialSignature;
import org.omnione.did.zkp.datamodel.credential.SignatureCorrectnessProof;
import org.omnione.did.zkp.datamodel.credentialrequest.CredentialRequest;
import org.omnione.did.zkp.datamodel.credentialoffer.KeyCorrectnessProof;

/**
 * Interface provider for wallet operations (cryptography and key management).
 * Abstracts the interaction with both file-based wallets and ZKP wallets.
 */
public interface WalletProvider {

    /* File Wallet Operations (Legacy/Simple) */
    byte[] generateCompactSignature(String keyId, String source);

    /* ZKP Wallet Operations */
    CredentialSignature credSignature(String alias, CredentialRequest credentialRequest,
            CredentialValues credentialValues);

    SignatureCorrectnessProof signatureCorrectnessProof(String alias, CredentialRequest credentialRequest,
            CredentialSignature credentialSignature);

    KeyCorrectnessProof getCorrectnessProof(String alias);
}
