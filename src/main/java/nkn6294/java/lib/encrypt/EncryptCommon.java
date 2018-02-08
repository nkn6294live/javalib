package nkn6294.java.lib.encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import nkn6294.java.lib.io.FileUtil;
import nkn6294.java.lib.util.StringUtils;

public class EncryptCommon {
	public static void testEncryptDecrypt() throws Exception {
		String filePath = "E:\\data.encrypt";
		String content = "NguyenKHacNam";

		String keyFilePath = "E:\\serect.key";
		String ivFilePath = "E:\\serect.iv";

		byte[] key = FileUtil.readFile(keyFilePath, 1024);
		byte[] iv = FileUtil.readFile(ivFilePath, 1024);

		System.out.println("KEY:" + StringUtils.byte2Hex(key));
		System.out.println("IV:" + StringUtils.byte2Hex(iv));

		FileUtil.writeFile(filePath, encrypt(content, key, iv));
		byte[] result = FileUtil.readFile(filePath, 1024);
		System.out.println(decrypt(result, key, iv));
	}
	
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
	
	public static void testHash() throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		messageDigest.update("Test".getBytes(Charset.forName("UTF-8")));
		System.out.println(StringUtils.byte2Hex(messageDigest.digest()));
		hashData("Test", "SHA1");
		hashData("Test", "MD5");
		hashAuthentication("Test", "HmacSHA1");
		hashAuthentication("Test", "HmacMD5");
		privateKeyCryptography("Test", "DES", 56);
		privateKeyCryptography("Test", "AES", 128);
	}
	
	public static byte[] hashData(String data, String algothimn)
			throws Exception {
		byte[] plainText = data.getBytes("UTF8");
		MessageDigest messageDigest = MessageDigest.getInstance(algothimn);
		// System.out.println(messageDigest.getProvider().getInfo());

		messageDigest.update(plainText);
		byte[] output = messageDigest.digest();
		String result = StringUtils.byte2Hex(output);
		System.out.println("RESULT: " + result);
		return output;
	}

	public static byte[] hashAuthentication(String data, String algothimn)
			throws Exception {
		String result = "";
		byte[] input = data.getBytes("UTF-8");
		KeyGenerator keyGen = KeyGenerator.getInstance(algothimn);
		SecretKey key = keyGen.generateKey();
		System.out.println("KEY:" + StringUtils.byte2Hex(key.getEncoded()));

		Mac mac = Mac.getInstance(algothimn);
		mac.init(key);
		mac.update(input);
		byte[] output = mac.doFinal();
		result = StringUtils.byte2Hex(mac.doFinal());
		System.out.println("RESULT: " + result);

		return output;
	}

	public static byte[] privateKeyCryptography(String data, String algothim,
			int keySize) throws Exception {
		String result = "";
		byte[] input = data.getBytes("UTF-8");

		KeyGenerator keyGen = KeyGenerator.getInstance(algothim);
		keyGen.init(keySize);
		Key key = keyGen.generateKey();
		System.out.println("KEY:" + StringUtils.byte2Hex(key.getEncoded()));

		Cipher cipher = Cipher.getInstance(algothim + "/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] output = cipher.doFinal(input);
		result = StringUtils.byte2Hex(output);

		System.out.println("RESULT: " + result);
		return output;
	}

	public static byte[] encrypt(String data, byte[] key, byte[] iv)
			throws Exception {

		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
		byte[] result = cipher.doFinal(data.getBytes("UTF8"));
		return result;
	}

	
	public static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        byte[] result = cipher.doFinal(data);
        return result;
    }
	public static byte[] encrypt(String data, byte[] key) throws Exception {
        return encrypt(data, key, iv);
    }
	public static byte[] encrypt(String data, String keyFile) throws Exception {
        byte[] key = FileUtil.readFile(keyFile, 1024);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        byte[] result = cipher.doFinal(data.getBytes("UTF8"));
        return result;
    }
	public static String decrypt(byte[] data, byte[] key, byte[] iv)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv);

		// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
		byte[] output = cipher.doFinal(data);

		String result = new String(output, "UTF8");
		return result;
	}
	static class AES {
		public static void setKey(String strKey) {
			MessageDigest sha = null;
			try {
				key = strKey.getBytes("UTF-8");
				sha = MessageDigest.getInstance("SHA-1");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 16);
				secretKey = new SecretKeySpec(key, "AES");
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}

		public static byte[] encrypt(String content, String password) {
			try {
				setKey(password);
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] output = cipher.doFinal(content.getBytes("UTF8"));
				return output;
			} catch (Exception ex) {
				System.out.println("Error while encrypt:" + ex.getMessage());
				ex.printStackTrace();
			}
			return null;
		}

		public static byte[] decrypt(String content, String password) {
			try {
				setKey(password);
				Cipher cipher = Cipher.getInstance("AES/EBC/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return cipher.doFinal(content.getBytes());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		private static byte[] key;
		private static SecretKey secretKey;
	}
	public static String decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, iv);
    }
	public static String decrypt(byte[] data, String keyFile) throws Exception {
        byte[] key = FileUtil.readFile(keyFile, 1024);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
        byte[] output = cipher.doFinal(data);

        String result = new String(output, "UTF8");
        return result;
    }
    public static byte[] decrypt2Bytes(byte[] data,  byte[] key, byte[] iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
        byte[] output = cipher.doFinal(data);
        return output;
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
    
    
//	public static byte publicKey[] = { (byte) 0xFD, (byte) 0xE0, (byte) 0x3B,
//			(byte) 0xBE, (byte) 0xFD, (byte) 0xBD, (byte) 0xAB, (byte) 0xD1,
//			(byte) 0x52, (byte) 0x50, (byte) 0x09, (byte) 0x59, (byte) 0x14,
//			(byte) 0x0A, (byte) 0xA0, (byte) 0x97 };
//
//	public static byte iv[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//
//
//
//	public static byte[] PUBLIC_KEY = { (byte) 0xFD, (byte) 0xE0, (byte) 0x3B,
//			(byte) 0xBE, (byte) 0xFD, (byte) 0xBD, (byte) 0xAB, (byte) 0xD1,
//			(byte) 0x52, (byte) 0x50, (byte) 0x09, (byte) 0x59, (byte) 0x14,
//			(byte) 0x0A, (byte) 0xA0, (byte) 0x97 };
//
//	public static byte[] IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
}
