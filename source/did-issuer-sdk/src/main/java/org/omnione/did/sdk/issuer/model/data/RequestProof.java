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

import org.omnione.did.data.model.did.Proof;

/**
 * Represents the request proof data structure.
 */
public abstract class RequestProof {
    private Proof proof;

    public RequestProof() {
    }

    public RequestProof(Proof proof) {
        this.proof = proof;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "RequestProof{" +
                "proof=" + proof +
                '}';
    }

    public abstract static class Builder<C extends RequestProof, B extends Builder<C, B>> {
        protected Proof proof;

        public B proof(Proof proof) {
            this.proof = proof;
            return self();
        }

        protected abstract B self();

        public abstract C build();
    }
}
