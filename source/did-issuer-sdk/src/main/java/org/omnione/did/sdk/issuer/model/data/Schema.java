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

import org.omnione.did.sdk.issuer.model.enums.CredentialSchemaType;

/**
 * Schema data structure.
 */
public class Schema {
    private String id;
    private CredentialSchemaType type;
    private String value;

    public Schema() {
    }

    public Schema(String id, CredentialSchemaType type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CredentialSchemaType getType() {
        return type;
    }

    public void setType(CredentialSchemaType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private CredentialSchemaType type;
        private String value;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(CredentialSchemaType type) {
            this.type = type;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Schema build() {
            return new Schema(id, type, value);
        }
    }
}
