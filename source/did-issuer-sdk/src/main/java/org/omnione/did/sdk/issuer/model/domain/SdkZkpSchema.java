package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.sdk.issuer.model.common.enums.ZkpSchemaStatus;

public class SdkZkpSchema {
    private Long id;
    private String schemaId;
    private String name;
    private String version;
    private String tag;
    private ZkpSchemaStatus status;
    private String schema;

    public SdkZkpSchema() {
    }

    public SdkZkpSchema(Long id, String schemaId, String name, String version, String tag, ZkpSchemaStatus status,
            String schema) {
        this.id = id;
        this.schemaId = schemaId;
        this.name = name;
        this.version = version;
        this.tag = tag;
        this.status = status;
        this.schema = schema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ZkpSchemaStatus getStatus() {
        return status;
    }

    public void setStatus(ZkpSchemaStatus status) {
        this.status = status;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "SdkZkpSchema{" +
                "id=" + id +
                ", schemaId='" + schemaId + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", tag='" + tag + '\'' +
                ", status=" + status +
                ", schema='" + schema + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String schemaId;
        private String name;
        private String version;
        private String tag;
        private ZkpSchemaStatus status;
        private String schema;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder schemaId(String schemaId) {
            this.schemaId = schemaId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder status(ZkpSchemaStatus status) {
            this.status = status;
            return this;
        }

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public SdkZkpSchema build() {
            return new SdkZkpSchema(id, schemaId, name, version, tag, status, schema);
        }
    }
}
