package nkn6294.java.lib.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nkn6294.java.lib.collection.CollectionUtil;
import nkn6294.java.lib.common.CollectionCheckAble;
import nkn6294.java.lib.common.XmlImportAble;
import nkn6294.java.lib.xml.XmlNode;
import nkn6294.java.lib.xml.XmlNodeList;

public class MapCheckAble implements CollectionCheckAble, XmlImportAble {

    public MapCheckAble() {
        this.collection = new HashMap<>();
    }

    public MapCheckAble(Map<String, String[]> maps) {
        this.collection = maps;
    }

    public void setCollection(Map<String, String[]> maps) {
        this.collection = maps;
    }

    public Map<String, String[]> getCollection() {
        return this.collection;
    }

    @Override
    public boolean isExited(String value) {
        return CollectionUtil.Contains(collection, value);
    }

    private Map<String, String[]> collection;
    private Comparator<String> comparator;

    @Override
    public boolean importData(XmlNode node) {
        if (node.getNodeType() == XmlNode.XmlNodeType.ELEMENT_NODE) {
            collection.clear();
            XmlNodeList items = node.getElementsByTagName("item");
            this.updateComparator();
            for (XmlNode item : items) {
                String key = item.getAttributeValue("key");
                if (key != null && key.length() > 0) {
                    if (item.isOnlyTextInner()) {
                        String[] values = item.getTextContent().split(" ");
                        List<String> strings = new ArrayList<>();
                        for (String str : values) {
                            if (str.startsWith(key)) {
                                strings.add(str.trim());
                            }
                        }
//                        Arrays.sort(values, this.comparator);
//                        collection.put(key, values);

                        collection.put(key, (String[]) strings.toArray(new String[strings.size()]));
                    }
                }
            }
            this.onDataChange(this.collection);
            return true;
        }
        return false;
    }

    @Override
    public String getStartString(String string) {
        if (string == null) {
            return null;
        }
        Set<String> keys = this.collection.keySet();
        for (String key : keys) {
            if (string.startsWith(key)) {
                String[] items = this.collection.get(key);
                for (String item : items) {
                    if (string.startsWith(item)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onDataChange(Object data) {
        this.updateComparator();
        for (String key : this.collection.keySet()) {
            Arrays.sort(this.collection.get(key), this.comparator);
        }
    }

    private void updateComparator() {
        if (this.comparator == null) {
            this.comparator = new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return Integer.compare(str2.length(), str1.length());
                }
            };
        }
    }
}
