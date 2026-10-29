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

import org.omnione.did.sdk.issuer.model.data.Holder;

/**
 * request for generate issue profile for a Verifiable Credential.
 */
public class GenerateIssueProfileReqDto {
    private String id;
    private String txId;
    private String userId;
    private Holder holder;

    public GenerateIssueProfileReqDto() {
    }

    public GenerateIssueProfileReqDto(String id, String txId, Holder holder) {
        this.id = id;
        this.txId = txId;
        this.holder = holder;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return "GenerateIssueProfileReqDto{" +
                "id='" + id + '\'' +
                ", txId='" + txId + '\'' +
                ", holder=" + holder +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String txId;
        private String userId;
        private Holder holder;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder holder(Holder holder) {
            this.holder = holder;
            return this;
        }

        public GenerateIssueProfileReqDto build() {
            GenerateIssueProfileReqDto request = new GenerateIssueProfileReqDto(id, txId, holder);
            request.setUserId(userId);
            return request;
        }
    }
}
