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

import org.omnione.did.sdk.issuer.model.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.issuer.model.common.enums.SymmetricPaddingType;

import org.omnione.did.data.model.did.Proof;

/**
 * Represents the Accept E2E (End-to-End) data structure.
 */
public class AccEcdh {
    private String server;
    private String serverNonce;
    private String publicKey;
    private SymmetricCipherType cipher;
    private SymmetricPaddingType padding;
    private Proof proof;

    public AccEcdh() {
    }

    public AccEcdh(String server, String serverNonce, String publicKey, SymmetricCipherType cipher,
            SymmetricPaddingType padding, Proof proof) {
        this.server = server;
        this.serverNonce = serverNonce;
        this.publicKey = publicKey;
        this.cipher = cipher;
        this.padding = padding;
        this.proof = proof;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerNonce() {
        return serverNonce;
    }

    public void setServerNonce(String serverNonce) {
        this.serverNonce = serverNonce;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public SymmetricCipherType getCipher() {
        return cipher;
    }

    public void setCipher(SymmetricCipherType cipher) {
        this.cipher = cipher;
    }

    public SymmetricPaddingType getPadding() {
        return padding;
    }

    public void setPadding(SymmetricPaddingType padding) {
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
        return "AccEcdh{" +
                "server='" + server + '\'' +
                ", serverNonce='" + serverNonce + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", cipher=" + cipher +
                ", padding=" + padding +
                ", proof=" + proof +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String server;
        private String serverNonce;
        private String publicKey;
        private SymmetricCipherType cipher;
        private SymmetricPaddingType padding;
        private Proof proof;

        public Builder server(String server) {
            this.server = server;
            return this;
        }

        public Builder serverNonce(String serverNonce) {
            this.serverNonce = serverNonce;
            return this;
        }

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder cipher(SymmetricCipherType cipher) {
            this.cipher = cipher;
            return this;
        }

        public Builder padding(SymmetricPaddingType padding) {
            this.padding = padding;
            return this;
        }

        public Builder proof(Proof proof) {
            this.proof = proof;
            return this;
        }

        public AccEcdh build() {
            return new AccEcdh(server, serverNonce, publicKey, cipher, padding, proof);
        }
    }
}
