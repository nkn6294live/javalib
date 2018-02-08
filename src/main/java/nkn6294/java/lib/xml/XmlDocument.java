package nkn6294.java.lib.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class XmlDocument {

	public static XmlDocument CreatXml(InputStream inputStream)
			throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringElementContentWhitespace(true);
		return new XmlDocument(dbFactory.newDocumentBuilder()
				.parse(inputStream));

	}

	public static XmlDocument CreatXml(String uri) throws SAXException,
			IOException, ParserConfigurationException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		return new XmlDocument(dbBuilder.parse(uri));

	}

	public static XmlDocument LoadXml(String content) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringElementContentWhitespace(true);
		return new XmlDocument(dbFactory.newDocumentBuilder().parse(
				new ByteArrayInputStream(content.getBytes())));
	}

	public static Transformer getTransform() {
		init();
		return transformer;
	}

	public static Transformer getNewTransform() {
		init();
		if (transformerFactory == null) {
			try {
				Transformer new_transformer = transformerFactory
						.newTransformer();
				new_transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				new_transformer.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "2");
				return new_transformer;
			} catch (TransformerConfigurationException ex) {
				System.out.println("CreateTransformer_Error" + ex.getMessage());
			}
		}
		return null;
	}

	public static boolean exportXML(Node node, Transformer transformer,
			OutputStream output) {
		try {
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, Transformer transformer,
			Writer output) {
		try {
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, Transformer transformer,
			File output) {
		try {
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, OutputStream output) {
		try {
			init();
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, Writer output) {
		try {
			init();
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, File output) {
		try {
			init();
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
			return true;
		} catch (Exception ex) {
//			System.out.println("ExportXml_Error:" + ex.getMessage());
			return false;
		}
	}

	public static boolean exportXML(Node node, String output) {
		return exportXML(node, new File(output));
	}

	public static Document creatNewDocument() {
		init();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringElementContentWhitespace(true);
		Document document;
		try {
			document = dbFactory.newDocumentBuilder().newDocument();
			return document;
		} catch (ParserConfigurationException e) {
			return null;
		}
		
//		return null;
	}

	private XmlDocument(Document doc) {
		this.doc = doc;
	}

	public XmlNode getRootNode() {
		try {
			return new XmlNode(null, doc.getDocumentElement());
		} catch (Exception ex) {
			return null;
		}
	}

	public boolean exportData(OutputStream output) {
		return exportXML(this.doc, output);
	}

	public boolean exportData(Writer output) {
		return exportXML(this.doc, output);
	}

	public boolean exportData(File output) {
		return exportXML(this.doc, output);
	}

	public boolean exportData(String output) {
		return exportXML(this.doc, output);
	}

	public Document getDocument() {
		return this.doc;
	}

	private final Document doc;
//	private static DocumentBuilderFactory dbFactory;
//	private static DocumentBuilder dBuilder;
	private static TransformerFactory transformerFactory;
	private static Transformer transformer;

	private static void init() {
		// if (dbFactory == null) {
		// dbFactory = DocumentBuilderFactory.newInstance();
		// dbFactory.setIgnoringElementContentWhitespace(true);
		// }
		// if (dBuilder == null) {
		// try {
		// dBuilder = dbFactory.newDocumentBuilder();
		// } catch (ParserConfigurationException e) {
		// System.out.println("CreateDocumentBuilder_Error" + e.getMessage());
		// }
		// }
		if (transformerFactory == null) {
			transformerFactory = TransformerFactory.newInstance();
		}
		if (transformer == null) {
			try {
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "2");
			} catch (TransformerConfigurationException ex) {
				System.out.println("CreateTransformer_Error" + ex.getMessage());
			}
		}
	}
}
