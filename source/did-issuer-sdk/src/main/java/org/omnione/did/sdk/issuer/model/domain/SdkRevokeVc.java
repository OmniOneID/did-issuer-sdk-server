package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.data.model.enums.vc.VcStatus;

public class SdkRevokeVc {
    private Long id;
    private String nonce;
    private String vcId;
    private VcStatus status;
    private Long transactionId;

    public SdkRevokeVc() {
    }

    public SdkRevokeVc(Long id, String nonce, String vcId, VcStatus status, Long transactionId) {
        this.id = id;
        this.nonce = nonce;
        this.vcId = vcId;
        this.status = status;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public VcStatus getStatus() {
        return status;
    }

    public void setStatus(VcStatus status) {
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
        return "SdkRevokeVc{" +
                "id=" + id +
                ", nonce='" + nonce + '\'' +
                ", vcId='" + vcId + '\'' +
                ", status=" + status +
                ", transactionId=" + transactionId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String nonce;
        private String vcId;
        private VcStatus status;
        private Long transactionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public Builder status(VcStatus status) {
            this.status = status;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public SdkRevokeVc build() {
            return new SdkRevokeVc(id, nonce, vcId, status, transactionId);
        }
    }
}
