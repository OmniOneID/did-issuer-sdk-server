package org.omnione.did.sdk.issuer.provider;

import org.omnione.did.data.model.enums.vc.VcStatus;
import org.omnione.did.sdk.issuer.model.common.enums.SubTransactionType;
import org.omnione.did.sdk.issuer.model.common.enums.TransactionStatus;
import org.omnione.did.sdk.issuer.model.domain.SdkRevokeVc;
import org.omnione.did.sdk.issuer.model.domain.SdkSubTransaction;
import org.omnione.did.sdk.issuer.model.domain.SdkTransaction;
import org.omnione.did.sdk.issuer.model.domain.SdkVc;
import org.omnione.did.sdk.issuer.model.enums.VerifyAuthType;

/**
 * Interface provider for VC status database operations.
 */
public interface VcStatusDataProvider {

    /* RevokeVc Operations */
    SdkRevokeVc findRevokeVcByVcId(String vcId);

    SdkRevokeVc saveRevokeVc(SdkRevokeVc revokeVc);

    SdkRevokeVc findRevokeVcByTransactionId(Long transactionId);

    /* Transaction Operations */

    SdkTransaction validateAndFindTransaction(String txId, SubTransactionType type);

    /* Vc Operations */
    void updateVcStatus(String vcId, VcStatus status);

    SdkVc findVcByVcId(String vcId);

    void saveVc(SdkVc vc);

    /* Config Operations */
    VerifyAuthType getRevokeAuthType();

    long getTransactionExpirationTime();
}
