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

import jakarta.validation.constraints.NotNull;
import org.omnione.did.data.model.did.Proof;

/**
 * Represents a DID authentication structure.
 */
public class DidAuth extends RequestProof {
    @NotNull(message = "didAuth.id cannot be null")
    private String did;
    @NotNull(message = "didAuth.authNonce cannot be null")
    private String authNonce;

    public DidAuth() {
    }

    public DidAuth(Proof proof, String did, String authNonce) {
        super(proof);
        this.did = did;
        this.authNonce = authNonce;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getAuthNonce() {
        return authNonce;
    }

    public void setAuthNonce(String authNonce) {
        this.authNonce = authNonce;
    }

    @Override
    public String toString() {
        return "DidAuth{" +
                "did='" + did + '\'' +
                ", authNonce='" + authNonce + '\'' +
                ", proof=" + getProof() +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RequestProof.Builder<DidAuth, Builder> {
        private String did;
        private String authNonce;

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder authNonce(String authNonce) {
            this.authNonce = authNonce;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DidAuth build() {
            return new DidAuth(proof, did, authNonce);
        }
    }
}