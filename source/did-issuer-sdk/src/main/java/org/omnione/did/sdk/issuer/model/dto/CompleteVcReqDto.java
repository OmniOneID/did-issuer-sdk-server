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
 * request for completing the issuance of a VC.
 */
public class CompleteVcReqDto {
    private String id;
    private String txId;
    private String vcId;

    public CompleteVcReqDto() {
    }

    public CompleteVcReqDto(String id, String txId, String vcId) {
        this.id = id;
        this.txId = txId;
        this.vcId = vcId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    @Override
    public String toString() {
        return "CompleteVcReqDto{" +
                "id='" + id + '\'' +
                ", txId='" + txId + '\'' +
                ", vcId='" + vcId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String txId;
        private String vcId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public CompleteVcReqDto build() {
            return new CompleteVcReqDto(id, txId, vcId);
        }
    }
}
