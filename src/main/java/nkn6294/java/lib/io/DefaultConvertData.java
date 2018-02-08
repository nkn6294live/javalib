package nkn6294.java.lib.io;

import java.nio.charset.Charset;

public class DefaultConvertData implements IConvertData {
    public DefaultConvertData() {
        
    }
    @Override
    public byte[] encrypt(String string) {
        if(string == null) {
            return null;
        }
        return string.getBytes(Charset.forName("UTF8"));
    }

    @Override
    public String decrypt(byte[] data) {
        if(data == null) {
            return null;
        }
        return new String(data, Charset.forName("UTF8"));
    }

    @Override
    public byte[] encryptBytes(byte[] data) {
        return data;
    }

    @Override
    public byte[] decryptBytes(byte[] data) {
        return data;
    }
    
}
