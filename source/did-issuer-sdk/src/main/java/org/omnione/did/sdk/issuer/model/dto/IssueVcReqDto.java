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

import org.omnione.did.sdk.issuer.model.data.AccE2e;
import org.omnione.did.sdk.issuer.model.data.DidAuth;

/**
 * request for issue VC.
 */
public class IssueVcReqDto {
    private String id;
    private String txId;
    private AccE2e accE2e;
    private String encReqVc;
    private DidAuth didAuth;

    public IssueVcReqDto() {
    }

    public IssueVcReqDto(String id, String txId, AccE2e accE2e, String encReqVc, DidAuth didAuth) {
        this.id = id;
        this.txId = txId;
        this.accE2e = accE2e;
        this.encReqVc = encReqVc;
        this.didAuth = didAuth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public AccE2e getAccE2e() {
        return accE2e;
    }

    public void setAccE2e(AccE2e accE2e) {
        this.accE2e = accE2e;
    }

    public String getEncReqVc() {
        return encReqVc;
    }

    public void setEncReqVc(String encReqVc) {
        this.encReqVc = encReqVc;
    }

    public DidAuth getDidAuth() {
        return didAuth;
    }

    public void setDidAuth(DidAuth didAuth) {
        this.didAuth = didAuth;
    }

    @Override
    public String toString() {
        return "IssueVcReqDto{" +
                "id='" + id + '\'' +
                ", txId='" + txId + '\'' +
                ", accE2e=" + accE2e +
                ", encReqVc='" + encReqVc + '\'' +
                ", didAuth=" + didAuth +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String txId;
        private AccE2e accE2e;
        private String encReqVc;
        private DidAuth didAuth;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder txId(String txId) {
            this.txId = txId;
            return this;
        }

        public Builder accE2e(AccE2e accE2e) {
            this.accE2e = accE2e;
            return this;
        }

        public Builder encReqVc(String encReqVc) {
            this.encReqVc = encReqVc;
            return this;
        }

        public Builder didAuth(DidAuth didAuth) {
            this.didAuth = didAuth;
            return this;
        }

        public IssueVcReqDto build() {
            return new IssueVcReqDto(id, txId, accE2e, encReqVc, didAuth);
        }
    }
}
