package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.sdk.issuer.model.common.enums.OfferType;

import java.time.Instant;

public class SdkVcOffer {
    private Long id;
    private String offerId;
    private OfferType offerType;
    private String vcPlanId;
    private String did;
    private Instant validUntil;
    private Long transactionId;

    public SdkVcOffer() {
    }

    public SdkVcOffer(Long id, String offerId, OfferType offerType, String vcPlanId, String did, Instant validUntil,
            Long transactionId) {
        this.id = id;
        this.offerId = offerId;
        this.offerType = offerType;
        this.vcPlanId = vcPlanId;
        this.did = did;
        this.validUntil = validUntil;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SdkVcOffer{" +
                "id=" + id +
                ", offerId='" + offerId + '\'' +
                ", offerType=" + offerType +
                ", vcPlanId='" + vcPlanId + '\'' +
                ", did='" + did + '\'' +
                ", validUntil=" + validUntil +
                ", transactionId=" + transactionId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String offerId;
        private OfferType offerType;
        private String vcPlanId;
        private String did;
        private Instant validUntil;
        private Long transactionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder offerId(String offerId) {
            this.offerId = offerId;
            return this;
        }

        public Builder offerType(OfferType offerType) {
            this.offerType = offerType;
            return this;
        }

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder validUntil(Instant validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public SdkVcOffer build() {
            return new SdkVcOffer(id, offerId, offerType, vcPlanId, did, validUntil, transactionId);
        }
    }
}
