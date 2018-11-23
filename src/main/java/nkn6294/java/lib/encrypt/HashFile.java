package nkn6294.java.lib.encrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nkn6294.java.lib.util.StringUtils;


public final class HashFile{
    public enum HashAlgorithm{

    }

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    public static final String DEFAULT_ALGORITHM = MD5;

    public static String GetHexHashFromFile(String fileName){
        return GetHexHashFromFile(fileName, DEFAULT_ALGORITHM);
    }

    public static byte[] GetHashFromFile(String fileName){
        return GetHashFromFile(fileName, DEFAULT_ALGORITHM);
    }

    public static String GetHexHashFromFile(String fileName, String hashAlgorithm){
        byte[] digest = GetHashFromFile(fileName, hashAlgorithm);
        if(digest == null) {
    		return null;
    	}
        return StringUtils.byte2Hex(digest);
    }

    public static byte[] GetHashFromFile(String fileName, String hashAlgorithm){
        MessageDigest md;
		try {
			md = MessageDigest.getInstance(hashAlgorithm);
            FileInputStream fileInputStream = new FileInputStream(fileName);

            byte[] buffer = new byte[2048];
            int numBytes;
            while((numBytes = fileInputStream.read(buffer)) != -1) {
                md.update(buffer, 0, numBytes);
            }
	        byte[] digest = md.digest();
	        fileInputStream.close();
	        return digest;
		} catch (NoSuchAlgorithmException | IOException e) {
			return null;
		}
    }


}
