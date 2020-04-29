package com.example.androidchat;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    private static final String pass = "ololo";

    private static SecretKeySpec keySpec;

    static {
        MessageDigest shaDigest = null;
        try {
            shaDigest = MessageDigest.getInstance("SHA-256");
            shaDigest.update(pass.getBytes(), 0, pass.length());
            byte[] key = shaDigest.digest();
            keySpec = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String  encrypt(String rawText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypt = cipher.doFinal(rawText.getBytes());
        return Base64.encodeToString(encrypt, Base64.DEFAULT);
    }

    public static String  decrypt(String cipheredText) throws Exception{
        byte[] encrypt = Base64.decode(cipheredText, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] rawText = cipher.doFinal(encrypt);
        return new String(rawText, "UTF-8");
    }
}
