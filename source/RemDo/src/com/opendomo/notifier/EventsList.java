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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que gestiona la lista de eventos.<p>
 * 
 * Define una lista de objetos de tipo Event, sobre la cual se permite
 * añadir un nuevo evento, eliminar uno existente, buscar un elemento en particular, e
 * informar del número total de objetos incluidos.
 * 
 * @see Event
 * @version  1.0, 30/05/2013
 */

public class EventsList {	

	private List<Event> list = new ArrayList<Event>();

	public Iterator<Event> getIterator() {
		Iterator<Event> it = list.iterator();
		return (it);	
	}	

	//Añade el evento a la lista de eventos
	public void addEvent (Event newEvent) {
		list.add(newEvent);
	}
	
	//Elimina el evento de la lista de eventos
	public boolean removeEvent (String time, String transmitter, String type) {
		boolean success = false;
		for(Event event : list) {
			if (event.getTime() == time &&
				event.getTransmitter() == transmitter &&
				event.getType() == type) {
				list.remove(event);
				success = true;
				break;
			}
		}
		return success;
	}
	
	//Obtiene un evento de la lista de eventos
	public Event getEvent (String time, String transmitter, String type) {
		Event event = null;
		for(Event auxEvent : list) {
			if (auxEvent.getTime() == time &&
				auxEvent.getTransmitter() == transmitter &&
				auxEvent.getType() == type) {
				event = auxEvent;
				break;
			}
		}
		return event;
	}
	
	//Comprueba si ya existe un evento en la lista de eventos
	public Boolean existEvent (String time, String transmitter, String type) {
		boolean exist = false;
		Event event = getEvent(time, transmitter, type);
		if (event != null)
			exist = true;
		return exist;
	}
	
	public int size() {
		return list.size();
	}
}