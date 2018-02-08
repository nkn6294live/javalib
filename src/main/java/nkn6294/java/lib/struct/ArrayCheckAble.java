package nkn6294.java.lib.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nkn6294.java.lib.collection.CollectionUtil;
import nkn6294.java.lib.common.CollectionCheckAble;
import nkn6294.java.lib.common.XmlImportAble;
import nkn6294.java.lib.xml.XmlNode;


public final class ArrayCheckAble implements CollectionCheckAble, XmlImportAble {

    public ArrayCheckAble() {
        this.values = null;
    }

    public ArrayCheckAble(String[] values) {
        this.setCollection(values);
    }

    public ArrayCheckAble(List<String> values) {
        this.setCollection((String[]) values.toArray());
    }

    public String[] getCollection() {
        return values;
    }

    public void setCollection(String[] values) {
        this.values = values;
        this.onDataChange(this.values);
    }

    @Override
    public boolean isExited(String value) {
        if (this.values == null) {
            return false;
        } else {
            return CollectionUtil.Contains(values, value);
        }
    }
    private String[] values;
    private Comparator<String> comparator;

    @Override
    public boolean importData(XmlNode node) {
        if (node.isOnlyTextInner()) {
            String[] values = node.getTextContent().split(" ");
            List<String> strings = new ArrayList<>();
            for (String str : values) {
                if (str.trim().length() > 0) {
                    strings.add(str.trim());
                }
            }
            this.setCollection(strings.toArray(new String[strings.size()]));
            return true;
        }
        return false;
    }

    @Override
    public String getStartString(String string) {
        if (string == null) {
            return null;
        }
        for (String item : this.values) {
            if (item.length() == 0) {
                continue;
            }
            if (string.startsWith(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void onDataChange(Object data) { // data == values
        if (this.values != null) {
            this.updateComparator();
            Arrays.sort(this.values, this.comparator);
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
