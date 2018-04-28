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

package com.webauthn4j.context.validator;

import com.webauthn4j.context.RelyingParty;
import com.webauthn4j.exception.BadRpIdException;
import com.webauthn4j.util.MessageDigestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Validates rpIdHash
 */
public class RpIdHashValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void validate(byte[] rpIdHash, RelyingParty relyingParty) {
        MessageDigest messageDigest = MessageDigestUtil.createSHA256();
        byte[] relyingPartyRpIdBytes = relyingParty.getRpId().getBytes(StandardCharsets.UTF_8);
        byte[] relyingPartyRpIdHash = messageDigest.digest(relyingPartyRpIdBytes);
        if (!Arrays.equals(rpIdHash, relyingPartyRpIdHash)) {
            logger.debug("Authentication failed: bad rpId is specified");
            throw new BadRpIdException("Bad rpId");
        }
    }
}
