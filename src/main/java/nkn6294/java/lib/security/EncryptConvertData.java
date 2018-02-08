package nkn6294.java.lib.security;

import nkn6294.java.lib.encrypt.EncryptConfig;
import nkn6294.java.lib.io.IConvertData;

public class EncryptConvertData implements IConvertData {

    public EncryptConvertData(EncryptConfig encryptConfig) {
        if (encryptConfig == null) {
            this.encryptConfig = EncryptConfig.getDefaultEncryptConfig();
        } else {
            this.encryptConfig = encryptConfig;
        }
    }

    public void updateConfig(EncryptConfig encryptConfig) {
        if (encryptConfig == null) {
            this.encryptConfig = EncryptConfig.getDefaultEncryptConfig();
        } else {
            this.encryptConfig = encryptConfig;
        }
    }

    @Override
    public byte[] encrypt(String string) {
        return encryptConfig.encrypt(string);
    }

    @Override
    public String decrypt(byte[] data) {
        return encryptConfig.decrypt(data);
    }

    @Override
    public byte[] encryptBytes(byte[] data) {
        return encryptConfig.encrypt(data);
    }

    @Override
    public byte[] decryptBytes(byte[] data) {
        return encryptConfig.decrypt2Bytes(data);
    }

    private EncryptConfig encryptConfig;
}
