package nkn6294.java.lib.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.NamedNodeMap;

public class XmlAttributeList implements Iterable<XmlAttribute> {

    public XmlAttributeList(XmlNode parentNode, NamedNodeMap nameNodeList) {
        if (nameNodeList != null) {
            this.attributes = getXmlAttribute(parentNode, nameNodeList);
        } else {
            this.attributes = new ArrayList<>();
        }
    }

    public XmlAttributeList(List<XmlAttribute> attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        } else {
            this.attributes = new ArrayList<>();
        }
    }

    public XmlAttribute item(int index) {
        try {
            return this.attributes.get(index);
        } catch (Exception ex) {
            return null;
        }
    }

    public String item(String name) {
        for (XmlAttribute attribute : this.attributes) {
            if (attribute.getName().equals(name)) {
                return attribute.getValue();
            }
        }
        return null;
    }

    public XmlAttribute first() {
        return this.item(0);
    }

    public XmlAttribute last() {
        return this.item(this.getLength() - 1);
    }

    @Override
    public Iterator<XmlAttribute> iterator() {
        return this.attributes.iterator();
    }

    public int getLength() {
        return this.attributes.size();
    }

    public boolean Contains(String key) {
        return this.item(key) != null;
    }

    private final List<XmlAttribute> attributes;

    private static List<XmlAttribute> getXmlAttribute(XmlNode parentNode, NamedNodeMap nameNodeList) {
        List<XmlAttribute> xmlNodes = new ArrayList<>();
        if (nameNodeList != null) {
            for (int index = 0; index < nameNodeList.getLength(); index++) {
                try {
                    xmlNodes.add(new XmlAttribute(parentNode, nameNodeList.item(index)));
                } catch (Exception ex) {
                }
            }
        }
        return xmlNodes;
    }

}
