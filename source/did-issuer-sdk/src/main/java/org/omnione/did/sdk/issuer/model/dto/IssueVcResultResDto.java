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
 * request for issue vc result.
 */
public class IssueVcResultResDto {
    private String txId;
    private String offerId;
    private Boolean result;

    public IssueVcResultResDto() {
    }

    public IssueVcResultResDto(String txId, String offerId, Boolean result) {
        this.txId = txId;
        this.offerId = offerId;
        this.result = result;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "IssueVcResultResDto{" +
                "txId='" + txId + '\'' +
                ", offerId='" + offerId + '\'' +
                ", result=" + result +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private String offerId;
        private Boolean result;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder offerId(String offerId) {
            this.offerId = offerId;
            return this;
        }

        public Builder result(Boolean result) {
            this.result = result;
            return this;
        }

        public IssueVcResultResDto build() {
            return new IssueVcResultResDto(txId, offerId, result);
        }
    }
}
