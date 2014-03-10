/* ************************************************************************  
 * Copyright © 2013 Maite Calpe Miravet 
 * ************************************************************************ 
 * This file is part of OpenDomo Notifier.
 *
 * OpenDomo Notifier is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License v3 
 * published by the Free Software Foundation.
 *
 * OpenDomo Notifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3 for more details.
 *
 * You should have received a copy of the GNU General Public License v3
 * along with OpenDomo Notifier. If not, see <http://www.gnu.org/licenses/>
 * ************************************************************************/

package com.opendomo.notifier;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

/**
 * Analizador del XML que incluye los eventos OpenDomo.<p> 
 * 
 * Clase que analiza la respuesta XML resultante de la petición HTTP 
 * de consulta de eventos al servidor OpenDomo y devuelve una lista
 * de objetos de tipo Event.
 * 
 * @see Event
 * @see EventsList
 * @version  1.0, 30/05/2013
 */

public class EventsXmlParser {
	
	private static final String ns = null;
	   
    public static EventsList parse(InputStream in) throws XmlPullParserException, IOException {
    	try {
    		XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readEvents(parser);
        } finally {
            in.close();
        }
    }
    
    private static EventsList readEvents(XmlPullParser parser) throws XmlPullParserException, IOException {
    	EventsList list = null;    	
    	Boolean firstGui = true;

        parser.require(XmlPullParser.START_TAG, ns, "odcgi");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("gui") && firstGui) {
            	list = readGui(parser);
            	firstGui = false;
            } else {
                skip(parser);
            }
        }  
        return list;
    }
    
    private static EventsList readGui(XmlPullParser parser) throws XmlPullParserException, IOException {
    	EventsList list = new EventsList();

        parser.require(XmlPullParser.START_TAG, ns, "gui");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {            	
           		list.addEvent(readItem(parser));
            } else {
                skip(parser);
            }
        }  
        return list;
    }   
      
    //Procesamos las etiquetas Item
    private static Event readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Event event = null;
    	parser.require(XmlPullParser.START_TAG, ns, "item");
    	
    	//<item href="showEvents.sh?odcgioptionsel[]=08:49:28-odcgi.notice&GUI=XML" label="Login user admin"/>
        String href = parser.getAttributeValue(null, "href"); 
        String label = parser.getAttributeValue(null, "label");
        if ((!TextUtils.isEmpty(href)) && (!TextUtils.isEmpty(label))) {
        	String time = href.substring(href.indexOf('=')+1, href.indexOf('-'));        	
        	String aux = href.substring(href.indexOf('-')+1, href.indexOf('&'));
        	String transmitter = aux.substring(0, aux.indexOf('.'));
        	String type = aux.substring(aux.indexOf('.')+1, aux.length());
        	String message = label;
        	
        	event = new Event(time,transmitter,type,message);        	 
        	parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return event;
    }
    
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
}