package ru.otus.persistence.xml;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.IOException;
import java.net.URL;

import java.util.*;

public class PersistenceParams {

    private Map<ParamTypes, Object> parameters = new HashMap<>();

    private enum ParamTypes {
        CONNECTION,
        CLASSES
    }

    @SuppressWarnings("unchecked")
    public PersistenceParams(Map parameters) {
        this.parameters = (Map<ParamTypes, Object>) parameters;
    }

    public PersistenceParams(@NotNull String persistenceUnit,
                             @NotNull String filename) throws IllegalArgumentException {
        Document doc = null;
        try {
            doc = createParser(filename);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException | SAXException e) {
            throw new IllegalArgumentException("Wrong XML path!");
        }

        if (doc == null)
            throw new IllegalStateException("doc is null");

        try {
            XPathExpression xPath = createXPath("//persistence-unit[@name='" + persistenceUnit + "']/properties/*");
            parameters.put(ParamTypes.CONNECTION, getProperties((NodeList) xPath.evaluate(doc, XPathConstants.NODESET)));

            xPath = createXPath("//persistence-unit[@name='" + persistenceUnit + "']/class");
            parameters.put(ParamTypes.CLASSES, getEntityClasses((NodeList) xPath.evaluate(doc, XPathConstants.NODESET)));

            parameters = Collections.unmodifiableMap(parameters);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    public Map<ParamTypes, Object> getParameters() {
        return parameters;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getConnectionData() {
        return (Map<String, String>) parameters.get(ParamTypes.CONNECTION);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getEntityClasses() {
        return (Set<String>) parameters.get(ParamTypes.CLASSES);
    }

    private Set<String> getEntityClasses(NodeList nodeList) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeName().equals("class")) {
                String value = node.getTextContent();
                result.add(value);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private Map<String, String> getProperties(NodeList nodeList) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeName().equals("property")) {
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                String value = node.getAttributes().getNamedItem("value").getNodeValue();
                result.put(key, value);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private Document createParser(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new MyErrorHandler(System.out));

        return builder.parse(getResource(filename));
    }

    private XPathExpression createXPath(String query) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return xpath.compile(query);
    }

    private String getResource(String filename) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(filename);
        if (resource != null) {
            return resource.getFile();
        } else
            throw new IOException();
    }
}
