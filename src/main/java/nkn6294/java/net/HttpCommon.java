package nkn6294.java.net;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpCommon {
	/***
	 * GET REMOTE DATA
	 * 
	 * @param targetURL: targetURL request
	 * @param username : username for basic http auth, if username and password == null -> not use auth
	 * @param password : password for basic http auth, if username and password == null -> not use auth
	 * @return null if error otherise content data (can empty string).
	 */
	public static String getRemoteData2(String targetURL, String username, String password, String methodName) {
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection connection = null;
		try {
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(6000);
			if (username != null && password != null) {
				connection = updateConnectionBasicAuthorization(connection, username, password);
			}
			if (methodName != null && !methodName.isEmpty()) {
				connection = updateConnectionMethod(connection, "PROPFIND");			
			} else {
				connection.setRequestMethod("GET");				
			}
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new Exception("URL Invalid");
			}
			InputStream inputStream = connection.getInputStream();
			// int length = connection.getContentLength();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			reader.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	public static HttpURLConnection updateConnectionMethod(HttpURLConnection connection, String methodName) {
		try {
			connection.setRequestMethod(methodName);
		} catch (final ProtocolException pe) {
			try {
				final Class<?> httpURLConnectionClass = connection.getClass();
				final Class<?> parentClass = httpURLConnectionClass.getSuperclass();
				final Field methodField;
				if (parentClass == HttpsURLConnection.class) {
					methodField = parentClass.getSuperclass().getDeclaredField("method");
				} else {
					methodField = parentClass.getDeclaredField("method");
				}
				methodField.setAccessible(true);
				methodField.set(connection, methodName);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
		return connection;
	}

	public static HttpURLConnection updateConnectionBasicAuthorization(HttpURLConnection connection, String username, String password) {
//		byte[] message = (username + ":" + password).getBytes(StandardCharsets.UTF_8);
//		String encoded = DatatypeConverter.printBase64Binary(message);
		String encoded = Base64.getEncoder()
				.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)); // Java 8
		connection.setRequestProperty("Authorization", "Basic " + encoded);
		return connection;
	}
	public static String getRemoteData(String targetURL, String username, String password, String methodName, SSLContext context) {
		try {
			//TODO check input
			HttpURLConnection connection = null;
			StringBuilder result = new StringBuilder();
			URL url = new URL(targetURL);
			
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
	                url.getPort(), url.getPath(), url.getQuery(), null);
	        url = new URL(uri.toASCIIString());
			
	        if (context == null) {
				connection = (HttpURLConnection) url.openConnection();				
			} else {
				connection = (HttpsURLConnection) url.openConnection();
				((HttpsURLConnection)connection).setSSLSocketFactory(context.getSocketFactory());
			}
			connection.setConnectTimeout(6000);
			connection.setReadTimeout(6000);
			if (username != null && password != null) {
				connection = updateConnectionBasicAuthorization(connection, username, password);
			}
			if (methodName != null && !methodName.isEmpty()) {
				connection = updateConnectionMethod(connection, methodName);			
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			return result.toString();
		} catch(Exception ex) {
			return null;
		}
		
	}
	public static Map<String, String> parseParams(String paramString) {
		Map<String, String> params = new HashMap<String, String>();
		if (paramString == null || paramString.length() == 0) {
			return params;
		}
		StringTokenizer tokenizer = new StringTokenizer(paramString, "&");
		while (tokenizer.hasMoreElements()) {
			String param = tokenizer.nextToken();
			int sep = param.indexOf("=");
            String name = (sep >= 0) ? decodeUrl(param.substring(0, sep)).trim() : decodeUrl(param).trim();
            String value = (sep >= 0) ? decodeUrl(param.substring(sep + 1)) : null;
            
            if (name != null && value != null) {
            	params.put(name, value);            	
            }
		}		
		return params;
	}
	public static void testHTTPGetInfo() throws Exception {
		String domain = "http://hfs1.duytan.edu.vn/upload/ebooks/";
		int indexF = 1001;
		int max = 1050;
		System.out.println("Processing...");
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		while (indexF <= max) {
			String link = domain + indexF + ".pdf";
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			try {
				connection.connect();
				int code = connection.getResponseCode();
				if (code != 404) {
					buffer.append("\'").append(link).append("\'").append(",")
							.append("\n");
				} else {

				}
			} catch (Exception ex) {
			}
			indexF++;
		}
		buffer.append("]");
		System.out.println("--------END-------------------");
		Writer writer = new OutputStreamWriter(
				new FileOutputStream("source.js"));
		writer.write(buffer.toString());
		writer.flush();
		writer.close();
		System.out.println(buffer.toString());
	}
	public static String decodeUrl(String input) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        return decoded;
	}

	public static void testHTTPHeader() throws Exception {
		URL obj = new URL("https://google.com");
		URLConnection conn = obj.openConnection();
		Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " ,Value : "
					+ entry.getValue());
		}
	}
}
