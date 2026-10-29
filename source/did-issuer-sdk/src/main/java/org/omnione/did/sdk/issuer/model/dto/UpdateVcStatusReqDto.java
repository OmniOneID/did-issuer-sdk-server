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

import org.omnione.did.data.model.enums.vc.VcStatus;

/**
 * request to update the status of a Verifiable Credential.
 */
public class UpdateVcStatusReqDto {
    @NotNull(message = "vcId cannot be null")
    private String vcId;
    @NotNull(message = "vcStatus cannot be null")
    private VcStatus vcStatus;

    public UpdateVcStatusReqDto() {
    }

    public UpdateVcStatusReqDto(String vcId, VcStatus vcStatus) {
        this.vcId = vcId;
        this.vcStatus = vcStatus;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public VcStatus getVcStatus() {
        return vcStatus;
    }

    public void setVcStatus(VcStatus vcStatus) {
        this.vcStatus = vcStatus;
    }

    @Override
    public String toString() {
        return "UpdateVcStatusReqDto{" +
                "vcId='" + vcId + '\'' +
                ", vcStatus=" + vcStatus +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vcId;
        private VcStatus vcStatus;

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public Builder vcStatus(VcStatus vcStatus) {
            this.vcStatus = vcStatus;
            return this;
        }

        public UpdateVcStatusReqDto build() {
            return new UpdateVcStatusReqDto(vcId, vcStatus);
        }
    }
}
