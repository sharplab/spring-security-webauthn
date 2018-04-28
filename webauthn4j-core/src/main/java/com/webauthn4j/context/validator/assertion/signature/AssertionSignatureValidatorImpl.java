/*
 * Copyright 2002-2018 the original author or authors.
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

package com.webauthn4j.context.validator.assertion.signature;

import com.webauthn4j.attestation.authenticator.CredentialPublicKey;
import com.webauthn4j.context.WebAuthnAuthenticationContext;
import com.webauthn4j.exception.BadSignatureException;
import com.webauthn4j.util.MessageDigestUtil;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * AssertionSignatureValidatorImpl
 */
public class AssertionSignatureValidatorImpl implements AssertionSignatureValidator {

    public void verifySignature(WebAuthnAuthenticationContext webAuthnAuthenticationContext, CredentialPublicKey credentialPublicKey) {
        byte[] signedData = getSignedData(webAuthnAuthenticationContext);
        byte[] signature = webAuthnAuthenticationContext.getSignature();
        if (!credentialPublicKey.verifySignature(signature, signedData)) {
            throw new BadSignatureException("Bad signature");
        }
    }

    protected byte[] getSignedData(WebAuthnAuthenticationContext webAuthnAuthenticationContext) {
        MessageDigest messageDigest = MessageDigestUtil.createSHA256();
        byte[] rawAuthenticatorData = webAuthnAuthenticationContext.getAuthenticatorData();
        byte[] clientDataHash = messageDigest.digest(webAuthnAuthenticationContext.getCollectedClientData());
        return ByteBuffer.allocate(rawAuthenticatorData.length + clientDataHash.length).put(rawAuthenticatorData).put(clientDataHash).array();
    }


}
