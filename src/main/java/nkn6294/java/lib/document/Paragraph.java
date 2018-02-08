package nkn6294.java.lib.document;

public interface Paragraph extends Iterable<WordElement> {
    public Chapter getChapter();
    public String getContent();
    public int getIndex();
}
