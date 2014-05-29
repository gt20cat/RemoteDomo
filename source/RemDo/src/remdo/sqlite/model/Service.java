/* ************************************************************************  
 * RemoteDomo is an Android native application to remotely control domotics systems
 * Copyright © 2014 Gerard Torrents Vinaixa
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <www.gnu.org/licenses/>.
 * ************************************************************************/
package remdo.sqlite.model;

public class Service {

	public  int id;
	public  String description;
	int minutes;
	public 	int locationId; 
	
	public Service()
	{
		
	}
	
	public Service(int id,String description,int minutes, int locationId){
		this.id = id;
		this.description = description;		
		this.minutes = minutes;
		this.locationId = locationId;
	}
}




