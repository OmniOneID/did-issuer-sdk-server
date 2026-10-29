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

import jakarta.validation.constraints.NotNull;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.sdk.issuer.model.enums.VcStatus;

/**
 * Data structure for signed vc status.
 */
public class SignedVcStatus {
    @NotNull(message = "vcId cannot be null")
    private String vcId;
    @NotNull(message = "vcStatus cannot be null")
    private VcStatus vcStatus;
    @NotNull
    private Proof proof;

    public SignedVcStatus() {
    }

    public SignedVcStatus(String vcId, VcStatus vcStatus, Proof proof) {
        this.vcId = vcId;
        this.vcStatus = vcStatus;
        this.proof = proof;
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

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "SignedVcStatus{" +
                "vcId='" + vcId + '\'' +
                ", vcStatus=" + vcStatus +
                ", proof=" + proof +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vcId;
        private VcStatus vcStatus;
        private Proof proof;

        public Builder vcId(String vcId) {
            this.vcId = vcId;
            return this;
        }

        public Builder vcStatus(VcStatus vcStatus) {
            this.vcStatus = vcStatus;
            return this;
        }

        public Builder proof(Proof proof) {
            this.proof = proof;
            return this;
        }

        public SignedVcStatus build() {
            return new SignedVcStatus(vcId, vcStatus, proof);
        }
    }
}
