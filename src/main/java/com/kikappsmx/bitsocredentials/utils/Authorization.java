package com.kikappsmx.bitsocredentials.utils;

import java.math.BigInteger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Authorization {

    private static final String REQUEST_PATH = "/v3/balance/";
    private static final String ALGORITHM = "HmacSHA256";
    private static final String HTTP_METHOD = "GET";

    /**
     * @param key    the decrypted key
     * @param secret the decrypted secret
     * @return the authorization which be used in the header
     */
    public static String getAuthorization(String key, String secret) throws Exception {
        String signature;
        long nonce = System.currentTimeMillis();
        String message = nonce + HTTP_METHOD + REQUEST_PATH;
        byte[] secretBytes = secret.getBytes();
        SecretKeySpec localMac = new SecretKeySpec(secretBytes, ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(localMac);
        byte[] arrayOfByte = mac.doFinal(message.getBytes());
        BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
        signature = String.format("%0" + (arrayOfByte.length << 1) + "x", localBigInteger);
        return String.format("Bitso %s:%s:%s", key, nonce, signature);
    }
}