package org.omnione.did.sdk.issuer.model.domain;

public class SdkVcProfile {
    private Long id;
    private String profileId;
    private String did;
    private String nonce;
    private String zkpNonce;
    private Long userId;
    private Long transactionId;

    public SdkVcProfile() {
    }

    public SdkVcProfile(Long id, String profileId, String did, String nonce, String zkpNonce, Long userId,
            Long transactionId) {
        this.id = id;
        this.profileId = profileId;
        this.did = did;
        this.nonce = nonce;
        this.zkpNonce = zkpNonce;
        this.userId = userId;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getZkpNonce() {
        return zkpNonce;
    }

    public void setZkpNonce(String zkpNonce) {
        this.zkpNonce = zkpNonce;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SdkVcProfile{" +
                "id=" + id +
                ", profileId='" + profileId + '\'' +
                ", did='" + did + '\'' +
                ", nonce='" + nonce + '\'' +
                ", zkpNonce='" + zkpNonce + '\'' +
                ", userId=" + userId +
                ", transactionId=" + transactionId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String profileId;
        private String did;
        private String nonce;
        private String zkpNonce;
        private Long userId;
        private Long transactionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder profileId(String profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder zkpNonce(String zkpNonce) {
            this.zkpNonce = zkpNonce;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public SdkVcProfile build() {
            return new SdkVcProfile(id, profileId, did, nonce, zkpNonce, userId, transactionId);
        }
    }
}
