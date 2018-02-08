package nkn6294.java.lib.struct;

import java.util.ArrayList;
import java.util.List;

import nkn6294.java.lib.collection.CollectionUtil;
import nkn6294.java.lib.common.CollectionCheckAble;
import nkn6294.java.lib.common.XmlImportAble;
import nkn6294.java.lib.xml.XmlNode;
import nkn6294.java.lib.xml.XmlNodeList;

public final class ListCheckAble implements CollectionCheckAble, XmlImportAble {

    public ListCheckAble() {
        this.collection = new ArrayList<>();
    }

    public ListCheckAble(List<String> collection) {
        this.setCollection(collection);
    }

    public void setCollection(List<String> maps) {
        this.collection = maps;
        this.onDataChange(maps);
    }

    public List<String> getCollection() {
        return this.collection;
    }

    private List<String> collection;

    @Override
    public boolean isExited(String value) {
        return CollectionUtil.Contains(collection, value);
    }

    @Override
    public boolean importData(XmlNode node) {
        if (node.getNodeType() == XmlNode.XmlNodeType.ELEMENT_NODE) {
            collection.clear();
            XmlNodeList items = node.getChildNodes();
            for (XmlNode item : items) {
                if (item.isOnlyTextInner()) {
                    collection.add(item.getTextContent());
                }
            }
            this.onDataChange(this.collection);
            return true;
        }
        return false;
    }


    @Override
    public String getStartString(String string) {
        return null;
    }

    @Override
    public void onDataChange(Object data) {
    }
    
}
