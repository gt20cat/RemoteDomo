package remdo.sqlite.model;

public class EventCategory {

	public EventCategory(String pname, int read, int unread) {
		this.name = pname;
		this.readAlerts = read;
		this.unreadAlerts = unread;
	}
	public String name;
	public int readAlerts;
	public int unreadAlerts;

}
