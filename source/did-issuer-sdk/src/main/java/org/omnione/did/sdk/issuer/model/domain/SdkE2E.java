package org.omnione.did.sdk.issuer.model.domain;

public class SdkE2E {
    private Long id;
    private String sessionKey;
    private String nonce;
    private String curve;
    private String cipher;
    private String padding;
    private Long transactionId;

    public SdkE2E() {
    }

    public SdkE2E(Long id, String sessionKey, String nonce, String curve, String cipher, String padding,
            Long transactionId) {
        this.id = id;
        this.sessionKey = sessionKey;
        this.nonce = nonce;
        this.curve = curve;
        this.cipher = cipher;
        this.padding = padding;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SdkE2E{" +
                "id=" + id +
                ", sessionKey='" + sessionKey + '\'' +
                ", nonce='" + nonce + '\'' +
                ", curve='" + curve + '\'' +
                ", cipher='" + cipher + '\'' +
                ", padding='" + padding + '\'' +
                ", transactionId=" + transactionId +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String sessionKey;
        private String nonce;
        private String curve;
        private String cipher;
        private String padding;
        private Long transactionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder sessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
            return this;
        }

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder curve(String curve) {
            this.curve = curve;
            return this;
        }

        public Builder cipher(String cipher) {
            this.cipher = cipher;
            return this;
        }

        public Builder padding(String padding) {
            this.padding = padding;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public SdkE2E build() {
            return new SdkE2E(id, sessionKey, nonce, curve, cipher, padding, transactionId);
        }
    }
}
