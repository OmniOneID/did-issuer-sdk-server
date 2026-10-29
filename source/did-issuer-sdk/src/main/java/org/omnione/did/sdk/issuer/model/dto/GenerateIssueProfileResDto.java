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

import org.omnione.did.sdk.issuer.model.data.zkp.ZkpIssueProfile;

/**
 * response for generate issue profile for a Verifiable Credential.
 */
public class GenerateIssueProfileResDto {
    private String txId;
    private ZkpIssueProfile profile;
    private String authNonce;

    public GenerateIssueProfileResDto() {
    }

    public GenerateIssueProfileResDto(String txId, ZkpIssueProfile profile, String authNonce) {
        this.txId = txId;
        this.profile = profile;
        this.authNonce = authNonce;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public ZkpIssueProfile getProfile() {
        return profile;
    }

    public void setProfile(ZkpIssueProfile profile) {
        this.profile = profile;
    }

    public String getAuthNonce() {
        return authNonce;
    }

    public void setAuthNonce(String authNonce) {
        this.authNonce = authNonce;
    }

    @Override
    public String toString() {
        return "GenerateIssueProfileResDto{" +
                "txId='" + txId + '\'' +
                ", profile=" + profile +
                ", authNonce='" + authNonce + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String txId;
        private ZkpIssueProfile profile;
        private String authNonce;

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder profile(ZkpIssueProfile profile) {
            this.profile = profile;
            return this;
        }

        public Builder authNonce(String authNonce) {
            this.authNonce = authNonce;
            return this;
        }

        public GenerateIssueProfileResDto build() {
            return new GenerateIssueProfileResDto(txId, profile, authNonce);
        }
    }
}
