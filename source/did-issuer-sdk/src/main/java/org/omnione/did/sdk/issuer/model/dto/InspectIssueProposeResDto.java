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

/**
 * response for inspecting the propose issue for a Verifiable Credential.
 */
public class InspectIssueProposeResDto {
    private String txId;
    private String refId;

    public InspectIssueProposeResDto() {
    }

    public InspectIssueProposeResDto(String txId, String refId) {
        this.txId = txId;
        this.refId = refId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "InspectIssueProposeResDto{" +
                "txId='" + txId + '\'' +
                ", refId='" + refId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private String refId;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder refId(String refId) {
            this.refId = refId;
            return this;
        }

        public InspectIssueProposeResDto build() {
            return new InspectIssueProposeResDto(txId, refId);
        }
    }
}
