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

/**
 * Clase que define un objeto Event OpenDomo.<p>
 * 
 * @link http://es.opendomo.org/odevents
 * @version  1.0, 30/05/2013
 */

public class Event {

	private String day;
	private String time;
	private String transmitter;
	private String type;	
	private String message;	
	private int read;	
	
	/**
	 * Constructor de la clase Event
	 * 
	 * @param time - Hora, minutos y segundos (00:00:00) en la que se produce el evento.
	 * @param transmitter - Elemento que origina el evento.
	 * @param type - Nivel de prioridad (debug, info, notice, warm, error y crit)
	 * @param message - Descripción del evento.
	 */	
	public Event(String day,String time, String transmitter, String type, String message, int read) {
		super();
		this.day = day;
		this.time = time;
		this.transmitter = transmitter;
		this.type = type;
		this.message = message;
		this.read = read;
	}
	
	public Event(String day, String time, String transmitter, String type, String message) {
		super();
		this.day = day;
		this.time = time;
		this.transmitter = transmitter;
		this.type = type;
		this.message = message;
		this.read = 0;
	}

	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTransmitter() {
		return transmitter;
	}
	
	public void setTransmitter(String transmitter) {
		this.transmitter = transmitter;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getRead() {
		return read;
	}
	
	public void setRead(int read) {
		this.read = read;
	}
}
