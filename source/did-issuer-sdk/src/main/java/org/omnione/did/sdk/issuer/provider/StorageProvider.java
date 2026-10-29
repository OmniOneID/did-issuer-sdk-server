package org.omnione.did.sdk.issuer.provider;

import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.enums.vc.VcStatus;
import org.omnione.did.data.model.vc.VcMeta;

/**
 * Interface provider for storage operations.
 * Abstracts the interaction with external storage or blockchain for DID
 * documents and VC metadata.
 */
public interface StorageProvider {

    DidDocument findDidDoc(String did);

    void registerVcMeta(VcMeta vcMeta);

    VcMeta getVcMetaByVcId(String vcId);

    void updateVcStatus(String vcId, VcStatus status);
}
