/*
 * Copyright 2025 OmniOne.
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

/**
 * This class represents the Option structure.
 */
public class Option {
    private Boolean allowUserInit;
    private Boolean allowIssuerInit;
    private Boolean delegatedIssuance;

    public Option() {
    }

    public Option(Boolean allowUserInit, Boolean allowIssuerInit, Boolean delegatedIssuance) {
        this.allowUserInit = allowUserInit;
        this.allowIssuerInit = allowIssuerInit;
        this.delegatedIssuance = delegatedIssuance;
    }

    public Boolean getAllowUserInit() {
        return allowUserInit;
    }

    public void setAllowUserInit(Boolean allowUserInit) {
        this.allowUserInit = allowUserInit;
    }

    public Boolean getAllowIssuerInit() {
        return allowIssuerInit;
    }

    public void setAllowIssuerInit(Boolean allowIssuerInit) {
        this.allowIssuerInit = allowIssuerInit;
    }

    public Boolean getDelegatedIssuance() {
        return delegatedIssuance;
    }

    public void setDelegatedIssuance(Boolean delegatedIssuance) {
        this.delegatedIssuance = delegatedIssuance;
    }

    @Override
    public String toString() {
        return "Option{" +
                "allowUserInit=" + allowUserInit +
                ", allowIssuerInit=" + allowIssuerInit +
                ", delegatedIssuance=" + delegatedIssuance +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean allowUserInit;
        private Boolean allowIssuerInit;
        private Boolean delegatedIssuance;

        public Builder allowUserInit(Boolean allowUserInit) {
            this.allowUserInit = allowUserInit;
            return this;
        }

        public Builder allowIssuerInit(Boolean allowIssuerInit) {
            this.allowIssuerInit = allowIssuerInit;
            return this;
        }

        public Builder delegatedIssuance(Boolean delegatedIssuance) {
            this.delegatedIssuance = delegatedIssuance;
            return this;
        }

        public Option build() {
            return new Option(allowUserInit, allowIssuerInit, delegatedIssuance);
        }
    }
}
