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
 * request e2e data structure.
 */
public class ReqE2e {
    private String nonce;
    private String curve;
    private String publicKey;
    private String cipher;
    private String padding;
    private Proof proof;

    public ReqE2e() {
    }

    public ReqE2e(String nonce, String curve, String publicKey, String cipher, String padding, Proof proof) {
        this.nonce = nonce;
        this.curve = curve;
        this.publicKey = publicKey;
        this.cipher = cipher;
        this.padding = padding;
        this.proof = proof;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "ReqE2e{" +
                "nonce='" + nonce + '\'' +
                ", curve='" + curve + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", cipher='" + cipher + '\'' +
                ", padding='" + padding + '\'' +
                ", proof=" + proof +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String nonce;
        private String curve;
        private String publicKey;
        private String cipher;
        private String padding;
        private Proof proof;

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder curve(String curve) {
            this.curve = curve;
            return this;
        }

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder cipher(String cipher) {
            this.cipher = cipher;
            return this;
        }

        public Builder padding(String padding) {
            this.padding = padding;
            return this;
        }

        public Builder proof(Proof proof) {
            this.proof = proof;
            return this;
        }

        public ReqE2e build() {
            return new ReqE2e(nonce, curve, publicKey, cipher, padding, proof);
        }
    }
}
