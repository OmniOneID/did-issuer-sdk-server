package org.omnione.did.sdk.issuer.protocol.vc;

import org.omnione.did.sdk.issuer.model.domain.SdkTransaction;
import org.omnione.did.sdk.issuer.model.dto.*;

/**
 * Service interface for handling Verifiable Credential (VC) status in the SDK.
 */
public interface VcStatusProtocol {
    /**
     * Inspects the propose revoke for a Verifiable Credential.
     *
     * @param request     the request containing the VC to inspect the propose
     *                    revoke
     *                    for
     * @param transaction the database transaction related to this issue
     * @return the inspection result for the given VC
     */
    InspectProposeRevokeResDto inspectProposeRevoke(InspectProposeRevokeReqDto request, SdkTransaction transaction);

    /**
     * Revokes a Verifiable Credential.
     *
     * @param request     the request containing the Verifiable Credential to revoke
     * @param transaction the database transaction related to this issue
     * @return the response indicating the result of revoking the Verifiable
     *         Credential
     */
    RevokeVcResDto revokeVc(RevokeVcReqDto request, SdkTransaction transaction);

    /**
     * Updates the status of a Verifiable Credential.
     *
     * @param request the VC to update the status for
     * @return the result of updating the status for the given VC
     */
    UpdateVcStatusResDto updateVcStatus(UpdateVcStatusReqDto request);

    /**
     * Completes the revocation process for a Verifiable Credential.
     *
     * @param request     The completion request containing the transaction ID.
     * @param transaction the database transaction related to this issue
     * @return CompleteRevokeResDto containing the transaction ID of the completed
     *         revocation.
     */
    CompleteRevokeResDto completeRevoke(CompleteRevokeReqDto request, SdkTransaction transaction);
}
