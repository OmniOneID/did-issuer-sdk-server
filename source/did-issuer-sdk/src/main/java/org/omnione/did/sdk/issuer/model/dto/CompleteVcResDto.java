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
 * response for completing the issuance of a VC.
 */
public class CompleteVcResDto {
    private String txId;

    public CompleteVcResDto() {
    }

    public CompleteVcResDto(String txId) {
        this.txId = txId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    @Override
    public String toString() {
        return "CompleteVcResDto{" +
                "txId='" + txId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public CompleteVcResDto build() {
            return new CompleteVcResDto(txId);
        }
    }
}
