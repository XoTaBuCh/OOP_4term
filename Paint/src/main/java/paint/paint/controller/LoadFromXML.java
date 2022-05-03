
package paint.paint.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import paint.paint.model.Shape;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadFromXML {
    private String path;
    private ArrayList myList = new ArrayList<Shape>();
    private HashMap m;
    private boolean success = false;

    public LoadFromXML(String path) throws SAXException, ParserConfigurationException, IOException {
        this.path = path;
        doTheJob();
    }

    private void doTheJob() throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom;
        try {
            dom = db.parse(path);
        } catch (Exception e) {
            success = false;
            return;
        }
        Element doc = dom.getDocumentElement();

        Node x = doc.getFirstChild();
        x = x.getNextSibling();

        try {
            do {
                if (x.getNodeName().equals("#text")) {
                    x = x.getNextSibling();
                    continue;
                }
                NamedNodeMap tempm = x.getAttributes();
                m = new HashMap<String, Double>();
                for (int i = 0; i < tempm.getLength(); i++) {
                    addToMap(tempm.item(i).toString());
                }
                copyMapToList(x.getNodeName());
                x = x.getNextSibling();
            } while (!x.getNextSibling().equals(null));
        } catch (Exception e) {
        }
        success = true;

    }

    private void copyMapToList(String type) {
        Shape temp = new ShapeFactory().createShape(type, m);
        myList.add(temp);

    }

    private void addToMap(String s) {
        int ind = s.indexOf('=');
        String key = s.substring(0, ind);
        double val = Double.parseDouble(s.substring(ind + 2, s.length() - 1));
        //System.out.println(key + " " +val);
        m.put(key, val);
    }

    public ArrayList<Shape> getList() {
        return myList;
    }

    public boolean checkSuccess() {
        return success;
    }
}
