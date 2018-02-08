package nkn6294.java.lib.document;

import nkn6294.java.lib.delegate.Function;

public interface DocumentReader {
    public String nextWord() throws Exception;
    public String nextParagraph() throws Exception;
    public String nextParagraph(Function<String, String> convert) throws Exception;
    public String nextTitle();
    public void close() throws Exception;
}
