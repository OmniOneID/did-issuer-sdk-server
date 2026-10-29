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

import com.google.gson.annotations.Expose;

import org.omnione.did.data.model.DataObject;
import org.omnione.did.data.model.util.json.GsonWrapper;
import org.omnione.did.zkp.datamodel.credentialrequest.CredentialRequest;

/**
 * request vc data structure.
 */
public class ReqVc extends DataObject {
    @Expose
    private String refId;
    @Expose
    private ProfileInfo profile;
    @Expose
    private CredentialRequest credentialRequest;

    public ReqVc() {
    }

    public ReqVc(String refId, ProfileInfo profile, CredentialRequest credentialRequest) {
        this.refId = refId;
        this.profile = profile;
        this.credentialRequest = credentialRequest;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public ProfileInfo getProfile() {
        return profile;
    }

    public void setProfile(ProfileInfo profile) {
        this.profile = profile;
    }

    public CredentialRequest getCredentialRequest() {
        return credentialRequest;
    }

    public void setCredentialRequest(CredentialRequest credentialRequest) {
        this.credentialRequest = credentialRequest;
    }

    @Override
    public String toString() {
        return "ReqVc{" +
                "refId='" + refId + '\'' +
                ", profile=" + profile +
                ", credentialRequest=" + credentialRequest +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String refId;
        private ProfileInfo profile;
        private CredentialRequest credentialRequest;

        public Builder refId(String refId) {
            this.refId = refId;
            return this;
        }

        public Builder profile(ProfileInfo profile) {
            this.profile = profile;
            return this;
        }

        public Builder credentialRequest(CredentialRequest credentialRequest) {
            this.credentialRequest = credentialRequest;
            return this;
        }

        public ReqVc build() {
            return new ReqVc(refId, profile, credentialRequest);
        }
    }

    @Override
    public void fromJson(String data) {
        GsonWrapper gson = new GsonWrapper();
        ReqVc reqVc = gson.fromJson(data, ReqVc.class);

        this.refId = reqVc.refId;
        this.profile = reqVc.profile;
        this.credentialRequest = reqVc.credentialRequest;
    }
}
