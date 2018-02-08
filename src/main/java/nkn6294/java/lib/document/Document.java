package nkn6294.java.lib.document;

public interface Document extends Iterable<Chapter>{
    public String getTitle();
    public Chapter getChapter(int index);
}
