package org.omnione.did.sdk.issuer.provider;

import org.omnione.did.sdk.issuer.model.common.enums.SubTransactionType;
import org.omnione.did.sdk.issuer.model.common.enums.TransactionStatus;
import org.omnione.did.sdk.issuer.model.domain.*;

import java.util.Optional;

/**
 * Interface provider for issuer database operations.
 * Abstracts the interaction with the underlying data store for transactions,
 * offers, profiles, and more.
 */
public interface IssueDataProvider {

    /* VcOffer Operations */
    void saveVcOffer(SdkVcOffer vcOffer);

    SdkVcOffer findVcOfferByOfferId(String offerId);

    /* Transaction Operations */

    SdkTransaction validateAndFindTransaction(String txId, SubTransactionType type);

    SdkTransaction findTransactionById(Long id);

    SdkSubTransaction findSubTransactionByTransactionIdOrderByStepDesc(Long transactionId);

    long getTransactionExpirationTime();

    /* IssueProfile Operations */
    SdkIssueProfile findIssueProfileByVcPlanId(String vcPlanId);

    SdkIssueProfile findIssueProfileById(Long id);

    /* IssuerInfo Operations */
    SdkIssuerInfo getIssuerInfo();

    /* VcProfile Operations */
    void saveVcProfile(SdkVcProfile vcProfile);

    SdkVcProfile findVcProfileByTransactionId(Long transactionId);

    /* E2E Operations */
    void saveE2E(SdkE2E e2e);

    SdkE2E findE2EByTransactionId(Long transactionId);

    /* VC Operations */
    void saveVc(SdkVc vc);

    SdkVc findVcByTxId(String txId);

    Optional<SdkVc> findVcByUserIdAndVcPlanId(Long userId, String vcPlanId);

    /* ZKP Operations */
    SdkZkpCredentialDefinition findZkpCredentialDefinitionByDefinitionId(String definitionId);

    SdkZkpSchema findZkpSchemaBySchemaId(String schemaId);

    /* VcSchema Operations */
    org.omnione.did.data.model.schema.VcSchema getVcSchemaById(Long id);
}
