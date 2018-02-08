package nkn6294.java.lib.checker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nkn6294.java.lib.collection.CollectionUtil;

public class CheckerManager implements Checker, Iterable<Checker> {

    public CheckerManager(Checker... checkers) {
        this.checkers = new ArrayList<>();
        CollectionUtil.Combine(this.checkers, checkers);
    }

    public CheckerManager(List<Checker> checkers) {
        this.checkers = new ArrayList<>();
        CollectionUtil.Combine(this.checkers, checkers);
    }

    @Override
    public boolean isInvalid(String string) {
        String str = this.preProcessing(string);
        if(str.length() == 0) {
            return true;
        }
        for (Checker checker : checkers) {
            if (checker.isInvalid(str)) {
                return true;
            }
        }
        return false;
    }

    public String preProcessing(String string) {
        return string;
    }
    
    @Override
    public Iterator<Checker> iterator() {
        return this.checkers.iterator();
    }

    public boolean add(Checker checker) {
        return this.checkers.add(checker);
    }

    private final List<Checker> checkers;
}
