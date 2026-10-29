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
 * Represents the vc_profile data structure.
 */
public class Profile {
    private ProviderDetail issuer;
    private Schema credentialSchema;
    private Process process;

    public Profile() {
    }

    public Profile(ProviderDetail issuer, Schema credentialSchema, Process process) {
        this.issuer = issuer;
        this.credentialSchema = credentialSchema;
        this.process = process;
    }

    public ProviderDetail getIssuer() {
        return issuer;
    }

    public void setIssuer(ProviderDetail issuer) {
        this.issuer = issuer;
    }

    public Schema getCredentialSchema() {
        return credentialSchema;
    }

    public void setCredentialSchema(Schema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "issuer=" + issuer +
                ", credentialSchema=" + credentialSchema +
                ", process=" + process +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProviderDetail issuer;
        private Schema credentialSchema;
        private Process process;

        public Builder issuer(ProviderDetail issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder credentialSchema(Schema credentialSchema) {
            this.credentialSchema = credentialSchema;
            return this;
        }

        public Builder process(Process process) {
            this.process = process;
            return this;
        }

        public Profile build() {
            return new Profile(issuer, credentialSchema, process);
        }
    }
}
