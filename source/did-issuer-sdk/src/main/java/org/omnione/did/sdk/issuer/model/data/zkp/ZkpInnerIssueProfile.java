package org.omnione.did.sdk.issuer.model.data.zkp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.data.model.profile.issue.InnerIssueProfile;
import org.omnione.did.zkp.datamodel.credentialoffer.CredentialOffer;

public class ZkpInnerIssueProfile extends InnerIssueProfile {
    @SerializedName("credentialOffer")
    @Expose
    private CredentialOffer credentialOffer;

    public ZkpInnerIssueProfile() {
    }

    public ZkpInnerIssueProfile(CredentialOffer credentialOffer) {
        this.credentialOffer = credentialOffer;
    }

    public CredentialOffer getCredentialOffer() {
        return credentialOffer;
    }

    public void setCredentialOffer(CredentialOffer credentialOffer) {
        this.credentialOffer = credentialOffer;
    }

    @Override
    public String toString() {
        return "ZkpInnerIssueProfile{" +
                "credentialOffer=" + credentialOffer +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CredentialOffer credentialOffer;

        public Builder credentialOffer(CredentialOffer credentialOffer) {
            this.credentialOffer = credentialOffer;
            return this;
        }

        public ZkpInnerIssueProfile build() {
            return new ZkpInnerIssueProfile(credentialOffer);
        }
    }
}
