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

import java.util.ArrayList;

/**
 * Represents the process data structure.
 */
public class Process {
    private String issuerNonce;
    private ReqE2e reqE2e;
    private ArrayList<String> endpoints;

    public Process() {
    }

    public Process(String issuerNonce, ReqE2e reqE2e, ArrayList<String> endpoints) {
        this.issuerNonce = issuerNonce;
        this.reqE2e = reqE2e;
        this.endpoints = endpoints;
    }

    public String getIssuerNonce() {
        return issuerNonce;
    }

    public void setIssuerNonce(String issuerNonce) {
        this.issuerNonce = issuerNonce;
    }

    public ReqE2e getReqE2e() {
        return reqE2e;
    }

    public void setReqE2e(ReqE2e reqE2e) {
        this.reqE2e = reqE2e;
    }

    public ArrayList<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(ArrayList<String> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "Process{" +
                "issuerNonce='" + issuerNonce + '\'' +
                ", reqE2e=" + reqE2e +
                ", endpoints=" + endpoints +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String issuerNonce;
        private ReqE2e reqE2e;
        private ArrayList<String> endpoints;

        public Builder issuerNonce(String issuerNonce) {
            this.issuerNonce = issuerNonce;
            return this;
        }

        public Builder reqE2e(ReqE2e reqE2e) {
            this.reqE2e = reqE2e;
            return this;
        }

        public Builder endpoints(ArrayList<String> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public Process build() {
            return new Process(issuerNonce, reqE2e, endpoints);
        }
    }
}
