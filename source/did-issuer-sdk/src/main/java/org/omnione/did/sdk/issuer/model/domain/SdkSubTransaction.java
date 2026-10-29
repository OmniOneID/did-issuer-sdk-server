package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.sdk.issuer.model.common.enums.SubTransactionStatus;
import org.omnione.did.sdk.issuer.model.common.enums.SubTransactionType;

public class SdkSubTransaction {
    private Long id;
    private Integer step;
    private SubTransactionType type;
    private SubTransactionStatus status;
    private Long transactionId;

    public SdkSubTransaction() {
    }

    public SdkSubTransaction(Long id, Integer step, SubTransactionType type, SubTransactionStatus status,
            Long transactionId) {
        this.id = id;
        this.step = step;
        this.type = type;
        this.status = status;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public SubTransactionType getType() {
        return type;
    }

    public void setType(SubTransactionType type) {
        this.type = type;
    }

    public SubTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(SubTransactionStatus status) {
        this.status = status;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SdkSubTransaction{" +
                "id=" + id +
                ", step=" + step +
                ", type=" + type +
                ", status=" + status +
                ", transactionId=" + transactionId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Integer step;
        private SubTransactionType type;
        private SubTransactionStatus status;
        private Long transactionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder step(Integer step) {
            this.step = step;
            return this;
        }

        public Builder type(SubTransactionType type) {
            this.type = type;
            return this;
        }

        public Builder status(SubTransactionStatus status) {
            this.status = status;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public SdkSubTransaction build() {
            return new SdkSubTransaction(id, step, type, status, transactionId);
        }
    }
}
