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

import org.omnione.did.sdk.issuer.model.data.E2e;

/**
 * response for issue VC.
 */
public class IssueVcResDto {
    private String txId;
    private E2e e2e;

    public IssueVcResDto() {
    }

    public IssueVcResDto(String txId, E2e e2e) {
        this.txId = txId;
        this.e2e = e2e;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public E2e getE2e() {
        return e2e;
    }

    public void setE2e(E2e e2e) {
        this.e2e = e2e;
    }

    @Override
    public String toString() {
        return "IssueVcResDto{" +
                "txId='" + txId + '\'' +
                ", e2e=" + e2e +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private E2e e2e;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder e2e(E2e e2e) {
            this.e2e = e2e;
            return this;
        }

        public IssueVcResDto build() {
            return new IssueVcResDto(txId, e2e);
        }
    }
}
