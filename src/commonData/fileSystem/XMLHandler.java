package commonData.fileSystem;

import commonData.modelHandlers.ElementBuilderHelper;
import commonData.exceptions.WrongDataInputException;
import commonData.exceptions.WrongTagException;
import lombok.Getter;
import lombok.Setter;
import server.model.LinkedListStorage;
import commonData.data.StudyGroup;
import server.model.interfaces.IStore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * XMLHandler handles xml file and get it's input.
 */
public class XMLHandler {
    @Getter
    @Setter
    private static Node node;
    public static IStore<StudyGroup> getStorageFromXML() throws Throwable, IOException, SAXException, WrongDataInputException, ReflectiveOperationException, WrongTagException, ParserConfigurationException {
        File file = new File(ScriptHandler.getFileName());
        if (file.toString().equals("")){
            return new LinkedListStorage<StudyGroup>();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        documentBuilder.setErrorHandler(new NullErrorHandler());
        Document doc = documentBuilder.parse(file);
        IStore<StudyGroup> storage = new LinkedListStorage<>();
        node = doc.getFirstChild();
        NodeList rootChildren = node.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++){
            if (rootChildren.item(i).getNodeType() == Node.ELEMENT_NODE){
                StudyGroup studyGroup = ElementBuilderHelper.buildElement(StudyGroup.class, "studyGroup", "");
                storage.add(studyGroup);
            }
        }
        return storage;
    }

    public static Node getNextNode(){
        node = getNextNode(node);
        return node;
    }
    public static String getValue(){
        return node.getTextContent();
    }
    private static Node getElementSiblingNode(Node node){
        do {
            if (node.getNextSibling() == null) return node;
            node = node.getNextSibling();
        } while (node.getNodeType() != Node.ELEMENT_NODE);
        return node;
    }
    private static Node getChildNode(Node node){
        if (node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) return node.getFirstChild();
        return getElementSiblingNode(node.getFirstChild());
    }
    private static boolean isLastSibling(Node node, Node nextNode){
        return (nextNode.getNextSibling() == null && nextNode.getNodeType() != Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() == Node.ELEMENT_NODE);
    }

    public static Node getNextNode(Node node){
        if (node.getChildNodes().getLength() > 1){
            return getChildNode(node);
        }
        Node nextNode = getElementSiblingNode(node);
        if ((nextNode.getNextSibling() == null && nextNode.getNodeType() != Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() == Node.ELEMENT_NODE)){
            Node parNode;
            do{
                parNode = node.getParentNode();
                node = getElementSiblingNode(parNode);
            } while ((parNode.equals(node) && node.getNodeType() == Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() != Node.ELEMENT_NODE));
            return node;
        }
        node = nextNode;
        return node;
    }

}
