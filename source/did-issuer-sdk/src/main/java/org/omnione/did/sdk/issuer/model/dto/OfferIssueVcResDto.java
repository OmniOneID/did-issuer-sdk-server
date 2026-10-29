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

import org.omnione.did.sdk.issuer.model.data.IssueOfferPayload;

/**
 * response offer for issuing a Verifiable Credential.
 */
public class OfferIssueVcResDto {
    private String txId;
    private IssueOfferPayload issueOfferPayload;

    public OfferIssueVcResDto() {
    }

    public OfferIssueVcResDto(String txId, IssueOfferPayload issueOfferPayload) {
        this.txId = txId;
        this.issueOfferPayload = issueOfferPayload;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public IssueOfferPayload getIssueOfferPayload() {
        return issueOfferPayload;
    }

    public void setIssueOfferPayload(IssueOfferPayload issueOfferPayload) {
        this.issueOfferPayload = issueOfferPayload;
    }

    @Override
    public String toString() {
        return "OfferIssueVcResDto{" +
                "txId='" + txId + '\'' +
                ", issueOfferPayload=" + issueOfferPayload +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private IssueOfferPayload issueOfferPayload;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder issueOfferPayload(IssueOfferPayload issueOfferPayload) {
            this.issueOfferPayload = issueOfferPayload;
            return this;
        }

        public OfferIssueVcResDto build() {
            return new OfferIssueVcResDto(txId, issueOfferPayload);
        }
    }
}
