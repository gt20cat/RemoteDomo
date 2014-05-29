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

public class Device {

	public int id;
	public String name;
	public String url;
	public String usr;
	public String pwd;
	public int location;
	public int odType;
	
	public Device()	{
		
	}
	
	public Device(int id,String name,String url,String usr,String pwd,int location,int odType)	{
		this.id= id;
		this.name = name;
		this.url = url;
		this.usr = usr;
		this.pwd = pwd;
		this.location = location;
		this.odType = odType;
	}
	
	public Device(String name,String url,String usr,String pwd,int location,int odType)	{
		this.name = name;
		this.url = url;
		this.usr = usr;
		this.pwd = pwd;
		this.location = location;
		this.odType = odType;
	}

}

