package org.omnione.did.sdk.issuer.model.data.zkp;

import com.google.gson.annotations.Expose;

import org.omnione.did.data.model.DataObject;
import org.omnione.did.data.model.util.json.GsonWrapper;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.zkp.datamodel.credential.Credential;

/**
 * Description...
 */
public class CredentialInfo extends DataObject {
    @Expose
    private VerifiableCredential vc;
    @Expose
    private Credential credential;

    public CredentialInfo() {
    }

    public CredentialInfo(VerifiableCredential vc, Credential credential) {
        this.vc = vc;
        this.credential = credential;
    }

    public VerifiableCredential getVc() {
        return vc;
    }

    public void setVc(VerifiableCredential vc) {
        this.vc = vc;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "CredentialInfo{" +
                "vc=" + vc +
                ", credential=" + credential +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private VerifiableCredential vc;
        private Credential credential;

        public Builder vc(VerifiableCredential vc) {
            this.vc = vc;
            return this;
        }

        public Builder credential(Credential credential) {
            this.credential = credential;
            return this;
        }

        public CredentialInfo build() {
            return new CredentialInfo(vc, credential);
        }
    }

    @Override
    public void fromJson(String val) {
        GsonWrapper gson = new GsonWrapper();
        CredentialInfo data = gson.fromJson(val, CredentialInfo.class);
        this.vc = data.vc;
        this.credential = data.credential;
    }
}
