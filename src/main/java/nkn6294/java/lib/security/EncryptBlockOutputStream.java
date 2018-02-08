package nkn6294.java.lib.security;

import java.io.OutputStream;

import nkn6294.java.lib.encrypt.EncryptConfig;
import nkn6294.java.lib.io.BlockOutputStream;


public class EncryptBlockOutputStream extends BlockOutputStream {
	public static EncryptConfig DEFAULT_ENCRYPT_CONFIG = EncryptConfig
			.getDefaultEncryptConfig();

	public EncryptBlockOutputStream(OutputStream outputStream) {
		super(outputStream);
		this.encryptConfig = DEFAULT_ENCRYPT_CONFIG;
		this.convertData = new EncryptConvertData(this.encryptConfig);
	}

	public EncryptBlockOutputStream(OutputStream outputStream, byte delimeter) {
		super(outputStream, delimeter);
		this.encryptConfig = DEFAULT_ENCRYPT_CONFIG;
		this.convertData = new EncryptConvertData(this.encryptConfig);
	}

	public EncryptBlockOutputStream(OutputStream outputStream,
			EncryptConfig encryptConfig) {
		super(outputStream);
		this.encryptConfig = encryptConfig;
		this.convertData = new EncryptConvertData(this.encryptConfig);
	}

	public EncryptBlockOutputStream(OutputStream outputStream, byte delimeter,
			EncryptConfig encryptConfig) {
		super(outputStream, delimeter);
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
