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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.omnione.did.sdk.issuer.model.common.enums.EccCurveType;
import org.omnione.did.data.model.DataObject;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.util.json.GsonWrapper;

/**
 * Represents the ECDH (Elliptic Curve Diffie-Hellman) request data structure.
 */
public class EcdhReqData extends DataObject {
    @NotNull(message = "reqEcdh.client cannot be null")
    private String client;
    @NotNull(message = "reqEcdh.clientNonce cannot be null")
    private String clientNonce;
    @NotNull(message = "reqEcdh.curve cannot be null")
    private EccCurveType curve;
    @NotNull(message = "reqEcdh.publicKey cannot be null")
    private String publicKey;
    @Valid
    private Candidate candidate;
    @Valid
    private Proof proof;

    public EcdhReqData() {
    }

    public EcdhReqData(String client, String clientNonce, EccCurveType curve, String publicKey, Candidate candidate,
            Proof proof) {
        this.client = client;
        this.clientNonce = clientNonce;
        this.curve = curve;
        this.publicKey = publicKey;
        this.candidate = candidate;
        this.proof = proof;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientNonce() {
        return clientNonce;
    }

    public void setClientNonce(String clientNonce) {
        this.clientNonce = clientNonce;
    }

    public EccCurveType getCurve() {
        return curve;
    }

    public void setCurve(EccCurveType curve) {
        this.curve = curve;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "EcdhReqData{" +
                "client='" + client + '\'' +
                ", clientNonce='" + clientNonce + '\'' +
                ", curve=" + curve +
                ", publicKey='" + publicKey + '\'' +
                ", candidate=" + candidate +
                ", proof=" + proof +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String client;
        private String clientNonce;
        private EccCurveType curve;
        private String publicKey;
        private Candidate candidate;
        private Proof proof;

        public Builder client(String client) {
            this.client = client;
            return this;
        }

        public Builder clientNonce(String clientNonce) {
            this.clientNonce = clientNonce;
            return this;
        }

        public Builder curve(EccCurveType curve) {
            this.curve = curve;
            return this;
        }

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder candidate(Candidate candidate) {
            this.candidate = candidate;
            return this;
        }

        public Builder proof(Proof proof) {
            this.proof = proof;
            return this;
        }

        public EcdhReqData build() {
            return new EcdhReqData(client, clientNonce, curve, publicKey, candidate, proof);
        }
    }

    @Override
    public void fromJson(String s) {
        GsonWrapper gson = new GsonWrapper();
        EcdhReqData reqData = gson.fromJson(s, this.getClass());

        this.client = reqData.client;
        this.clientNonce = reqData.clientNonce;
        this.curve = reqData.curve;
        this.publicKey = reqData.publicKey;
        this.candidate = reqData.candidate;
        this.proof = reqData.proof;
    }
}
