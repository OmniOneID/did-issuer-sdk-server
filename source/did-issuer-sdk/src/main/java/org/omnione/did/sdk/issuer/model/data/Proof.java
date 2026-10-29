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

/**
 * Represents the proof data structure.
 */
public class Proof {
    private String type;
    private String created;
    private String verificationMethod;
    private String proofPurpose;
    private String proofValue;

    public Proof() {
    }

    public Proof(String type, String created, String verificationMethod, String proofPurpose, String proofValue) {
        this.type = type;
        this.created = created;
        this.verificationMethod = verificationMethod;
        this.proofPurpose = proofPurpose;
        this.proofValue = proofValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public String getProofPurpose() {
        return proofPurpose;
    }

    public void setProofPurpose(String proofPurpose) {
        this.proofPurpose = proofPurpose;
    }

    public String getProofValue() {
        return proofValue;
    }

    public void setProofValue(String proofValue) {
        this.proofValue = proofValue;
    }

    @Override
    public String toString() {
        return "Proof{" +
                "type='" + type + '\'' +
                ", created='" + created + '\'' +
                ", verificationMethod='" + verificationMethod + '\'' +
                ", proofPurpose='" + proofPurpose + '\'' +
                ", proofValue='" + proofValue + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String created;
        private String verificationMethod;
        private String proofPurpose;
        private String proofValue;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder created(String created) {
            this.created = created;
            return this;
        }

        public Builder verificationMethod(String verificationMethod) {
            this.verificationMethod = verificationMethod;
            return this;
        }

        public Builder proofPurpose(String proofPurpose) {
            this.proofPurpose = proofPurpose;
            return this;
        }

        public Builder proofValue(String proofValue) {
            this.proofValue = proofValue;
            return this;
        }

        public Proof build() {
            return new Proof(type, created, verificationMethod, proofPurpose, proofValue);
        }
    }
}
