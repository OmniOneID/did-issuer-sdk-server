---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
---

IssuerSdkErrorCode
==

- Topic: Issuer SDK error codes
- Date: 2026-10-29
- Version: v1.0.0

| Version | Date | Changes |
| --- | --- | --- |
| v1.0.0 | 2026-10-29 | Complete SDK error code reference |

<div style="page-break-after: always;"></div>

# Table of Contents
- [Model](#model)
- [Error codes](#error-codes)

# Model

`IssuerSdkException#getCode()` returns the code from `IssuerSdkErrorCode`. Codes use the `SSDKISS` prefix followed by five digits. `IssuerSdkErrorCode#getHttpStatus()` supplies a status value for host transport mappings.

| Property | Type | Description |
| --- | --- | --- |
| `code` | `String` | `SSDKISS` plus five digits |
| `message` | `String` | SDK error message |
| `httpStatus` | `int` | Suggested HTTP status for host applications |

# Error codes

## Transactions

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00100` | `TR_VC_OFFER_FAILED` | Failed to process the 'request-offer' API request. | 500 |
| `SSDKISS00101` | `TR_VC_ISSUE_PROPOSE_FAILED` | Failed to process the 'inspect-propose-issue' API request. | 500 |
| `SSDKISS00102` | `TR_VC_ISSUE_PROFILE_FAILED` | Failed to process the 'generate-issue-profile' API request. | 500 |
| `SSDKISS00103` | `TR_VC_ISSUE_FAILED` | Failed to process the 'issue-vc' API request. | 500 |
| `SSDKISS00104` | `TR_VC_ISSUE_COMPLETE_FAILED` | Failed to process the 'complete-vc' API request. | 500 |
| `SSDKISS00105` | `TR_VC_ISSUE_RESULT_FAILED` | Failed to process the 'issue-vc-result' API request. | 500 |
| `SSDKISS00106` | `TRANSACTION_NOT_FOUND` | The transaction does not exist. | 400 |
| `SSDKISS00107` | `TRANSACTION_INVALID` | The transaction is not valid. | 400 |
| `SSDKISS00108` | `TRANSACTION_EXPIRED` | The transaction has expired. | 400 |
| `SSDKISS00109` | `REF_ID_INVALID_LEGACY` | The refId is not valid. | 400 |
| `SSDKISS00110` | `TR_VC_REVOKE_PROPOSE_FAILED` | Failed to process the 'inspect-propose-revoke' API request. | 500 |
| `SSDKISS00111` | `TR_VC_REVOKE_FAILED` | Failed to process the 'revoke-vc' API request. | 500 |
| `SSDKISS00112` | `TR_VC_UPDATE_STATUS_FAILED` | Failed to update VC status. | 500 |
| `SSDKISS00113` | `TR_VC_REVOKE_COMPLETE_FAILED` | Failed to process the 'complete-revoke' API request. | 500 |

## DID documents

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00200` | `DID_DOC_NOT_FOUND` | DID Document not found. | 400 |
| `SSDKISS00201` | `FAILED_TO_GENERATE_DID_DOCUMENT` | Failed to generate DID document. | 500 |
| `SSDKISS00202` | `DID_DOC_VERSION_INVALID` | Invalid DID Document version. | 400 |
| `SSDKISS00203` | `INVALID_DID_DOCUMENT` | Invalid DID Document | 400 |
| `SSDKISS00204` | `DID_DOCUMENT_RETRIEVAL_FAILED` | Failed to retrieve DID Document. | 500 |

## VCs and users

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00300` | `VC_OFFER_EXPIRED` | The Offer has expired. | 400 |
| `SSDKISS00301` | `VC_PROFILE_INVALID` | The profile id is not valid. | 400 |
| `SSDKISS00302` | `VC_PROFILE_ISSUER_NONCE_INVALID` | The profile issuer nonce is not valid. | 400 |
| `SSDKISS00303` | `VC_ISSUE_FAILED` | Failed to Issue VC | 500 |
| `SSDKISS00304` | `VC_ID_NOT_MATCH` | VC ID does not match. | 400 |
| `SSDKISS00305` | `ISSUER_NONCE_INVALID` | The issuer nonce is not valid. | 400 |
| `SSDKISS00306` | `VC_GENERATION_FAILED` | Failed to generate VC | 500 |
| `SSDKISS00310` | `VC_OFFER_NOT_FOUND` | The Offer does not exist. | 400 |
| `SSDKISS00311` | `VC_PLAN_ID_INVALID` | The vcPlanId is not valid. | 400 |
| `SSDKISS00312` | `VC_NOT_FOUND` | The VC does not exist. | 400 |
| `SSDKISS00313` | `PARSE_REQUEST_VC_FAILURE` | Failed to parse Request VC. | 400 |
| `SSDKISS00314` | `REVOKED_VC` | This VC is Revoked. | 400 |
| `SSDKISS00315` | `VC_SCHEMA_NAME_INVALID` | VC Schema name is not valid | 400 |
| `SSDKISS00316` | `VC_SCHEMA_PARSE_FAILED` | Failed to parse VC Schema | 500 |
| `SSDKISS00317` | `VC_SCHEMA_NOT_FOUND` | VC Schema not found for given ID. | 400 |
| `SSDKISS00318` | `VC_SCHEMA_NOT_UNIQUE` | VC Schema ID does not Unique | 500 |
| `SSDKISS00319` | `AUTH_NONCE_INVALID` | The Auth nonce is not valid. | 400 |
| `SSDKISS00350` | `HOLDER_NOT_FOUND` | The Holder does not exist. | 400 |
| `SSDKISS00351` | `USER_NOT_FOUND` | The User dose not exist. | 400 |
| `SSDKISS00352` | `HOLDER_INVALID` | The Holder is not valid. | 400 |

## Cryptography and wallets

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00400` | `CRYPTO_ENCODING_FAILED` | Failed to encoding data. | 500 |
| `SSDKISS00401` | `CRYPTO_DECODING_FAILED` | Failed to decoding data. | 400 |
| `SSDKISS00402` | `CRYPTO_ENCRYPTION_FAILED` | Failed to encrypt data. | 500 |
| `SSDKISS00403` | `CRYPTO_DECRYPTION_FAILED` | Failed to decrypt data. | 500 |
| `SSDKISS00404` | `CRYPTO_NONCE_GENERATION_FAILED` | Failed to generate nonce. | 500 |
| `SSDKISS00405` | `CRYPTO_INITIAL_VECTOR_GENERATION_FAILED` | Failed to generate initial vector. | 500 |
| `SSDKISS00406` | `CRYPTO_SESSION_KEY_GENERATION_FAILED` | Failed to generate session key. | 500 |
| `SSDKISS00407` | `CRYPTO_NONCE_AND_SHARED_SECRET_MERGE_FAILED` | Failed to merge nonce and shared secret. | 500 |
| `SSDKISS00408` | `CRYPTO_NONCE_MERGE_FAILED` | Failed to merge nonce. | 500 |
| `SSDKISS00409` | `CRYPTO_SYMMETRIC_CIPHER_TYPE_INVALID` | Invalid symmetric cipher type. | 500 |
| `SSDKISS00410` | `CRYPTO_KEY_PAIR_GENERATION_FAILED` | Failed to generate key pair. | 500 |
| `SSDKISS00411` | `CRYPTO_PUBLIC_KEY_COMPRESS_FAILED` | Failed to compress public key. | 500 |
| `SSDKISS00412` | `SIGNATURE_VERIFICATION_FAILED` | Failed to Signature verification. | 400 |
| `SSDKISS00413` | `SIGNATURE_GENERATION_FAILED` | Failed to generate signature. | 500 |
| `SSDKISS00414` | `HASH_GENERATION_FAILED` | Failed to generate hash value. | 500 |
| `SSDKISS00415` | `FAILED_TO_GET_FILE_WALLET_MANAGER` | Failed to get File wallet manager | 500 |
| `SSDKISS00416` | `WALLET_ALREADY_EXISTS` | Failed to create wallet: wallet already exists. | 500 |
| `SSDKISS00417` | `CRYPTO_KEY_PAIR_ALREADY_EXISTS` | Failed to generate keys: key already exists. | 500 |
| `SSDKISS00418` | `INVALID_KEY_TYPE` | Invalid Key Type. | 400 |
| `SSDKISS00419` | `GET_SIGN_KEY_IDS_FAILED` | Failed to get sign key ids | 400 |
| `SSDKISS00420` | `GET_SIGN_DATA_FAILED` | Failed to get sign data | 400 |
| `SSDKISS00421` | `FAILED_TO_LOAD_KEY_ELEMENT` | Failed to load key element. | 500 |
| `SSDKISS00422` | `WALLET_CONNECT_FAILURE` | Failed to connect wallet. | 500 |
| `SSDKISS00430` | `CRYPTO_PUBLIC_KEY_UN_COMPRESS_FAILED` | Failed to uncompress public key. | 500 |
| `SSDKISS00440` | `WALLET_INFO_NOT_FOUND` | Wallet is not registered. | 400 |
| `SSDKISS00441` | `WALLET_CREATION_FAILURE` | Failed to create wallet. | 500 |
| `SSDKISS00450` | `INVALID_PROOF_PURPOSE` | Invalid proof purpose. | 400 |
| `SSDKISS00499` | `NO_SUCH_ALGORITHM` | No such algorithm. | 500 |

## ZKP

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00500` | `ZKP_WALLET_CONNECT_FAILURE` | Failed to connect ZKP wallet. | 500 |
| `SSDKISS00501` | `ZKP_WALLET_ALREADY_EXISTS` | Failed to create ZKP wallet: ZKP wallet already exists. | 500 |
| `SSDKISS00502` | `FAILED_TO_GENERATE_CREDENTIAL_OFFER` | Failed to Create Credential Offer. | 500 |
| `SSDKISS00503` | `FAILED_TO_GENERATE_ZKP_ATTRIBUTE_VALUE` | Failed to Create Attribute Value. | 500 |
| `SSDKISS00504` | `FAILED_TO_ISSUE_CREDENTIAL` | Failed to Issued Credential. | 500 |

## Blockchain storage

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00600` | `BLOCKCHAIN_DIDDOC_REGISTRATION_FAILED` | Failed to register DID Document on the blockchain. | 500 |
| `SSDKISS00601` | `BLOCKCHAIN_GET_DID_DOC_FAILED` | Failed to retrieve DID document on the blockchain. | 500 |
| `SSDKISS00602` | `BLOCKCHAIN_VC_META_REGISTRATION_FAILED` | Failed to register VC meta on the blockchain. | 500 |
| `SSDKISS00603` | `BLOCKCHAIN_VC_META_RETRIEVAL_FAILED` | Failed to retrieve VC meta on the blockchain. | 500 |
| `SSDKISS00604` | `BLOCKCHAIN_VC_STATUS_UPDATE_FAILED` | Failed to update VC status on the blockchain. | 500 |

## Validation

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00700` | `INVALID_DATE_FORMAT` | Invalid Date Format. | 500 |
| `SSDKISS00701` | `REF_ID_INVALID` | The refId is not valid. | 400 |

## Authentication

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS00800` | `INVALID_TOKEN` | Invalid Access Token. | 400 |
| `SSDKISS00801` | `JWT_TOKEN_EXPIRED` | JWT token has expired | 401 |

## General

| Error code | Enum value | Message | HTTP status |
| --- | --- | --- | --- |
| `SSDKISS99999` | `UNKNOWN_SERVER_ERROR` | An unknown server error. | 500 |

The source of truth is [`IssuerSdkErrorCode.java`](../../source/did-issuer-sdk/src/main/java/org/omnione/did/sdk/issuer/exception/IssuerSdkErrorCode.java).
