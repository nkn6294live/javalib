/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nkn6294.java.lib.encrypt;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CypherData {

    public static final String KEY = "BINHPV";

    public byte[] generateKey(String password) throws Exception {
        byte[] keyStart = password.getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public byte[] encodeFile(byte[] key, byte[] fileData) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(fileData);
        return encrypted;
    }

    public byte[] decodeFile(byte[] key, byte[] fileData) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(fileData);
        return decrypted;
    }
    
    public static byte[] encrypt(byte[] key, byte[] fileData, String algothinm) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, algothinm);
        Cipher cipher = Cipher.getInstance(algothinm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(fileData);
        return encrypted;
    }
    
    public static byte[] decrypt(byte[] key, byte[] fileData, String algothinm) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, algothinm);
        Cipher cipher = Cipher.getInstance(algothinm);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(fileData);
        return decrypted;
    }
}
