package org.omnione.did.sdk.issuer.model.data.zkp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;

import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.profile.MetaProfile;
import org.omnione.did.data.model.util.json.GsonWrapper;

/**
 * Description...
 */

public class ZkpIssueProfile extends MetaProfile {
    @SerializedName("profile")
    @Expose
    private ZkpInnerIssueProfile profile;
    @SerializedName("proof")
    @Expose
    private @NotNull Proof proof;

    public ZkpIssueProfile() {
    }

    public ZkpIssueProfile(ZkpInnerIssueProfile profile, Proof proof) {
        this.profile = profile;
        this.proof = proof;
    }

    public ZkpInnerIssueProfile getProfile() {
        return profile;
    }

    public void setProfile(ZkpInnerIssueProfile profile) {
        this.profile = profile;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "ZkpIssueProfile{" +
                "profile=" + profile +
                ", proof=" + proof +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ZkpInnerIssueProfile profile;
        private Proof proof;

        public Builder profile(ZkpInnerIssueProfile profile) {
            this.profile = profile;
            return this;
        }

        public Builder proof(Proof proof) {
            this.proof = proof;
            return this;
        }

        public ZkpIssueProfile build() {
            return new ZkpIssueProfile(profile, proof);
        }
    }

    @Override
    public void fromJson(String val) {
        super.fromJson(val);
        GsonWrapper gson = new GsonWrapper();
        ZkpIssueProfile data = gson.fromJson(val, ZkpIssueProfile.class);
        this.profile = data.profile;
        this.proof = data.proof;
    }
}
