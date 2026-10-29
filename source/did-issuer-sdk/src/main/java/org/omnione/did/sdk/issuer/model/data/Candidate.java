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

import org.omnione.did.sdk.issuer.model.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.issuer.model.validation.NonEmptyList;

import java.util.List;

/**
 * Represents the candidate data structure.
 */
public class Candidate {
    @NonEmptyList(message = "candidate.ciphers must not be empty")
    private List<SymmetricCipherType> ciphers;

    public Candidate() {
    }

    public Candidate(List<SymmetricCipherType> ciphers) {
        this.ciphers = ciphers;
    }

    public List<SymmetricCipherType> getCiphers() {
        return ciphers;
    }

    public void setCiphers(List<SymmetricCipherType> ciphers) {
        this.ciphers = ciphers;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "ciphers=" + ciphers +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<SymmetricCipherType> ciphers;

        public Builder ciphers(List<SymmetricCipherType> ciphers) {
            this.ciphers = ciphers;
            return this;
        }

        public Candidate build() {
            return new Candidate(ciphers);
        }
    }
}
