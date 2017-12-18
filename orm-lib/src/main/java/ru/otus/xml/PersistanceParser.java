package ru.otus.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

public class PersistanceParser {
    public static Map<String, String> parse(String persistanceUnit, String filename) throws IllegalArgumentException {
        if (persistanceUnit == null || filename == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        Map<String, String> result = new HashMap<>();
        Document doc = null;
        try {
            doc = createParser(filename);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException | SAXException e) {
            throw new IllegalArgumentException("Wrong XML path!");
        }

        if (doc != null) {
            try {
                XPathExpression xPath = createXPath("//persistence-unit[@name='"+persistanceUnit+"']/properties/*");
                NodeList nodeList = (NodeList)(xPath.evaluate(doc, XPathConstants.NODESET));
                for (int i =0; i < nodeList.getLength();i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeName().equals("property")) {
                        String key = node.getAttributes().getNamedItem("name").getNodeValue();
                        String value = node.getAttributes().getNamedItem("value").getNodeValue();
                        result.put(key, value);
                    }
                }
                return result;
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Document createParser(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new MyErrorHandler(System.out));

        return builder.parse(getResource(filename));
    }

    private static XPathExpression createXPath(String query) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return xpath.compile(query);
    }

    private static String getResource(String filename) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(filename);
        if (resource != null) {
            return resource.getFile();
        } else
            throw new IOException();
    }

}
