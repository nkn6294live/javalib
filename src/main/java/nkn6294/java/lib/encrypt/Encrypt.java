package nkn6294.java.lib.encrypt;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
	public static byte[] sha1(String input) throws Exception 
	{
		byte[] bytes = null;
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        bytes = input.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        bytes = digest.digest();       
	    return bytes;
	}
	
	public static byte[] encrypt(byte[] clear) throws Exception {
        //SecretKeySpec skeySpec = new SecretKeySpec(publicKey, "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//        IvParameterSpec ivspec = new IvParameterSpec(iv);
		
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

	public static byte[] decrypt(byte[] encrypted)
            throws Exception {
        //SecretKeySpec skeySpec = new SecretKeySpec(publicKey, "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
		//IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
	
	public static byte[] fromHexString(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String byte2Hex(byte bytes[]) {
		StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02X ", b));
	    }
	    
	    String result = sb.toString();
	    sb.delete(0, sb.length());
	    sb = null;
	    return result;
	}
    
    private static byte publicKey[] = 
	{   
		(byte)0xFD, (byte) 0xE0, (byte)0x3B, (byte)0xBE, 
		(byte)0xFD, (byte)0xBD, (byte)0xAB, (byte)0xD1, 
		(byte)0x52, (byte)0x50, (byte)0x09, (byte)0x59, 
		(byte)0x14, (byte)0x0A, (byte)0xA0, (byte)0x97 
	};	
    
    private static byte iv[] = 
	{   
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0
	};	
    
    private static SecretKeySpec skeySpec = new SecretKeySpec(publicKey, "AES");
    private static IvParameterSpec ivspec = new IvParameterSpec(iv);
}
