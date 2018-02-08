package nkn6294.java.lib.xml;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

public class XmlNode {

    public enum XmlNodeType {
        NONE, //Khong phai type = 0
        ELEMENT_NODE, //1
        ATTRIBUTE_NODE, //2
        TEXT_NODE, //3
        CDATA_SECTION_NODE, //4
        ENTITY_REFERENCE_NODE, //5
        ENTITY__NODE, //6
        PROCESSING_INSTRUCTION_NODE, //7
        COMMENT_NODE, //8
        DOCUMENT_NODE, //9
        DOCUMENT_TYPE_NODE, //10
        DOCUMENT_FRAMGMENT_NODE, //11
        NOTATION_NODE                   //12
    }

    static {
        NODE_TYPE = XmlNodeType.values();
    }

    public static XmlNodeType getNodeType(Node node) {
        return XmlNodeType.values()[node.getNodeType()];
    }

    public XmlNode(XmlNode parentNode, Node node) throws Exception {
        if (node != null && (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.TEXT_NODE)) {
            this.node = node;
            this.attributes = new XmlAttributeList(this, node.getAttributes());
            this.xmlNodeList = new XmlNodeList(this, this.node.getChildNodes());
            this.parentNode = parentNode;
            Node nodeNext = node.getNextSibling();
            if (nodeNext != null && nodeNext.getNodeType() == Node.ELEMENT_NODE) {
                this.nextSiblingNode = new XmlNode(this.parentNode, nodeNext);
            } else {
                this.nextSiblingNode = null;
            }
        } else {
            throw new Exception("Node input must be have type ELEMENT_NODE or TEXT_NODE");
        }
    }

    public String getNodeName() {
        return this.node.getNodeName();
    }

    public XmlNodeList getElementsByTagName(String tagName) {
        List<XmlNode> nodes = new ArrayList<>();
        if (tagName != null && this.xmlNodeList != null) {
            for (XmlNode item : this.xmlNodeList) {
                if (item.getNodeType() == XmlNodeType.ELEMENT_NODE && tagName.equals(item.getNodeName())) {
                    nodes.add(item);
                }
            }
        }
        return new XmlNodeList(nodes);
    }

    public String getNodeValue() {
        String value = this.node.getNodeValue();
        return value == null ? null : value.trim();
    }

    public String getTextContent() {
        String value = this.node.getTextContent();
        return value == null ? null : value.trim();
    }
    public boolean isEmptyNode() {
        return this.getNodeType() == XmlNodeType.ELEMENT_NODE && this.getNodeValue() == null;
    }
    
    public boolean isOnlyTextInner() {
        return this.getNodeType() == XmlNodeType.ELEMENT_NODE && this.getFirstChild().getNodeType() == XmlNodeType.TEXT_NODE;
    }
    public void setNodeValue(String value) {
        this.node.setNodeValue(value);
    }

    public XmlNodeType getNodeType() {
        return NODE_TYPE[this.node.getNodeType()];
    }

    public XmlAttributeList getAttributes() {
        return this.attributes;
    }

    public String getAttributeValue(String name) {
        return this.attributes.item(name);
    }

    public XmlNodeList getChildNodes() {
        return this.xmlNodeList;
    }

    public XmlNode getFirstChild() {
        return this.xmlNodeList.first();
    }

    public XmlNode getLastChild() {
        return this.xmlNodeList.last();
    }

    public int getLength() {
        return this.xmlNodeList.getLength();
    }

    public XmlNode getParentNode() {
        return this.parentNode;
    }

    public XmlNode getNextSibling() {
        return this.nextSiblingNode;
    }

    private final Node node;
    private final XmlAttributeList attributes;
    private final XmlNodeList xmlNodeList;
    private final XmlNode parentNode;
    private final XmlNode nextSiblingNode;
    private final static XmlNodeType[] NODE_TYPE;
}
