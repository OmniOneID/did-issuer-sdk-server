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

import org.omnione.did.sdk.issuer.model.data.ReqRevokeVc;

/**
 * request for revoking a Verifiable Credential.
 */
public class RevokeVcReqDto {
    private String id;
    private String txId;
    private ReqRevokeVc request;

    public RevokeVcReqDto() {
    }

    public RevokeVcReqDto(String id, String txId, ReqRevokeVc request) {
        this.id = id;
        this.txId = txId;
        this.request = request;
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

    public ReqRevokeVc getRequest() {
        return request;
    }

    public void setRequest(ReqRevokeVc request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "RevokeVcReqDto{" +
                "id='" + id + '\'' +
                ", txId='" + txId + '\'' +
                ", request=" + request +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String txId;
        private ReqRevokeVc request;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder request(ReqRevokeVc request) {
            this.request = request;
            return this;
        }

        public RevokeVcReqDto build() {
            return new RevokeVcReqDto(id, txId, request);
        }
    }
}
