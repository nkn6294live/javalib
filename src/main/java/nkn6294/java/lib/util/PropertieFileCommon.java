package nkn6294.java.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertieFileCommon {
	public static void writeToPropertiesFile() {
		Properties properties = new Properties();
		OutputStream output = null;
		try {
			File file = new File("config.properties");
			output = new FileOutputStream(file);
			
			properties.setProperty("key1", "value1");
			properties.setProperty("key2", "value2");
			
			properties.store(output, null);
		} catch (IOException ex) {
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	public static void readPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			System.out.println(prop.getProperty("database"));
			System.out.println(prop.getProperty("dbuser"));
			System.out.println(prop.getProperty("dbpassword"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
