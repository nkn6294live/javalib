package nkn6294.java.lib.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlNodeList implements Iterable<XmlNode> {

    public XmlNodeList(XmlNode parentNode, NodeList nodeList) {
        if (nodeList != null) {
            this.nodes = getXmlNodes(parentNode, nodeList);
        } else {
            this.nodes = new ArrayList<>();
        }
    }

    public XmlNodeList(List<XmlNode> nodes) {
        if (nodes == null) {
            this.nodes = new ArrayList<>();
        } else {
            this.nodes = nodes;
        }
    }

    public XmlNode item(int index) {
        try {
            return this.nodes.get(index);
        } catch (Exception ex) {
            return null;
        }
    }

    public XmlNode first() {
        return this.item(0);
    }

    public XmlNode last() {
        return this.item(this.getLength() - 1);
    }

    @Override
    public Iterator<XmlNode> iterator() {
        return this.nodes.iterator();
    }

    public int getLength() {
        return this.nodes.size();
    }

    private final List<XmlNode> nodes;

    private static List<XmlNode> getXmlNodes(XmlNode parentNode, NodeList nodeList) {
        List<XmlNode> xmlNodes = new ArrayList<>();
        if (nodeList != null) {
            int length = nodeList.getLength();
            if (length == 1) {
                try {
                    xmlNodes.add(new XmlNode(parentNode, nodeList.item(0)));
                } catch (Exception ex) {
                }
            } else {
                for (int index = 0; index < length; index++) {
                    try {
                        Node node = nodeList.item(index);
                        if (node.getNodeType() == Node.ELEMENT_NODE || (node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().length() > 0)) {
                            xmlNodes.add(new XmlNode(parentNode, nodeList.item(index)));
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return xmlNodes;
    }

}
