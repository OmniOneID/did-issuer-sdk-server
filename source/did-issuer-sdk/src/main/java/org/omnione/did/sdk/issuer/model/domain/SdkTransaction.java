package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.sdk.issuer.model.common.enums.TransactionStatus;
import org.omnione.did.sdk.issuer.model.common.enums.TransactionType;

import java.time.Instant;

public class SdkTransaction {
    private Long id;
    private String txId;
    private String refId;
    private TransactionType type;
    private TransactionStatus status;
    private String vcPlanId;
    private Instant expiredAt;
    private Long issueProfileId;

    public SdkTransaction() {
    }

    public SdkTransaction(Long id, String txId, String refId, TransactionType type, TransactionStatus status,
            String vcPlanId, Instant expiredAt, Long issueProfileId) {
        this.id = id;
        this.txId = txId;
        this.refId = refId;
        this.type = type;
        this.status = status;
        this.vcPlanId = vcPlanId;
        this.expiredAt = expiredAt;
        this.issueProfileId = issueProfileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Long getIssueProfileId() {
        return issueProfileId;
    }

    public void setIssueProfileId(Long issueProfileId) {
        this.issueProfileId = issueProfileId;
    }

    @Override
    public String toString() {
        return "SdkTransaction{" +
                "id=" + id +
                ", txId='" + txId + '\'' +
                ", refId='" + refId + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", vcPlanId='" + vcPlanId + '\'' +
                ", expiredAt=" + expiredAt +
                ", issueProfileId=" + issueProfileId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String txId;
        private String refId;
        private TransactionType type;
        private TransactionStatus status;
        private String vcPlanId;
        private Instant expiredAt;
        private Long issueProfileId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder refId(String refId) {
            this.refId = refId;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder status(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder expiredAt(Instant expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public Builder issueProfileId(Long issueProfileId) {
            this.issueProfileId = issueProfileId;
            return this;
        }

        public SdkTransaction build() {
            return new SdkTransaction(id, txId, refId, type, status, vcPlanId, expiredAt, issueProfileId);
        }
    }
}
