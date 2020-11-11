/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.security.assist;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Cryptography utils
 */
public class MACUtils {

    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String HmacSHA224 = "HmacSHA224";
    public static final String HmacSHA256 = "HmacSHA256";
    public static final String HmacSHA384 = "HmacSHA384";
    public static final String HmacSHA512 = "HmacSHA512";
    public static final String HmacSHA512_224 = "HmacSHA512/224";
    public static final String HmacSHA512_256 = "HmacSHA512/256";
    public static final String HmacSHA3_224 = "HmacSHA3-224";
    public static final String HmacSHA3_256 = "HmacSHA3-256";
    public static final String HmacSHA3_384 = "HmacSHA3-384";
    public static final String HmacSHA3_512 = "HmacSHA3-512";

    /**
     * Returns MAC of value
     *
     * @param value
     * @param key
     * @return
     */
    public static byte[] sign(String algorithm, byte[] value, byte[] key) {
        try {
            final Mac hashAlg = Mac.getInstance(algorithm);
            final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            hashAlg.init(keySpec);
            hashAlg.update(value);
            return hashAlg.doFinal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies MAC of value
     *
     * @param algorithm
     * @param value
     * @param key
     * @param signature
     * @return
     */
    public static boolean verify(String algorithm, byte[] value, byte[] key, byte[] signature) {
        byte[] nsignature = sign(algorithm, value, key);
        return MessageDigest.isEqual(nsignature, signature);
    }

}
