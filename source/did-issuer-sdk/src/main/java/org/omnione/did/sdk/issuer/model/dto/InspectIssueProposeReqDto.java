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
 * request for inspecting the propose issue for a Verifiable Credential.
 */
public class InspectIssueProposeReqDto {
    private String id;
    private String vcPlanId;
    private String issuer;
    private String offerId;

    public InspectIssueProposeReqDto() {
    }

    public InspectIssueProposeReqDto(String id, String vcPlanId, String issuer, String offerId) {
        this.id = id;
        this.vcPlanId = vcPlanId;
        this.issuer = issuer;
        this.offerId = offerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "InspectIssueProposeReqDto{" +
                "id='" + id + '\'' +
                ", vcPlanId='" + vcPlanId + '\'' +
                ", issuer='" + issuer + '\'' +
                ", offerId='" + offerId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String vcPlanId;
        private String issuer;
        private String offerId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder vcPlanId(String vcPlanId) {
            this.vcPlanId = vcPlanId;
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder offerId(String offerId) {
            this.offerId = offerId;
            return this;
        }

        public InspectIssueProposeReqDto build() {
            return new InspectIssueProposeReqDto(id, vcPlanId, issuer, offerId);
        }
    }
}
