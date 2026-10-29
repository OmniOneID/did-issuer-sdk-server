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
 * Enumeration of error codes used in the SDK.
 */
public enum IssuerSdkErrorCode {

    // Transaction (001xx)
    TR_VC_OFFER_FAILED("00100", "Failed to process the 'request-offer' API request.", 500),
    TR_VC_ISSUE_PROPOSE_FAILED("00101", "Failed to process the 'inspect-propose-issue' API request.", 500),
    TR_VC_ISSUE_PROFILE_FAILED("00102", "Failed to process the 'generate-issue-profile' API request.", 500),
    TR_VC_ISSUE_FAILED("00103", "Failed to process the 'issue-vc' API request.", 500),
    TR_VC_ISSUE_COMPLETE_FAILED("00104", "Failed to process the 'complete-vc' API request.", 500),
    TR_VC_ISSUE_RESULT_FAILED("00105", "Failed to process the 'issue-vc-result' API request.", 500),
    TR_VC_REVOKE_PROPOSE_FAILED("00110", "Failed to process the 'inspect-propose-revoke' API request.", 500),
    TR_VC_REVOKE_FAILED("00111", "Failed to process the 'revoke-vc' API request.", 500),
    TR_VC_UPDATE_STATUS_FAILED("00112", "Failed to update VC status.", 500),
    TR_VC_REVOKE_COMPLETE_FAILED("00113", "Failed to process the 'complete-revoke' API request.", 500),

    // DID (002xx)
    DID_DOC_NOT_FOUND("00200", "DID Document not found.", 400),
    FAILED_TO_GENERATE_DID_DOCUMENT("00201", "Failed to generate DID document.", 500),

    // VC (003xx)
    VC_OFFER_EXPIRED("00300", "The Offer has expired.", 400),
    VC_PROFILE_INVALID("00301", "The profile id is not valid.", 400),
    VC_PROFILE_ISSUER_NONCE_INVALID("00302", "The profile issuer nonce is not valid.", 400),
    VC_ISSUE_FAILED("00303", "Failed to Issue VC", 500),
    VC_ID_NOT_MATCH("00304", "VC ID does not match.", 400),
    ISSUER_NONCE_INVALID("00305", "The issuer nonce is not valid.", 400),
    VC_GENERATION_FAILED("00306", "Failed to generate VC", 500),

    // SDK Generic (004xx)
    CRYPTO_ENCODING_FAILED("00400", "Failed to encoding data.", 500),
    CRYPTO_DECODING_FAILED("00401", "Failed to decoding data.", 400),
    CRYPTO_ENCRYPTION_FAILED("00402", "Failed to encrypt data.", 500),
    CRYPTO_DECRYPTION_FAILED("00403", "Failed to decrypt data.", 500),
    CRYPTO_NONCE_GENERATION_FAILED("00404", "Failed to generate nonce.", 500),
    CRYPTO_INITIAL_VECTOR_GENERATION_FAILED("00405", "Failed to generate initial vector.", 500),
    CRYPTO_SESSION_KEY_GENERATION_FAILED("00406", "Failed to generate session key.", 500),
    CRYPTO_NONCE_AND_SHARED_SECRET_MERGE_FAILED("00407", "Failed to merge nonce and shared secret.", 500),
    CRYPTO_NONCE_MERGE_FAILED("00408", "Failed to merge nonce.", 500),
    CRYPTO_SYMMETRIC_CIPHER_TYPE_INVALID("00409", "Invalid symmetric cipher type.", 500),
    CRYPTO_KEY_PAIR_GENERATION_FAILED("00410", "Failed to generate key pair.", 500),
    CRYPTO_PUBLIC_KEY_COMPRESS_FAILED("00411", "Failed to compress public key.", 500),
    SIGNATURE_VERIFICATION_FAILED("00412", "Failed to Signature verification.", 400),
    SIGNATURE_GENERATION_FAILED("00413", "Failed to generate signature.", 500),
    HASH_GENERATION_FAILED("00414", "Failed to generate hash value.", 500),
    FAILED_TO_GET_FILE_WALLET_MANAGER("00415", "Failed to get File wallet manager", 500),
    WALLET_ALREADY_EXISTS("00416", "Failed to create wallet: wallet already exists.", 500),
    CRYPTO_KEY_PAIR_ALREADY_EXISTS("00417", "Failed to generate keys: key already exists.", 500),
    INVALID_KEY_TYPE("00418", "Invalid Key Type.", 400),
    GET_SIGN_KEY_IDS_FAILED("00419", "Failed to get sign key ids", 400),
    GET_SIGN_DATA_FAILED("00420", "Failed to get sign data", 400),
    FAILED_TO_LOAD_KEY_ELEMENT("00421", "Failed to load key element.", 500),
    WALLET_CONNECT_FAILURE("00422", "Failed to connect wallet.", 500),

    // ZKP Errors (005xx)
    ZKP_WALLET_CONNECT_FAILURE("00500", "Failed to connect ZKP wallet.", 500),
    ZKP_WALLET_ALREADY_EXISTS("00501", "Failed to create ZKP wallet: ZKP wallet already exists.", 500),
    FAILED_TO_GENERATE_CREDENTIAL_OFFER("00502", "Failed to Create Credential Offer.", 500),
    FAILED_TO_GENERATE_ZKP_ATTRIBUTE_VALUE("00503", "Failed to Create Attribute Value.", 500),
    FAILED_TO_ISSUE_CREDENTIAL("00504", "Failed to Issued Credential.", 500),

    // Blockchain (006xx)
    BLOCKCHAIN_DIDDOC_REGISTRATION_FAILED("00600", "Failed to register DID Document on the blockchain.", 500),
    BLOCKCHAIN_GET_DID_DOC_FAILED("00601", "Failed to retrieve DID document on the blockchain.", 500),
    BLOCKCHAIN_VC_META_REGISTRATION_FAILED("00602", "Failed to register VC meta on the blockchain.", 500),
    BLOCKCHAIN_VC_META_RETRIEVAL_FAILED("00603", "Failed to retrieve VC meta on the blockchain.", 500),
    BLOCKCHAIN_VC_STATUS_UPDATE_FAILED("00604", "Failed to update VC status on the blockchain.", 500),

    // Etc (007xx)
    INVALID_DATE_FORMAT("00700", "Invalid Date Format.", 500),
    REF_ID_INVALID("00701", "The refId is not valid.", 400),

    // Auth / Token (008xx)
    INVALID_TOKEN("00800", "Invalid Access Token.", 400),
    JWT_TOKEN_EXPIRED("00801", "JWT token has expired", 401),

    // Generic
    UNKNOWN_SERVER_ERROR("99999", "An unknown server error.", 500),

    // Legacy / Added during refactor
    TRANSACTION_NOT_FOUND("00106", "The transaction does not exist.", 400),
    TRANSACTION_INVALID("00107", "The transaction is not valid.", 400),
    TRANSACTION_EXPIRED("00108", "The transaction has expired.", 400),
    REF_ID_INVALID_LEGACY("00109", "The refId is not valid.", 400),

    // DID
    DID_DOC_VERSION_INVALID("00202", "Invalid DID Document version.", 400),
    INVALID_DID_DOCUMENT("00203", "Invalid DID Document", 400),
    DID_DOCUMENT_RETRIEVAL_FAILED("00204", "Failed to retrieve DID Document.", 500),

    // VC
    VC_OFFER_NOT_FOUND("00310", "The Offer does not exist.", 400),
    VC_PLAN_ID_INVALID("00311", "The vcPlanId is not valid.", 400),
    VC_NOT_FOUND("00312", "The VC does not exist.", 400),
    PARSE_REQUEST_VC_FAILURE("00313", "Failed to parse Request VC.", 400),
    REVOKED_VC("00314", "This VC is Revoked.", 400),
    VC_SCHEMA_NAME_INVALID("00315", "VC Schema name is not valid", 400),
    VC_SCHEMA_PARSE_FAILED("00316", "Failed to parse VC Schema", 500),
    VC_SCHEMA_NOT_FOUND("00317", "VC Schema not found for given ID.", 400),
    VC_SCHEMA_NOT_UNIQUE("00318", "VC Schema ID does not Unique", 500),
    AUTH_NONCE_INVALID("00319", "The Auth nonce is not valid.", 400),

    // Holder
    HOLDER_NOT_FOUND("00350", "The Holder does not exist.", 400),
    USER_NOT_FOUND("00351", "The User dose not exist.", 400),
    HOLDER_INVALID("00352", "The Holder is not valid.", 400),

    // Crypto
    CRYPTO_PUBLIC_KEY_UN_COMPRESS_FAILED("00430", "Failed to uncompress public key.", 500),

    // Wallet
    WALLET_INFO_NOT_FOUND("00440", "Wallet is not registered.", 400),
    WALLET_CREATION_FAILURE("00441", "Failed to create wallet.", 500),

    INVALID_PROOF_PURPOSE("00450", "Invalid proof purpose.", 400),
    NO_SUCH_ALGORITHM("00499", "No such algorithm.", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    IssuerSdkErrorCode(String code, String message, int httpStatus) {
        this.code = "S" + "SDK" + "ISS" + code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static String getMessageByCode(String code) {
        for (IssuerSdkErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode.getMessage();
            }
        }
        return "Unknown error code: " + code;
    }
}
