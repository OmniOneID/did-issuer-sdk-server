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
import org.omnione.did.data.model.util.json.GsonWrapper;

/**
 * Represents the req_revoke_vc data structure.
 */
public class ReqRevokeVc extends RequestProof {
    private String vcId;
    private String issuerNonce;

    public ReqRevokeVc() {
    }

    public ReqRevokeVc(Proof proof, String vcId, String issuerNonce) {
        super(proof);
        this.vcId = vcId;
        this.issuerNonce = issuerNonce;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public String getIssuerNonce() {
        return issuerNonce;
    }

    public void setIssuerNonce(String issuerNonce) {
        this.issuerNonce = issuerNonce;
    }

    @Override
    public String toString() {
        return "ReqRevokeVc{" +
                "vcId='" + vcId + '\'' +
                ", issuerNonce='" + issuerNonce + '\'' +
                ", proof=" + getProof() +
                '}';
    }

    public void fromJson(String json) {
        GsonWrapper gson = new GsonWrapper();
        ReqRevokeVc reqRevokeVc = gson.fromJson(json, ReqRevokeVc.class);

        this.vcId = reqRevokeVc.vcId;
        this.issuerNonce = reqRevokeVc.issuerNonce;
        this.setProof(reqRevokeVc.getProof());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RequestProof.Builder<ReqRevokeVc, Builder> {
        private String vcId;
        private String issuerNonce;

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public Builder issuerNonce(String issuerNonce) {
            this.issuerNonce = issuerNonce;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ReqRevokeVc build() {
            return new ReqRevokeVc(proof, vcId, issuerNonce);
        }
    }
}
