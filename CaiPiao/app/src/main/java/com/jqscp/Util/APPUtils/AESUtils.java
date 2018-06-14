package com.jqscp.Util.APPUtils;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密
 */

public class AESUtils {
    private static String key="M3q4fMg3h4N5ij8k09pWq4rs6t1uFv8l";//密钥
    private static String iv="8438513960722961";//

    public static String encrypt(String seed)
            throws Exception {
        //byte[] rawKey = getRawKey(key.getBytes("utf-8"));
        byte[] keys = new byte[32];
        for (int i = 0; i < 32; i++) {
            if (i < key.getBytes().length) {
                keys[i] = key.getBytes()[i];
            } else {
                keys[i] = 0;
            }
        }
        byte[] result = encrypt(seed.getBytes("utf-8"), keys);
        ALog.e("加密后："+new String(result, "UTF8"));
        return new String(result, "UTF8");
    }

    public static String decrypt(String seed) throws Exception {
        //byte[] rawKey = getRawKey(key.getBytes("ASCII"));
        byte[] keys = new byte[32];
        for (int i = 0; i < 32; i++) {
            if (i < key.getBytes().length) {
                keys[i] = key.getBytes()[i];
            } else {
                keys[i] = 0;
            }
        }
        byte[] result = decrypt(Base64.decode(seed,0), keys);
        ALog.e("解密前："+new String(result, "UTF8"));
        return new String(result, "UTF8");
    }

    /***
     * AES加密算法加密
     * @param byteData 数据
     * @param byteKey key
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return AseE(byteData, byteKey);
    }

    /***
     * AES加密算法解密
     * @param byteData 数据
     * @param byteKey key
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return AseD(byteData, byteKey);
    }


    /***
     *
     * @param byteData
     * @param byteKey
     * @return
     * @throws Exception
     */
    private static byte[] AseE(byte[] byteData, byte[] byteKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new IvParameterSpec(iv.getBytes()));
        byte[] decrypted = cipher.doFinal(byteData);
        return Base64.encode(decrypted,0);
    }

    /***
     *
     * @param byteData
     * @param byteKey
     * @return
     * @throws Exception
     */
    private static byte[] AseD(byte[] byteData, byte[] byteKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(iv.getBytes()));
        byte[] decrypted = cipher.doFinal(byteData);
        return decrypted;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        int sdk_version = android.os.Build.VERSION.SDK_INT;
        if (sdk_version > 23) {  // Android  6.0 以上
            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        } else if (sdk_version >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    public static class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
