package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.common.vo.HeaderEntry;
import org.omnione.did.sdk.issuer.model.enums.InitiateType;
import org.omnione.did.sdk.issuer.model.enums.UserQueryType;
import org.omnione.did.sdk.issuer.model.enums.VcIssuanceMode;

import java.util.List;

public class SdkIssueProfile {
    private Long id;
    private Long vcSchemaId;
    private String vcPlanId;
    private String title;
    private String description;
    private List<String> endpoints;
    private String cipher;
    private String curve;
    private String padding;
    private String language;
    private VcIssuanceMode issuanceMode;
    private InitiateType initiateType;
    private List<String> tags;
    private Boolean zkpEnabled;
    private String definitionId;
    private UserQueryType userQueryType;
    private String userQueryUrl;
    private List<HeaderEntry> userQueryHeaders;

    public SdkIssueProfile() {
    }

    public SdkIssueProfile(Long id, Long vcSchemaId, String vcPlanId, String title, String description,
            List<String> endpoints, String cipher, String curve, String padding, String language,
            VcIssuanceMode issuanceMode, InitiateType initiateType, List<String> tags, Boolean zkpEnabled,
            String definitionId, UserQueryType userQueryType, String userQueryUrl, List<HeaderEntry> userQueryHeaders) {
        this.id = id;
        this.vcSchemaId = vcSchemaId;
        this.vcPlanId = vcPlanId;
        this.title = title;
        this.description = description;
        this.endpoints = endpoints;
        this.cipher = cipher;
        this.curve = curve;
        this.padding = padding;
        this.language = language;
        this.issuanceMode = issuanceMode;
        this.initiateType = initiateType;
        this.tags = tags;
        this.zkpEnabled = zkpEnabled;
        this.definitionId = definitionId;
        this.userQueryType = userQueryType;
        this.userQueryUrl = userQueryUrl;
        this.userQueryHeaders = userQueryHeaders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVcSchemaId() {
        return vcSchemaId;
    }

    public void setVcSchemaId(Long vcSchemaId) {
        this.vcSchemaId = vcSchemaId;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public VcIssuanceMode getIssuanceMode() {
        return issuanceMode;
    }

    public void setIssuanceMode(VcIssuanceMode issuanceMode) {
        this.issuanceMode = issuanceMode;
    }

    public InitiateType getInitiateType() {
        return initiateType;
    }

    public void setInitiateType(InitiateType initiateType) {
        this.initiateType = initiateType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getZkpEnabled() {
        return zkpEnabled;
    }

    public void setZkpEnabled(Boolean zkpEnabled) {
        this.zkpEnabled = zkpEnabled;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public UserQueryType getUserQueryType() {
        return userQueryType;
    }

    public void setUserQueryType(UserQueryType userQueryType) {
        this.userQueryType = userQueryType;
    }

    public String getUserQueryUrl() {
        return userQueryUrl;
    }

    public void setUserQueryUrl(String userQueryUrl) {
        this.userQueryUrl = userQueryUrl;
    }

    public List<HeaderEntry> getUserQueryHeaders() {
        return userQueryHeaders;
    }

    public void setUserQueryHeaders(List<HeaderEntry> userQueryHeaders) {
        this.userQueryHeaders = userQueryHeaders;
    }

    @Override
    public String toString() {
        return "SdkIssueProfile{" +
                "id=" + id +
                ", vcSchemaId=" + vcSchemaId +
                ", vcPlanId='" + vcPlanId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", endpoints=" + endpoints +
                ", cipher='" + cipher + '\'' +
                ", curve='" + curve + '\'' +
                ", padding='" + padding + '\'' +
                ", language='" + language + '\'' +
                ", issuanceMode=" + issuanceMode +
                ", initiateType=" + initiateType +
                ", tags=" + tags +
                ", zkpEnabled=" + zkpEnabled +
                ", definitionId='" + definitionId + '\'' +
                ", userQueryType=" + userQueryType +
                ", userQueryUrl='" + userQueryUrl + '\'' +
                ", userQueryHeaders=" + userQueryHeaders +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long vcSchemaId;
        private String vcPlanId;
        private String title;
        private String description;
        private List<String> endpoints;
        private String cipher;
        private String curve;
        private String padding;
        private String language;
        private VcIssuanceMode issuanceMode;
        private InitiateType initiateType;
        private List<String> tags;
        private Boolean zkpEnabled;
        private String definitionId;
        private UserQueryType userQueryType;
        private String userQueryUrl;
        private List<HeaderEntry> userQueryHeaders;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder vcSchemaId(Long vcSchemaId) {
            this.vcSchemaId = vcSchemaId;
            return this;
        }

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder endpoints(List<String> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public Builder cipher(String cipher) {
            this.cipher = cipher;
            return this;
        }

        public Builder curve(String curve) {
            this.curve = curve;
            return this;
        }

        public Builder padding(String padding) {
            this.padding = padding;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder issuanceMode(VcIssuanceMode issuanceMode) {
            this.issuanceMode = issuanceMode;
            return this;
        }

        public Builder initiateType(InitiateType initiateType) {
            this.initiateType = initiateType;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder zkpEnabled(Boolean zkpEnabled) {
            this.zkpEnabled = zkpEnabled;
            return this;
        }

        public Builder definitionId(String definitionId) {
            this.definitionId = definitionId;
            return this;
        }

        public Builder userQueryType(UserQueryType userQueryType) {
            this.userQueryType = userQueryType;
            return this;
        }

        public Builder userQueryUrl(String userQueryUrl) {
            this.userQueryUrl = userQueryUrl;
            return this;
        }

        public Builder userQueryHeaders(List<HeaderEntry> userQueryHeaders) {
            this.userQueryHeaders = userQueryHeaders;
            return this;
        }

        public SdkIssueProfile build() {
            return new SdkIssueProfile(id, vcSchemaId, vcPlanId, title, description, endpoints, cipher, curve, padding,
                    language, issuanceMode, initiateType, tags, zkpEnabled, definitionId, userQueryType, userQueryUrl,
                    userQueryHeaders);
        }
    }
}
