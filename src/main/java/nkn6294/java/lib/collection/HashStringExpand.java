package nkn6294.java.lib.collection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import nkn6294.java.lib.xml.XmlDocument;
import nkn6294.java.lib.xml.XmlNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class HashStringExpand {

    public static void Put(Map<String, List<String>> data, String content) {
        String contentConverted = content.toLowerCase();
        String charKey = content.substring(0, 1);
        if (data.containsKey(charKey)) {
            List<String> items = data.get(charKey);
            if (!items.contains(contentConverted)) {
                items.add(contentConverted);
            }
        } else {
            List<String> items = new ArrayList<>();
            items.add(contentConverted);
            data.put(charKey, items);
        }
    }

    public static void Put(String content, Map<String, Map<Integer, List<String>>> data) {
        String contentConverted = content.toLowerCase();
        String charKey = contentConverted.substring(0, 1);
        int lengthKey = contentConverted.length();
        if (data.containsKey(charKey)) {
            Map<Integer, List<String>> items = data.get(charKey);
            if (items.containsKey(lengthKey)) {
                List<String> listItem = items.get(lengthKey);
                if (!listItem.contains(contentConverted)) {
                    listItem.add(contentConverted);
                }
            } else {
                List<String> listItem = new ArrayList<>();
                listItem.add(contentConverted);
                items.put(lengthKey, listItem);
            }
        } else {
            Map<Integer, List<String>> items = new HashMap<>();
            List<String> listItem = new ArrayList<>();
            listItem.add(contentConverted);
            items.put(lengthKey, listItem);
            data.put(charKey, items);
        }
    }

    public static boolean Checker(Map<String, List<String>> data, String content) {
        String key = content.substring(0, 1).toLowerCase();
        if (data.containsKey(key)) {
            return data.get(key).contains(content.toLowerCase());
        }
        return false;
    }

    public static boolean Checker(String content, Map<String, Map<Integer, List<String>>> data) {
        String key = content.substring(0, 1).toLowerCase();
        if (data.containsKey(key) && data.get(key).containsKey(content.length())) {
            return data.get(key).get(content.length()).contains(content);
        }
        return false;
    }

    public static void ImportFromXmlNode(Map<String, List<String>> data, XmlNode xmlNode) {
        //<data><colection key="a"><value>an</value>...</collection></data>
        for (XmlNode colectionNode : xmlNode.getChildNodes()) // colectionNode
        {
            String key = colectionNode.getAttributeValue("key");
            List<String> listValue;
            if (data.containsKey(key)) {
                listValue = data.get(key);
            } else {
                listValue = new ArrayList<>();
                data.put(key, listValue);
            }
            for (XmlNode valueNode : colectionNode.getChildNodes()) // Value node.
            {
                String value = valueNode.getTextContent(); //node.innerText
                if (!listValue.contains(value)) {
                    listValue.add(value);
                }
            }
        }
    }

    public static void ImportFromXmlNode(Map<String, List<String>> data, String xmlFilePath) throws SAXException, IOException, ParserConfigurationException {
        XmlDocument xmlDocument = XmlDocument.CreatXml(xmlFilePath);
        ImportFromXmlNode(data, xmlDocument.getRootNode());
    }

    public static void ImportFromXmlNode(XmlNode xmlNode, Map<String, Map<Integer, List<String>>> data) {
//<data><colection key="a"><values length="0"><value>an</value>...</values></collection></data>
        for (XmlNode colectionNode : xmlNode.getChildNodes()) {
            String key = colectionNode.getAttributeValue("key");
            Map<Integer, List<String>> values;
            if (data.containsKey(key)) {
                values = data.get(key);
            } else {
                values = new HashMap<>();
                data.put(key, values);
            }
            for (XmlNode valuesNode : colectionNode.getChildNodes()) {

                int keyLength = Integer.parseInt(valuesNode.getAttributeValue("length"));
                List<String> listContent;
                if (values.containsKey(keyLength)) {
                    listContent = values.get(keyLength);
                } else {
                    listContent = new ArrayList<>();
                    values.put(keyLength, listContent);
                }
                for (XmlNode valueNode : valuesNode.getChildNodes()) {
                    listContent.add(valueNode.getTextContent()); //innerText
                }
            }
        }
    }

    public static void ImportFromXmlNode(String xmlFilePath, Map<String, Map<Integer, List<String>>> data) throws SAXException, IOException, ParserConfigurationException {
        XmlDocument xmlDocument = XmlDocument.CreatXml(xmlFilePath);
        ImportFromXmlNode(xmlDocument.getRootNode(), data);
    }

    public static void ExportToXmlWriter(Map<String, List<String>> data, Writer writer) {
        //<data><colection key="a"><value>an</value>...</collection></data>
        Document document = XmlDocument.creatNewDocument();
        Element dataElement = document.createElement("data");
        dataElement.setAttribute("total", data.size() + "");
        for (String key : data.keySet()) {
            List<String> values = data.get(key);
            Element colectionElement = document.createElement("colection");
            colectionElement.setAttribute("key", key);
            colectionElement.setAttribute("total", values.size() + "");
            for (String value : values) {
                Element valueElement = document.createElement("value");
                valueElement.appendChild(document.createTextNode(value));
                colectionElement.appendChild(valueElement);
            }
            dataElement.appendChild(colectionElement);
        }
        document.appendChild(dataElement);
        XmlDocument.exportXML(document.getDocumentElement(), writer);
    }

    public static void ExportToXmlWriter(Map<String, List<String>> data, String xmlFilePath) throws IOException {
        FileWriter writer = new FileWriter(xmlFilePath);
        ExportToXmlWriter(data, writer);
    }

    public static void ExportToXmlWriter(Writer writer, Map<String, Map<Integer, List<String>>> data) {
        //<data total=""><colection key="a" total="12"><values length="0" total="2"><value>an</value>...</values></collection></data>
        Document document = XmlDocument.creatNewDocument();
        Element dataElement = document.createElement("data");
        dataElement.setAttribute("total", data.size() + "");
        for (String collectionKey : data.keySet()) {
            Map<Integer, List<String>> collectionValues = data.get(collectionKey);
            Element colectionElement = document.createElement("colection");
            colectionElement.setAttribute("key", collectionKey);
            colectionElement.setAttribute("total", collectionValues.size() + "");
            for (Integer valuesKey : collectionValues.keySet()) {
                List<String> values = collectionValues.get(valuesKey);
                Element valuesElement = document.createElement("values");
                valuesElement.setAttribute("length", valuesKey + "");
                valuesElement.setAttribute("total", values.size() + "");
                for (String value : values) {
                    Element valueElement = document.createElement("value");
                    valueElement.appendChild(document.createTextNode(value));
                    valuesElement.appendChild(valueElement);
                }
                colectionElement.appendChild(valuesElement);
            }
            dataElement.appendChild(colectionElement);
        }
        document.appendChild(dataElement);
        XmlDocument.exportXML(document.getDocumentElement(), writer);
    }

    public static void ExportToXmlWriter(String xmlFilePath, Map<String, Map<Integer, List<String>>> data) throws IOException {
        FileWriter writer = new FileWriter(xmlFilePath);
        ExportToXmlWriter(writer, data);
    }
}
