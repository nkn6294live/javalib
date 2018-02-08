package nkn6294.java.lib.security;

import java.io.InputStream;

import nkn6294.java.lib.encrypt.EncryptConfig;
import nkn6294.java.lib.io.BlockInputStream;


public class EncryptBlockInputStream extends BlockInputStream {
    public static EncryptConfig DEFAULT_ENCRYPT_CONFIG = EncryptConfig.getDefaultEncryptConfig();
    public EncryptBlockInputStream(InputStream inputStream) {
        super(inputStream);
        this.encryptConfig = DEFAULT_ENCRYPT_CONFIG;
        this.convertData = new EncryptConvertData(this.encryptConfig);
    }

    public EncryptBlockInputStream(InputStream inputStream, byte delimeter) {
        super(inputStream, delimeter);
        this.encryptConfig = DEFAULT_ENCRYPT_CONFIG;
        this.convertData = new EncryptConvertData(this.encryptConfig);
    }

    public EncryptBlockInputStream(InputStream inputStream, EncryptConfig encryptConfig) {
        super(inputStream);
        this.encryptConfig = encryptConfig;
        this.convertData = new EncryptConvertData(this.encryptConfig);
    }

    public EncryptBlockInputStream(InputStream inputStream, byte delimeter, EncryptConfig encryptConfig) {
        super(inputStream, delimeter);
        this.encryptConfig = encryptConfig;
        this.convertData = new EncryptConvertData(this.encryptConfig);
    }

    public EncryptConfig getEncryptConfig() {
        return this.encryptConfig;
    }

    public void updateConfig(EncryptConfig encryptConfig) {
        this.encryptConfig = encryptConfig;
        if (this.convertData instanceof EncryptConvertData) {
            ((EncryptConvertData) convertData).updateConfig(this.encryptConfig);
        }
    }
    private EncryptConfig encryptConfig;
}
