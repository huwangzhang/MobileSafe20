package com.example.huwang.mobilesafe20.utils;

import com.example.huwang.mobilesafe20.engine.AesCbcWithIntegrity;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by huwang on 2017/3/30.
 */

public class EncrptUtils {
    /**
     * 加密
     *
     * @param text
     * @param
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String text) throws Exception {
        // encrypt
        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = AesCbcWithIntegrity.encrypt(text, keys);
        // store or send to server
        String ciphertextString = cipherTextIvMac.toString();

        return ciphertextString;

    };

    public static String decrpty(String text) throws Exception
    {
        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac=new AesCbcWithIntegrity.CipherTextIvMac(text);
        String plainText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys);
        return plainText;
    }
    public static AesCbcWithIntegrity.SecretKeys keys;
    static {
        try {
            keys = AesCbcWithIntegrity.generateKey();

        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 加密密码
     *
     * @return
     */
    public static String getKey() {
        AesCbcWithIntegrity.SecretKeys keys = null;
        try {
            keys = AesCbcWithIntegrity.generateKey();
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return keys.toString();
    };
}
