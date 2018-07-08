package net.ddns.cyberstudios;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class XMLTools {

    public static void main(String[] args) {

    }

    /**
     * Text representation of document
     *
     * @param newDoc
     * @return
     */
    public static String toString(Document newDoc) {
        try {
            DOMSource domSource = new DOMSource(newDoc);
            Transformer transformer = null;
            transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            return sw.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return "<root/>";
    }

    /**
     * Reads file, parses document and constructs a readable and managable {@link Element} hierachy to easily traverse
     *
     * @param xml_file
     * @return
     */
    public static Element parseDocument(File xml_file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml_file);
            return parseDocument(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads document and constructs a readable and managable {@link Element} hierachy to easily traverse
     *
     * @param xml_document
     * @return
     */
    public static Element parseDocument(Document xml_document) {
        return parse(xml_document.getDocumentElement(), null);
    }

    public static Element parse(org.w3c.dom.Element root, Element parent) {
        Element element = new Element(root.getTagName(), parent);
        for (int i = 0; i < root.getAttributes().getLength(); i++) {
            Attr item = (Attr) root.getAttributes().item(i);
            String attrKey = item.getNodeName();
            String attrValue = "\"" + item.getNodeValue() + "\"";
            element.addAttribute(attrKey, attrValue);
        }
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element e = (org.w3c.dom.Element) item;
                String tag = e.getTagName();
                Element childNode = parse(e, element);
                if (tag.equals("component")) {
                    if (item.getFirstChild() != null && item.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                        childNode.setValue(item.getFirstChild().getNodeValue());
                    }
                }
                element.addChild(childNode);
            }
        }
        return element;
    }
}




































