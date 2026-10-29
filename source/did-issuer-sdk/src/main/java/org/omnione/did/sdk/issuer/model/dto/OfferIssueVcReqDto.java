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

import jakarta.validation.constraints.NotNull;

/**
 * request offer for issuing a Verifiable Credential.
 */
public class OfferIssueVcReqDto {
    @NotNull
    private String vcPlanId;

    public OfferIssueVcReqDto() {
    }

    public OfferIssueVcReqDto(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    @Override
    public String toString() {
        return "OfferIssueVcReqDto{" +
                "vcPlanId='" + vcPlanId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vcPlanId;

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public OfferIssueVcReqDto build() {
            return new OfferIssueVcReqDto(vcPlanId);
        }
    }
}
