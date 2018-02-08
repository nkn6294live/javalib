package nkn6294.java.lib.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Enumeration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLCommon {
	public static void testKeyStore() throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		String path = "P:/CA/ca.jks";
		String storePass = "sp123456";
		String keyPass = "kp123456";
		File file = new File(path);
		if (file.exists()) {
			InputStream in = new FileInputStream(path);
			keyStore.load(in, storePass.toCharArray());
			in.close();
		} else {
			keyStore.load(null, null);
		}
		KeyStore.ProtectionParameter params = new KeyStore.PasswordProtection(
				keyPass.toCharArray());

		Enumeration<String> strings = keyStore.aliases();

		// SecretKey mySecretKey = null;
		// KeyStore.SecretKeyEntry skEntry =
		// new KeyStore.SecretKeyEntry(mySecretKey);
		// keyStore.setEntry("secretKeyAlias", skEntry, params);

		while (strings.hasMoreElements()) {
			String alias = strings.nextElement();
			KeyStore.Entry entry = keyStore.getEntry(alias, params);
			if (entry instanceof KeyStore.PrivateKeyEntry) {
				System.out.println("PrivateKeyEntry");
			} else if (entry instanceof KeyStore.SecretKeyEntry) {
				System.out.println("SecretKeyEntry");
			} else if (entry instanceof KeyStore.TrustedCertificateEntry) {
				System.out.println("TrustedCertificateEntry");
			} else {
				System.out.println(entry);
			}
		}
	}
	
	public static void testKeystore() throws Exception {
		String path = "P:\\CA\\clientstatus\\1\\server.jks";
		path = "P:\\Eclipse\\Workspace\\DNSServiceNew\\dist\\data\\private\\server_.jks";
		try {
			FileInputStream is = new FileInputStream(path);
			char[] storePass = "storepassserver".toCharArray();
			char[] keyPass = "keypassserver".toCharArray();
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, storePass);
			Enumeration<String> alias = keystore.aliases();
			while (alias.hasMoreElements()) {
				System.out.println("Alias:" + alias.nextElement());
			}
			// KeyStore.ProtectionParameter protParam =
			// new KeyStore.PasswordProtection(keyPass);
			// System.out.println(keystore.getEntry("client",
			// protParam).toString());
			Key serverKey = keystore.getKey("server", keyPass);
			System.out.println(serverKey.toString());
			Key clientKey = keystore.getKey("client", keyPass);
			System.out.println(clientKey.toString());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public static void testSSL() throws Exception {
		String algKeyManager = KeyManagerFactory.getDefaultAlgorithm();
		System.out.println(algKeyManager);
		String algTrustManager = TrustManagerFactory.getDefaultAlgorithm();
		System.out.println(algTrustManager);
	}
	public static void testExport() throws Exception {
		// KeyStore keystore = KeyStore.getInstance("JKS");
		// BASE64Encoder encoder = new BASE64Encoder();
		// keystore.load(new FileInputStream(keystoreFile), keyStorePassword);
		// Key key = keystore.getKey(alias, keyPassword);
		// String encoded = encoder.encode(key.getEncoded());
		// FileWriter fw = new FileWriter(exportedFile);
		// fw.write("---BEGIN PRIVATE KEY---\n");
		// fw.write(encoded);
		// fw.write("\n");
		// fw.write("---END PRIVATE KEY---");
		// fw.close();
	}

}
