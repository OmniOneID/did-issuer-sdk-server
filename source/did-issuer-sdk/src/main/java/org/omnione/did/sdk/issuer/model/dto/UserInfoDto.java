package org.omnione.did.sdk.issuer.model.dto;

import java.time.ZonedDateTime;

public class UserInfoDto {
    private Long id;

    private String pii;

    private String did;

    private Long vcSchemaId;

    private String data;

    private ZonedDateTime validFrom;

    private ZonedDateTime validUntil;

    public UserInfoDto() {
    }

    public UserInfoDto(Long id, String pii, String did, Long vcSchemaId, String data, ZonedDateTime validFrom,
            ZonedDateTime validUntil) {
        this.id = id;
        this.pii = pii;
        this.did = did;
        this.vcSchemaId = vcSchemaId;
        this.data = data;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPii() {
        return pii;
    }

    public void setPii(String pii) {
        this.pii = pii;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Long getVcSchemaId() {
        return vcSchemaId;
    }

    public void setVcSchemaId(Long vcSchemaId) {
        this.vcSchemaId = vcSchemaId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ZonedDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(ZonedDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public ZonedDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String pii;
        private String did;
        private Long vcSchemaId;
        private String data;
        private ZonedDateTime validFrom;
        private ZonedDateTime validUntil;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder pii(String pii) {
            this.pii = pii;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder vcSchemaId(Long vcSchemaId) {
            this.vcSchemaId = vcSchemaId;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public Builder validFrom(ZonedDateTime validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder validUntil(ZonedDateTime validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public UserInfoDto build() {
            return new UserInfoDto(id, pii, did, vcSchemaId, data, validFrom, validUntil);
        }
    }

}
