package org.omnione.did.sdk.issuer.model.domain;

import org.omnione.did.sdk.issuer.model.common.enums.IssuerStatus;

public class SdkIssuerInfo {
    private Long id;
    private String did;
    private String name;
    private IssuerStatus status;
    private String serverUrl;
    private String certificateUrl;
    private String publishedCertificateUrl;

    public SdkIssuerInfo() {
    }

    public SdkIssuerInfo(Long id, String did, String name, IssuerStatus status, String serverUrl, String certificateUrl,
            String publishedCertificateUrl) {
        this.id = id;
        this.did = did;
        this.name = name;
        this.status = status;
        this.serverUrl = serverUrl;
        this.certificateUrl = certificateUrl;
        this.publishedCertificateUrl = publishedCertificateUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IssuerStatus getStatus() {
        return status;
    }

    public void setStatus(IssuerStatus status) {
        this.status = status;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getPublishedCertificateUrl() {
        return publishedCertificateUrl;
    }

    public void setPublishedCertificateUrl(String publishedCertificateUrl) {
        this.publishedCertificateUrl = publishedCertificateUrl;
    }

    @Override
    public String toString() {
        return "SdkIssuerInfo{" +
                "id=" + id +
                ", did='" + did + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", serverUrl='" + serverUrl + '\'' +
                ", certificateUrl='" + certificateUrl + '\'' +
                ", publishedCertificateUrl='" + publishedCertificateUrl + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String did;
        private String name;
        private IssuerStatus status;
        private String serverUrl;
        private String certificateUrl;
        private String publishedCertificateUrl;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder status(IssuerStatus status) {
            this.status = status;
            return this;
        }

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder certificateUrl(String certificateUrl) {
            this.certificateUrl = certificateUrl;
            return this;
        }

        public Builder publishedCertificateUrl(String publishedCertificateUrl) {
            this.publishedCertificateUrl = publishedCertificateUrl;
            return this;
        }

        public SdkIssuerInfo build() {
            return new SdkIssuerInfo(id, did, name, status, serverUrl, certificateUrl, publishedCertificateUrl);
        }
    }
}
