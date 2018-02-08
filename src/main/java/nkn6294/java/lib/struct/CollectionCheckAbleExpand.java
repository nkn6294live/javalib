package nkn6294.java.lib.struct;

import nkn6294.java.lib.common.CollectionCheckAble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CollectionCheckAbleExpand implements Iterable<CollectionCheckAble>,  CollectionCheckAble {

    public CollectionCheckAbleExpand() {
        this.listCollectionCheck = new ArrayList<>();
    }

    public CollectionCheckAbleExpand(CollectionCheckAble... lists) {
        this.listCollectionCheck = new ArrayList<>();
        this.listCollectionCheck.addAll(Arrays.asList(lists));
    }

    public void Add(CollectionCheckAble item) {
        if (!this.listCollectionCheck.contains(item)) {
            this.listCollectionCheck.add(item);
        }
    }

    @Override
    public Iterator<CollectionCheckAble> iterator() {
        return this.listCollectionCheck.iterator();
    }
    
    @Override
    public boolean isExited(String value) {
        for (CollectionCheckAble item : this.listCollectionCheck) {
            if (item.isExited(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getStartString(String string) {
        String str = null;
        for (CollectionCheckAble item : this.listCollectionCheck) {
            String sItem = item.getStartString(string);
            if (sItem != null) {
                if (str == null) {
                    str = sItem;
                } else if (sItem.length() > str.length()) {
                    str = sItem;
                }
            }
        }
        return str;
    }

    @Override
    public void onDataChange(Object data) { //item in ListCollection
    }
    
    private final List<CollectionCheckAble> listCollectionCheck;
}
