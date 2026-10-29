package org.omnione.did.sdk.issuer.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

/**
 * Description...
 */
public enum InitiateType {
    @SerializedName("user_init")
    USER_INIT("user_init"),

    @SerializedName("issuer_init")
    ISSUER_INIT("issuer_init");

    @JsonValue
    private String type;

    InitiateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
