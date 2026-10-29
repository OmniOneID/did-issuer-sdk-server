/*
 * Copyright 2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.sdk.issuer.model.data;

import org.omnione.did.sdk.issuer.model.data.zkp.CredentialDefinition;
import org.omnione.did.sdk.issuer.model.enums.VcIssuanceMode;

import org.omnione.did.data.model.vc.CredentialSchema;

import java.util.List;

/**
 * This class represents the VcPlan structure.
 */
public class VcPlan {
    private String vcPlanId;
    private String name;
    private String description;
    private String ref;
    private LogoImage logo;
    private String validFrom;
    private String validUntil;
    private List<String> tags;
    private CredentialSchema credentialSchema;
    private CredentialDefinition credentialDefinition;
    private Option option;
    private String delegator;
    private List<String> allowedIssuers;
    private String manager;
    private VcIssuanceMode issuanceMode;
    private List<String> endpoints;

    public VcPlan() {
    }

    public VcPlan(String vcPlanId, String name, String description, String ref, LogoImage logo, String validFrom,
            String validUntil, List<String> tags, CredentialSchema credentialSchema,
            CredentialDefinition credentialDefinition, Option option, String delegator, List<String> allowedIssuers,
            String manager, VcIssuanceMode issuanceMode, List<String> endpoints) {
        this.vcPlanId = vcPlanId;
        this.name = name;
        this.description = description;
        this.ref = ref;
        this.logo = logo;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.tags = tags;
        this.credentialSchema = credentialSchema;
        this.credentialDefinition = credentialDefinition;
        this.option = option;
        this.delegator = delegator;
        this.allowedIssuers = allowedIssuers;
        this.manager = manager;
        this.issuanceMode = issuanceMode;
        this.endpoints = endpoints;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public LogoImage getLogo() {
        return logo;
    }

    public void setLogo(LogoImage logo) {
        this.logo = logo;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public CredentialSchema getCredentialSchema() {
        return credentialSchema;
    }

    public void setCredentialSchema(CredentialSchema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }

    public CredentialDefinition getCredentialDefinition() {
        return credentialDefinition;
    }

    public void setCredentialDefinition(CredentialDefinition credentialDefinition) {
        this.credentialDefinition = credentialDefinition;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getDelegator() {
        return delegator;
    }

    public void setDelegator(String delegator) {
        this.delegator = delegator;
    }

    public List<String> getAllowedIssuers() {
        return allowedIssuers;
    }

    public void setAllowedIssuers(List<String> allowedIssuers) {
        this.allowedIssuers = allowedIssuers;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public VcIssuanceMode getIssuanceMode() {
        return issuanceMode;
    }

    public void setIssuanceMode(VcIssuanceMode issuanceMode) {
        this.issuanceMode = issuanceMode;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "VcPlan{" +
                "vcPlanId='" + vcPlanId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ref='" + ref + '\'' +
                ", logo=" + logo +
                ", validFrom='" + validFrom + '\'' +
                ", validUntil='" + validUntil + '\'' +
                ", tags=" + tags +
                ", credentialSchema=" + credentialSchema +
                ", credentialDefinition=" + credentialDefinition +
                ", option=" + option +
                ", delegator='" + delegator + '\'' +
                ", allowedIssuers=" + allowedIssuers +
                ", manager='" + manager + '\'' +
                ", issuanceMode=" + issuanceMode +
                ", endpoints=" + endpoints +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vcPlanId;
        private String name;
        private String description;
        private String ref;
        private LogoImage logo;
        private String validFrom;
        private String validUntil;
        private List<String> tags;
        private CredentialSchema credentialSchema;
        private CredentialDefinition credentialDefinition;
        private Option option;
        private String delegator;
        private List<String> allowedIssuers;
        private String manager;
        private VcIssuanceMode issuanceMode;
        private List<String> endpoints;

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder ref(String ref) {
            this.ref = ref;
            return this;
        }

        public Builder logo(LogoImage logo) {
            this.logo = logo;
            return this;
        }

        public Builder validFrom(String validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder validUntil(String validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder credentialSchema(CredentialSchema credentialSchema) {
            this.credentialSchema = credentialSchema;
            return this;
        }

        public Builder credentialDefinition(CredentialDefinition credentialDefinition) {
            this.credentialDefinition = credentialDefinition;
            return this;
        }

        public Builder option(Option option) {
            this.option = option;
            return this;
        }

        public Builder delegator(String delegator) {
            this.delegator = delegator;
            return this;
        }

        public Builder allowedIssuers(List<String> allowedIssuers) {
            this.allowedIssuers = allowedIssuers;
            return this;
        }

        public Builder manager(String manager) {
            this.manager = manager;
            return this;
        }

        public Builder issuanceMode(VcIssuanceMode issuanceMode) {
            this.issuanceMode = issuanceMode;
            return this;
        }

        public Builder endpoints(List<String> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public VcPlan build() {
            return new VcPlan(vcPlanId, name, description, ref, logo, validFrom, validUntil, tags, credentialSchema,
                    credentialDefinition, option, delegator, allowedIssuers, manager, issuanceMode, endpoints);
        }
    }
}
