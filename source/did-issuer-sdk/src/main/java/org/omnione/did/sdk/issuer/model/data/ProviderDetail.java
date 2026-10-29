/*
 * Copyright 2024 OmniOne.
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

/**
 * provider detail data structure.
 */
public class ProviderDetail {
    private String did;
    private String certVcRef;
    private String name;
    private String description;

    public ProviderDetail() {
    }

    public ProviderDetail(String did, String certVcRef, String name, String description) {
        this.did = did;
        this.certVcRef = certVcRef;
        this.name = name;
        this.description = description;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getCertVcRef() {
        return certVcRef;
    }

    public void setCertVcRef(String certVcRef) {
        this.certVcRef = certVcRef;
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

    @Override
    public String toString() {
        return "ProviderDetail{" +
                "did='" + did + '\'' +
                ", certVcRef='" + certVcRef + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String did;
        private String certVcRef;
        private String name;
        private String description;

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder certVcRef(String certVcRef) {
            this.certVcRef = certVcRef;
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

        public ProviderDetail build() {
            return new ProviderDetail(did, certVcRef, name, description);
        }
    }
}
