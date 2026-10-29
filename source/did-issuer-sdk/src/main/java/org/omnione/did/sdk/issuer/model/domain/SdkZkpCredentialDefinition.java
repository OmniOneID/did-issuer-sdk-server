package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.zkp.datamodel.enums.CredentialType;
import org.omnione.did.sdk.issuer.model.common.enums.ZkpCredentialDefinitionStatus;

public class SdkZkpCredentialDefinition {
    private Long id;
    private String definitionId;
    private String schemaId;
    private CredentialType type;
    private String alias;
    private String tag;
    private String version;
    private String definition;
    private Long zkpSchemaId;
    private ZkpCredentialDefinitionStatus status;

    public SdkZkpCredentialDefinition() {
    }

    public SdkZkpCredentialDefinition(Long id, String definitionId, String schemaId, CredentialType type, String alias,
            String tag, String version, String definition, Long zkpSchemaId, ZkpCredentialDefinitionStatus status) {
        this.id = id;
        this.definitionId = definitionId;
        this.schemaId = schemaId;
        this.type = type;
        this.alias = alias;
        this.tag = tag;
        this.version = version;
        this.definition = definition;
        this.zkpSchemaId = zkpSchemaId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public CredentialType getType() {
        return type;
    }

    public void setType(CredentialType type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Long getZkpSchemaId() {
        return zkpSchemaId;
    }

    public void setZkpSchemaId(Long zkpSchemaId) {
        this.zkpSchemaId = zkpSchemaId;
    }

    public ZkpCredentialDefinitionStatus getStatus() {
        return status;
    }

    public void setStatus(ZkpCredentialDefinitionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SdkZkpCredentialDefinition{" +
                "id=" + id +
                ", definitionId='" + definitionId + '\'' +
                ", schemaId='" + schemaId + '\'' +
                ", type=" + type +
                ", alias='" + alias + '\'' +
                ", tag='" + tag + '\'' +
                ", version='" + version + '\'' +
                ", definition='" + definition + '\'' +
                ", zkpSchemaId=" + zkpSchemaId +
                ", status=" + status +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String definitionId;
        private String schemaId;
        private CredentialType type;
        private String alias;
        private String tag;
        private String version;
        private String definition;
        private Long zkpSchemaId;
        private ZkpCredentialDefinitionStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder definitionId(String definitionId) {
            this.definitionId = definitionId;
            return this;
        }

        public Builder schemaId(String schemaId) {
            this.schemaId = schemaId;
            return this;
        }

        public Builder type(CredentialType type) {
            this.type = type;
            return this;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder definition(String definition) {
            this.definition = definition;
            return this;
        }

        public Builder zkpSchemaId(Long zkpSchemaId) {
            this.zkpSchemaId = zkpSchemaId;
            return this;
        }

        public Builder status(ZkpCredentialDefinitionStatus status) {
            this.status = status;
            return this;
        }

        public SdkZkpCredentialDefinition build() {
            return new SdkZkpCredentialDefinition(id, definitionId, schemaId, type, alias, tag, version, definition,
                    zkpSchemaId, status);
        }
    }
}
