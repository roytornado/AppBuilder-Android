package com.midland.base.xml;

import com.midland.base.util.Common;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SimpleXMLHandler extends BaseXMLHandler {
    private String tag = "";
    public String result = "";

    public SimpleXMLHandler() {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (isNode(tag)) {
            result = getItemvalue();
        }

        super.endElement(namespaceURI, localName, qName);
    }

    public static String parseXMLResult(String xmlString, String tag) {
        try {
            SimpleXMLHandler handler = new SimpleXMLHandler();
            handler.tag = tag;
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(handler);
            xr.parse(new InputSource(new StringReader(xmlString)));
            return handler.result;
        } catch (Exception e) {
            Common.e(e);
        }
        return null;
    }

}
