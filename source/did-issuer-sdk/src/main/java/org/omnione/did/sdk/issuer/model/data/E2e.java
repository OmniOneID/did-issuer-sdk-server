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
 * Represents the e2e data structure.
 */
public class E2e {
    private String iv;
    private String encVc;

    public E2e() {
    }

    public E2e(String iv, String encVc) {
        this.iv = iv;
        this.encVc = encVc;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncVc() {
        return encVc;
    }

    public void setEncVc(String encVc) {
        this.encVc = encVc;
    }

    @Override
    public String toString() {
        return "E2e{" +
                "iv='" + iv + '\'' +
                ", encVc='" + encVc + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String iv;
        private String encVc;

        public Builder iv(String iv) {
            this.iv = iv;
            return this;
        }

        public Builder encVc(String encVc) {
            this.encVc = encVc;
            return this;
        }

        public E2e build() {
            return new E2e(iv, encVc);
        }
    }
}
