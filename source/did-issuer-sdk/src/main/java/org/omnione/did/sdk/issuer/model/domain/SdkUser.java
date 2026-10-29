package org.omnione.did.sdk.issuer.model.domain;

public class SdkUser {
    private Long id;
    private String pii;
    private String did;
    private Long vcSchemaId;
    private String data;

    public SdkUser() {
    }

    public SdkUser(Long id, String pii, String did, Long vcSchemaId, String data) {
        this.id = id;
        this.pii = pii;
        this.did = did;
        this.vcSchemaId = vcSchemaId;
        this.data = data;
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

    @Override
    public String toString() {
        return "SdkUser{" +
                "id=" + id +
                ", pii='" + pii + '\'' +
                ", did='" + did + '\'' +
                ", vcSchemaId=" + vcSchemaId +
                ", data='" + data + '\'' +
                '}';
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

        public SdkUser build() {
            return new SdkUser(id, pii, did, vcSchemaId, data);
        }
    }
}
