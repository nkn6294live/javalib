package nkn6294.java.lib.encrypt;

public class EncryptConfig {

    public EncryptConfig() {
        this.initVector = INIT_VECTOR;
        this.publicKey = PUBLIC_KEY;
    }

    public EncryptConfig(byte[] initVector, byte[] publicKey) {
        this.initVector = initVector;
        this.publicKey = publicKey;
    }

    public static EncryptConfig getDefaultEncryptConfig() {
        return new EncryptConfig();
    }

    public static EncryptConfig createEncryptConfig(byte[] initVector, byte[] publicKey) {
        return new EncryptConfig(initVector, publicKey);
    }

    public static byte[] encrypt(String data, EncryptConfig encryptConfig) {
        try {
            return EncryptCommon.encrypt(data, encryptConfig.getPublicKey(), encryptConfig.getInitVector());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String decrypt(byte[] encryptData, EncryptConfig encryptConfig) {
        try {
            return EncryptCommon.decrypt(encryptData, encryptConfig.getPublicKey(), encryptConfig.getInitVector());
        } catch (Exception ex) {
            return null;
        }
    }
    public static byte[] decrypt2Bytes(byte[] encryptData, EncryptConfig encryptConfig) {
        try {
            return EncryptCommon.decrypt2Bytes(encryptData, encryptConfig.getPublicKey(), encryptConfig.getInitVector());
        } catch (Exception ex) {
            return null;
        }
    }    

    public byte[] getInitVector() {
        return initVector;
    }

    public void setInitVector(byte[] initVector) {
        this.initVector = initVector;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] encrypt(String data) {
        try {
            return EncryptCommon.encrypt(data, this.publicKey, this.initVector);
        } catch (Exception ex) {
            return null;
        }
    }

    public byte[] encrypt(byte[] data) {
        try {
            return EncryptCommon.encrypt(data, this.publicKey, this.initVector);
        } catch (Exception ex) {
            return null;
        }
    }

    public String decrypt(byte[] encryptData) {
        try {
            return EncryptCommon.decrypt(encryptData, this.publicKey, this.initVector);
        } catch (Exception ex) {
            return null;
        }
    }

    public byte[] decrypt2Bytes(byte[] encryptData) {
        try {
            return EncryptCommon.decrypt2Bytes(encryptData, this.publicKey, this.initVector);
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] INIT_VECTOR = {
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    };

    public static byte[] PUBLIC_KEY = {
        (byte) 0xFD, (byte) 0xE0, (byte) 0x3B, (byte) 0xBE,
        (byte) 0xFD, (byte) 0xBD, (byte) 0xAB, (byte) 0xD1,
        (byte) 0x52, (byte) 0x50, (byte) 0x09, (byte) 0x59,
        (byte) 0x14, (byte) 0x0A, (byte) 0xA0, (byte) 0x97
    };

    private byte[] initVector;
    private byte[] publicKey;
}
