package nkn6294.java.lib.common;

import java.io.InputStream;

public interface StreamImportAble {
    public boolean ImportData(InputStream inputStream);
    public boolean ImportData(String filePath);
}
