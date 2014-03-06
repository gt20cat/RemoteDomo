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




