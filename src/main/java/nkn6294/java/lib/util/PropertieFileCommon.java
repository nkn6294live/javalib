package nkn6294.java.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	 public static Properties ReadFromFile(String filePath) throws FileNotFoundException, IOException { //config.properties
	        FileInputStream input = new FileInputStream(filePath);
	        Properties properties = new Properties();
	        properties.load(input);
	        return properties;
	    }

	    public static void WriteToFile(String filePath, Properties properties, String... comments) throws FileNotFoundException, IOException {
	        File file = new File(filePath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	        FileOutputStream output = new FileOutputStream(file);
	        if (comments.length > 0) {
	            StringBuilder builder = new StringBuilder();
	            for(String str : comments) {
	                builder.append(str);
	            }
	            properties.store(output, builder.toString());
	        } else {
	            properties.store(output, null);
	        }
	    }
}
