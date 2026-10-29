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

package org.omnione.did.sdk.issuer.model.data;

import com.google.gson.annotations.Expose;

/**
 * Represents the profile info data structure.
 */
public class ProfileInfo {
    @Expose
    private String id;
    @Expose
    private String issuerNonce;

    public ProfileInfo() {
    }

    public ProfileInfo(String id, String issuerNonce) {
        this.id = id;
        this.issuerNonce = issuerNonce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuerNonce() {
        return issuerNonce;
    }

    public void setIssuerNonce(String issuerNonce) {
        this.issuerNonce = issuerNonce;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "id='" + id + '\'' +
                ", issuerNonce='" + issuerNonce + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String issuerNonce;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder issuerNonce(String issuerNonce) {
            this.issuerNonce = issuerNonce;
            return this;
        }

        public ProfileInfo build() {
            return new ProfileInfo(id, issuerNonce);
        }
    }
}
