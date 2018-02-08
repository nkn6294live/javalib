package nkn6294.java.lib.xml;

import org.w3c.dom.Node;

public class XmlAttribute {

    public XmlAttribute(XmlNode parentNode, Node node) throws Exception {
        if (node != null && node.getNodeType() == Node.ATTRIBUTE_NODE) {
            this.node = node;
            this.parentNode = parentNode;
            this.name = this.node.getNodeName();
            this.value = this.node.getNodeValue();
        } else {
            throw new Exception("Node input must be have type ATTRIBUTE_NODE");
        }
    }

    public String getTextContent() {
        return this.node.getTextContent();
    }
    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public XmlNode getParentNode() {
        return this.parentNode;
    }

    private final String name;
    private String value;
    private final Node node;
    private final XmlNode parentNode;
}
