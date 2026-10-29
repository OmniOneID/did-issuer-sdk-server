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

package org.omnione.did.sdk.issuer.exception;

/**
 * Exception thrown by the SDK.
 */
public class IssuerSdkException extends RuntimeException {

    private final IssuerSdkErrorCode errorCode;
    private final String detailMessage;
    private final Object[] args;

    public IssuerSdkException(IssuerSdkErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = null;
        this.args = new Object[0];
    }

    public IssuerSdkException(IssuerSdkErrorCode errorCode, Object[] args) {
        super(errorCode.getMessage() + ": " + arrayToString(args));
        this.errorCode = errorCode;
        this.detailMessage = null;
        this.args = args != null ? args : new Object[0];
    }

    public IssuerSdkException(IssuerSdkErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage() + " : " + detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
        this.args = new Object[]{detailMessage};
    }

    public IssuerSdkException(IssuerSdkErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detailMessage = cause.getMessage();
        this.args = new Object[0];
    }

    public IssuerSdkErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        if (detailMessage != null) {
            return errorCode.getMessage() + " : " + detailMessage;
        }
        return errorCode.getMessage();
    }

    public Object[] getArgs() {
        return args;
    }

    private static String arrayToString(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return java.util.Arrays.toString(args);
    }

    @Override
    public String toString() {
        return "IssuerSdkException{" +
                "errorCode=" + errorCode +
                ", detailMessage='" + detailMessage + '\'' +
                ", args=" + arrayToString(args) +
                '}';
    }
}
