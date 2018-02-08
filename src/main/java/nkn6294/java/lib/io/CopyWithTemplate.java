package nkn6294.java.lib.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import nkn6294.java.lib.delegate.Action2;

public class CopyWithTemplate {

    public static boolean exportWithTemplate(Writer writer, Map<String, String> datas, Reader readerTemplate) {
    	BufferedReader reader = null;
    	try {
            if (datas == null) {
                throw new Exception("Data param null");
            }
            String line = null;
            if (readerTemplate instanceof BufferedReader) {
                reader = (BufferedReader) readerTemplate;
            } else {
                reader = new BufferedReader(readerTemplate);
            }
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("###")) {
                    String key = line.substring(3);
                    String content = datas.get(key);
                    if (content != null) {
                        writer.write(content);
                    }
                } else {
                    writer.write(line);
                }
                writer.write("\n");
            }
            writer.flush();
            reader.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean exportWithTemplate(Writer writer, Reader readerTemplate, Action2<String, Writer> actions) {
    	BufferedReader reader = null;
        try {
            if (readerTemplate instanceof BufferedReader) {
                reader = (BufferedReader) readerTemplate;
            } else {
                reader = new BufferedReader(readerTemplate);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("###")) {
                    String key = line.substring(3);
                    if (actions != null) {
                        actions.action(key, writer);
                    }
                } else {
                    writer.write(line);
                }
                writer.write("\n");
            }
            writer.flush();
            reader.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean exportWithTemplate(String fileExport, Map<String, String> datas, String fileTemplate) {
        try {
            File fileOutput = new File(fileExport);
            if (!fileOutput.exists()) {
                fileOutput.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutput)));
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileTemplate)));
            return exportWithTemplate(writer, datas, reader);
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean exportWithTemplate(String fileExport, String fileTemplate, Action2<String, Writer> datas) {
        try {
            File fileOutput = new File(fileExport);
            if (!fileOutput.exists()) {
                fileOutput.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutput)));
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileTemplate)));
            return exportWithTemplate(writer, reader, datas);
        } catch (Exception ex) {
            return false;
        }
    }
}
