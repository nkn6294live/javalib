package nkn6294.java.lib.io;

public interface IConvertData {
   public byte[] encrypt(String string) ;
   public String decrypt(byte[] data);
   public byte[] encryptBytes(byte[] data);
   public byte[] decryptBytes(byte[] data);
}
