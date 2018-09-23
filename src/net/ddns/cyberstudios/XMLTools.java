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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLTools {

/*    public static void main(String[] args) {
        File f = null;
        try {
            f = new File(XMLTools.class.getResource("/rec-h.xml").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (!f.exists()) {
            System.out.printf("No file!\n");
            return;
        }
        Element element = XMLTools.parseDocument(f);
        if (element == null) {
            System.out.printf("Element == null!\n");
            return;
        }
        List<String> map = new ArrayList<>();
        map = XMLTools.flatten(element);
        System.out.printf("Done\n");
    }*/

    /**
     * Text representation of document
     *
     * @param newDoc DOM document
     * @return returns text representation of string
     */
    public static String toString(Document newDoc) {
        try {
            DOMSource domSource = new DOMSource(newDoc);
            Transformer transformer;
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
     * @param xml_file File object pointing to XML file
     * @return returns root Element parsed from DOM document
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
     * @param xml_document DOM document of XML elements to be parsed
     * @return root element parsed from DOM document
     */
    public static Element parseDocument(Document xml_document) {
        return parse(xml_document.getDocumentElement(), null);
    }

    /**
     * Creates {@link Element} from {@link org.w3c.dom.Element}
     *
     * @param root   root element of DOM document to be parsed
     * @param parent parent element used to create and set children
     * @return root element of DOM document as a parsed element
     */
    public static Element parse(org.w3c.dom.Element root, Element parent) {
        Element element = new Element(root.getTagName(), parent);
        for (int i = 0; i < root.getAttributes().getLength(); i++) {
            Attr item = (Attr) root.getAttributes().item(i);
            String attrKey = item.getNodeName();
            String attrValue = "" + item.getNodeValue() + "";
            element.addAttribute(attrKey, attrValue);
        }
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element e = (org.w3c.dom.Element) item;
                Element childNode = parse(e, element);
                element.addChild(childNode);
            } else if (item.getNodeType() == Node.TEXT_NODE) {
                String value = item.getNodeValue().replace("\n", "").replace("\"", "").trim();
                if (!value.isEmpty()) {
                    element.setValue(value);
                }
            }
        }
        return element;
    }

    /**
     * Searches DOM for element having a specific ID
     *
     * @param root root element of parsed document to be searched
     * @param id   ID of element to be found
     * @return element matching ID specified
     */
    public static Element lookup(Element root, String id) {
        if (root.getId().equals(id))
            return root;
        else {
            for (Element element : root.getChildren()) {
                Element e = lookup(element, id);
                if (e != null)
                    return e;
            }
        }
        return null;
    }

    /**
     * Flattens with isComponent = true
     * @param rootElement
     * @return
     */
    public static List<String> flatten(Element rootElement){
        return flatten(rootElement, true);
    }

    /**
     * Flattens an element by returning a list of all component ID's
     * @param rootElement   root element
     * @return list of all ID's as children of the element
     */
    public static List<String> flatten(Element rootElement, boolean isComponent){
        Stream<Element> stream = rootElement.getChildren().stream();
        if (isComponent) {
            stream = stream.filter(Element::isComponent);
        }
        List<String> collect = stream.filter(Element::hasId).map(Element::getId).collect(Collectors.toList());
        rootElement.getChildren().forEach(element -> collect.addAll(flatten(element)));
        return collect;
    }

}




































