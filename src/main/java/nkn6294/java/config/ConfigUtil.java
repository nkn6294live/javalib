package nkn6294.java.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigUtil {

	public static Map<String, String> importConfig(String filePath) {
		Map<String, String> mapConfig = new HashMap<>();
		if (filePath != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("$")) {
						// System.err.println(line);
						ConfigString configString = ConfigString.parse(line);
						String header = configString.getHeader();
						if (header.length() > 0) {
							mapConfig.put(header, configString.getContent());
						}
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
				return null;
			}
		}
		return mapConfig;
	}

	public static Map<String, String> importConfig(String filePath, Set<String> setKey) {
		Map<String, String> mapConfig = new HashMap<>();
		if (filePath != null && setKey != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("$")) {
						ConfigString configString = ConfigString.parse(line);
						String header = configString.getHeader();
						String content = configString.getContent();
						if (header.length() > 0 && setKey.contains(header)) {
							mapConfig.put(header, content);
						}
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
				return null;
			}
		}
		return mapConfig;
	}

	public static String exportConfig(Map<String, String> mapConfig) {
		if (mapConfig == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String key : mapConfig.keySet()) {
			builder.append(ConfigString.export(key, mapConfig.get(key)));
			builder.append("\n");
		}
		return builder.toString();
	}

	public static String exportConfig(Map<String, String> mapConfig, String template) {
		BufferedReader reader = new BufferedReader(new StringReader(template));
		return exportConfig(mapConfig, reader);
	}

	public static void exportConfig(Map<String, String> mapConfig, File templateFile, File outputFile) {
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			exportConfig(mapConfig, reader, writer);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (Exception ex) {
				System.err.printf(ex.getMessage());
			}
		}
	}

	public static String exportConfig(Map<String, String> mapConfig, File fileTemplate) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileTemplate)));
			return exportConfig(mapConfig, reader);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

	public static void exportConfig(Map<String, String> mapConfig, BufferedReader readerTemplate, BufferedWriter writer)
			throws IOException {
		if (mapConfig == null || readerTemplate == null || writer == null) {
			return;
		}
		String line = readerTemplate.readLine();
		while (line != null) {
			if (line.startsWith("$")) {
				ConfigString configString = ConfigString.parse(line);
				if (configString == null) {
					writer.write(line);
				} else {
					String content = mapConfig.get(configString.getHeader());
					if (content != null) {
						writer.write(ConfigString.export(configString.getHeader(), content));
					} else {
						writer.write(line);
					}
				}
			} else {
				writer.write(line);
			}
			writer.write("\n");
			line = readerTemplate.readLine();
		}
		writer.flush();
	}

	public static String exportConfig(Map<String, String> mapConfig, BufferedReader reader) {
		if (mapConfig == null || reader == null) {
			return null;
		}
		StringWriter writer = new StringWriter();
		try {
			String line = reader.readLine();
			while (line != null) {
				if (line.startsWith("$")) {
					ConfigString configString = ConfigString.parse(line);
					if (configString == null) {
						writer.write(line);
					} else {
						String content = mapConfig.get(configString.getHeader());
						if (content != null) {
							writer.write(ConfigString.export(configString.getHeader(), content));
						} else {
							writer.write(line);
						}
					}
				} else {
					writer.write(line);
				}
				writer.write("\n");
				line = reader.readLine();
			}
			writer.flush();

		} catch (IOException ex) {
			return null;
		}
		return writer.toString();
	}

}
