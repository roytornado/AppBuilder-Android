package com.royalnext.base.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Vector;

public class BaseXMLHandler extends DefaultHandler {
    private String itemvalue = "";
    private Vector<String> nodeStack = new Vector<String>();

    public BaseXMLHandler() {
    }

    public String getItemvalue() {
        return itemvalue.trim();
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
        nodeStack.clear();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        nodeStack.add(localName);
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        nodeStack.remove(nodeStack.size() - 1);
        itemvalue = "";
    }

    @Override
    public final void characters(char ch[], int start, int length) {
        String cdata = "";
        for (int i = 0; i < length; i++) {
            cdata += (ch[start + i]);
        }

        /*
         * if (cdata.trim().length() == 0) return;
         */
        this.itemvalue = this.itemvalue + cdata;
    }

    protected String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }

    public boolean inside(String parentName) {
        String temp = "";
        for (String node : nodeStack) {
            temp += "." + node;
        }
        temp += ".";

        if (temp.contains(parentName)) {
            return true;
        }
        return false;
    }

    public boolean isNode(String target) {
        String temp = "";
        for (String node : nodeStack) {
            temp += "." + node;
        }
        temp += ".";

        if (temp.endsWith(target)) {
            return true;
        }
        return false;
    }

    public boolean isParent(String parentName) {
        if (nodeStack.size() >= 2 && nodeStack.get(nodeStack.size() - 2).equalsIgnoreCase(parentName)) {
            return true;
        }
        return false;
    }

}
