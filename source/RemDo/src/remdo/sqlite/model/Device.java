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

