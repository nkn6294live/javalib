package nkn6294.java.lib.cache;

import nkn6294.java.lib.common.StreamExportAble;
import nkn6294.java.lib.common.StreamImportAble;

public interface Cache<T> extends StreamImportAble, StreamExportAble {
    public boolean isExited(T item); //exited in cached.
    public boolean insert(T item); // insert if not exited.
    public boolean reset();
}
