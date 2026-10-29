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

package org.omnione.did.sdk.issuer.model.dto;

import org.omnione.did.sdk.issuer.model.enums.VerifyAuthType;

/**
 * response for inspect propose revocation vc.
 */
public class InspectProposeRevokeResDto {
    private String txId;
    private String issuerNonce;
    private Integer authType;

    public InspectProposeRevokeResDto() {
    }

    public InspectProposeRevokeResDto(String txId, String issuerNonce, VerifyAuthType authType) {
        this.txId = txId;
        this.issuerNonce = issuerNonce;
        this.authType = authType.getValue();
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getIssuerNonce() {
        return issuerNonce;
    }

    public void setIssuerNonce(String issuerNonce) {
        this.issuerNonce = issuerNonce;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(VerifyAuthType authType) {
        this.authType = authType.getValue();
    }

    @Override
    public String toString() {
        return "InspectProposeRevokeResDto{" +
                "txId='" + txId + '\'' +
                ", issuerNonce='" + issuerNonce + '\'' +
                ", authType=" + authType +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private String issuerNonce;
        private VerifyAuthType authType;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder issuerNonce(String issuerNonce) {
            this.issuerNonce = issuerNonce;
            return this;
        }

        public Builder authType(VerifyAuthType authType) {
            this.authType = authType;
            return this;
        }

        public InspectProposeRevokeResDto build() {
            return new InspectProposeRevokeResDto(txId, issuerNonce, authType);
        }
    }
}
