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

import org.omnione.did.data.model.util.json.GsonWrapper;
import org.omnione.did.data.model.did.Proof;

/**
 * Represents the Accept E2E (End-to-End) data structure.
 */
public class AccE2e extends RequestProof {
    private String publicKey;
    private String iv;

    public AccE2e() {
    }

    public AccE2e(Proof proof, String publicKey, String iv) {
        super(proof);
        this.publicKey = publicKey;
        this.iv = iv;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public String toString() {
        return "AccE2e{" +
                "publicKey='" + publicKey + '\'' +
                ", iv='" + iv + '\'' +
                ", proof=" + getProof() +
                '}';
    }

    public void fromJson(String json) {
        GsonWrapper gson = new GsonWrapper();
        AccE2e accE2e = gson.fromJson(json, AccE2e.class);

        this.publicKey = accE2e.getPublicKey();
        this.iv = accE2e.getIv();
        this.setProof(accE2e.getProof());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RequestProof.Builder<AccE2e, Builder> {
        private String publicKey;
        private String iv;

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder iv(String iv) {
            this.iv = iv;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AccE2e build() {
            return new AccE2e(proof, publicKey, iv);
        }
    }
}
