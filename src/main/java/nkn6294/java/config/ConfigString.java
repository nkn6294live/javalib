package nkn6294.java.config;

public class ConfigString {

	private ConfigString() {
		this.header = "";
		this.content = "";
	}

	public String getHeader() {
		return this.header;
	}

	public String getContent() {
		return this.content;
	}

	// $HEADER='CONTENT'
	public static final ConfigString parse(String value) {
		ConfigString configString = null;
		if (value != null) {
			if (value.startsWith("$")) {
				configString = new ConfigString();
				int index = value.indexOf("=");
				int contentStart = value.indexOf("'");
				int contentStop = value.indexOf("'", contentStart + 1);
				if (index > 0 && contentStart == index + 1) {
					configString.header = value.substring(1, index);
					if (contentStop > contentStart) {
						configString.content = value.substring(contentStart + 1, contentStop);
					}
				}

			}
		}
		return configString;
	}

	public static final String export(ConfigString configString) {
		if (configString == null) {
			return "$=''";
		}
		return "$" + configString.getHeader() + "='" + configString.getContent() + "'";
	}

	public static final String export(String header, String content) {
		String tempHeader = header == null ? "" : header;
		String tempContent = content == null ? "" : content;
		return "$" + tempHeader + "='" + tempContent + "'";
	}

	private String header;
	private String content;
}
