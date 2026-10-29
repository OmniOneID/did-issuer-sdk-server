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

package org.omnione.did.sdk.issuer.model.data.zkp;

/**
 * This class represents the CredentialSchema structure.
 */
public class CredentialDefinition {
    private String id;
    private String schemaId;

    public CredentialDefinition() {
    }

    public CredentialDefinition(String id, String schemaId) {
        this.id = id;
        this.schemaId = schemaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    @Override
    public String toString() {
        return "CredentialDefinition{" +
                "id='" + id + '\'' +
                ", schemaId='" + schemaId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String schemaId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder schemaId(String schemaId) {
            this.schemaId = schemaId;
            return this;
        }

        public CredentialDefinition build() {
            return new CredentialDefinition(id, schemaId);
        }
    }
}
