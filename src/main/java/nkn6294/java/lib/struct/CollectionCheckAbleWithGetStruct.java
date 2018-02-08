package nkn6294.java.lib.struct;

import java.util.HashMap;
import java.util.Map;

import nkn6294.java.lib.common.CollectionCheckAble;
import nkn6294.java.lib.delegate.Action;


public class CollectionCheckAbleWithGetStruct implements CollectionCheckAble, StructStringAble {

    public CollectionCheckAbleWithGetStruct() {
        this.collection = new HashMap<>();
        this.isCheckInAllCollection = true;
    }

    @Override
    public boolean isExited(String value) {
        for (String key : this.collection.keySet()) {
            if (this.collection.get(key).isExited(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean Add(CollectionCheckAble collectionCheckAble, String key) {
        if (!this.collection.containsKey(key)) {
            this.collection.put(key, collectionCheckAble);
            return true;
        }
        return false;
    }

    public CollectionCheckAble addOrReplace(CollectionCheckAble collectionCheckAble, String key) {
        return this.collection.put(key, collectionCheckAble);
    }

    public boolean remove(String key) {
        return this.collection.remove(key) != null;
    }

    @Override
    public String getStartString(String string) {
        return this.getStartString(string, null);
    }

    public String getStartString(String string, Action<String> action) {
        if (string == null) {
            return null;
        }
        String str = null;
        String currentKey = null;
        for (String key : this.collection.keySet()) {
            String sItem = this.collection.get(key).getStartString(string);
            if (sItem != null) {
                if (!this.isCheckInAllCollection) {
                    if (action != null) {
                        action.action(sItem, key);
                    }
                    return sItem;
                }
                if (str == null) {
                    str = sItem;
                    currentKey = key;
                } else if (sItem.length() > str.length()) {
                    str = sItem;
                    currentKey = key;
                }
            }
        }
        if (action != null) {
            action.action(str, currentKey);
        }
        return str;
    }

    public void setCheckInAllCollection(boolean value) {
        this.isCheckInAllCollection = value;
    }

    @Override
    public String getStructString(String string) {
        final StringBuilder builder = new StringBuilder();
        Action<String> action = new Action<String>() {
            @Override
            public void action(String... values) {
                if (values.length == 2 && values[0] != null) { //start, key
                    builder.append("[").append(values[1])
//                            .append("(").append(values[0]).append(")")
                            .append("]");
                }
            }
        };
        String str = getStartString(string, action);
        if (str != null) {
            String subString = string.substring(str.length());
            if (subString.length() > 0) {
                builder.append(getStructString(subString));
            }
        } else {
            builder.append("[*]");
            String subString = string.substring(1);
            if (subString.length() > 0) {
                builder.append(getStructString(subString));
            }
        }
        return builder.toString();
    }

    @Override
    public void onDataChange(Object data) {
    }

    private final Map<String, CollectionCheckAble> collection;
    private boolean isCheckInAllCollection;

}
