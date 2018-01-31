package com.kikappsmx.bitsocredentials.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String ALGORITHM = "AES";

    /**
     * @param pin the user's pin
     * @return the secret to decrypt the credentials
     */
    public static SecretKey getSecretKey(String pin) {
        StringBuilder secret = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            secret.append(pin);
        }
        return new SecretKeySpec(secret.toString().getBytes(), ALGORITHM);
    }

    /**
     * @param decryptedString a decrypted string
     * @param secret          the secret to decrypt the credentials
     * @return a encrypted string based in the secret
     */
    public static String getEncryptedString(String decryptedString, SecretKey secret) {
        String encryptedString = "";
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] bytes = cipher.doFinal(decryptedString.getBytes(CHARSET_NAME));
            encryptedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    /**
     * @param encryptedString a encrypted string
     * @param secret          the secret to decrypt the credentials
     * @return a decrypted string based in the secret
     */
    public static String getDecryptedString(String encryptedString, SecretKey secret) {
        String decryptedString = "";
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte[] bytes = Base64.decode(encryptedString, Base64.DEFAULT);
            decryptedString = new String(cipher.doFinal(bytes), CHARSET_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}