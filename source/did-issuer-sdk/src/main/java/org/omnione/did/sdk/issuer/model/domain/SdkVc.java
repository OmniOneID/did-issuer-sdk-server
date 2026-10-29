package org.omnione.did.sdk.issuer.model.domain;

import java.time.Instant;

public class SdkVc {
    private Long id;
    private String did;
    private String vcId;
    private String txId;
    private Instant issuedAt;
    private Instant expiredAt;
    private String vcPlanId;
    private Long userId;
    private String vcSchemaId;
    private String status;
    private String vcType;

    public SdkVc() {
    }

    public SdkVc(Long id, String did, String vcId, String txId, Instant issuedAt, Instant expiredAt, String vcPlanId,
            Long userId, String vcSchemaId, String status, String vcType) {
        this.id = id;
        this.did = did;
        this.vcId = vcId;
        this.txId = txId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.vcPlanId = vcPlanId;
        this.userId = userId;
        this.vcSchemaId = vcSchemaId;
        this.status = status;
        this.vcType = vcType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVcSchemaId() {
        return vcSchemaId;
    }

    public void setVcSchemaId(String vcSchemaId) {
        this.vcSchemaId = vcSchemaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVcType() {
        return vcType;
    }

    public void setVcType(String vcType) {
        this.vcType = vcType;
    }

    @Override
    public String toString() {
        return "SdkVc{" +
                "id=" + id +
                ", did='" + did + '\'' +
                ", vcId='" + vcId + '\'' +
                ", txId='" + txId + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiredAt=" + expiredAt +
                ", vcPlanId='" + vcPlanId + '\'' +
                ", userId=" + userId +
                ", vcSchemaId='" + vcSchemaId + '\'' +
                ", status='" + status + '\'' +
                ", vcType='" + vcType + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String did;
        private String vcId;
        private String txId;
        private Instant issuedAt;
        private Instant expiredAt;
        private String vcPlanId;
        private Long userId;
        private String vcSchemaId;
        private String status;
        private String vcType;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder issuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder expiredAt(Instant expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder vcSchemaId(String vcSchemaId) {
            this.vcSchemaId = vcSchemaId;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder vcType(String vcType) {
            this.vcType = vcType;
            return this;
        }

        public SdkVc build() {
            return new SdkVc(id, did, vcId, txId, issuedAt, expiredAt, vcPlanId, userId, vcSchemaId, status, vcType);
        }
    }
}
