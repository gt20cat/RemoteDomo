/* ************************************************************************  
 * RemoteDomo is an Android native application to remotely control domotics systems
 * Copyright © 2013 Gerard Torrents Vinaixa
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ************************************************************************/
package remdo.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.opendomo.notifier.Event;

import remdo.sqlite.model.Device;
import remdo.sqlite.model.EventCategory;
import remdo.sqlite.model.OdDeviceTypes;
import remdo.sqlite.model.Service;

public class DatabaseHelper extends SQLiteOpenHelper {

		private static final  String DATABASE_NAME = "RemoteDomo.db";
		private static final int DATABASE_VERSION = 1;
		static final String T_DEVICES = "devices";
		static final String T_LOCATIONS = "locations";
		static final String T_ODDEVICETYPES = "odDeviceTypes";
		static final String T_SERVICES = "services";
		static final String T_EVENTS = "events";
		private static Context context;
		//static SQLiteDatabase db;

		private SQLiteStatement insertStmt;
		
	    private static final String INSERT = "insert into "
			+ T_DEVICES + " (name,url,usr,pwd,location,odType) values (?,?,?,?)";
	    
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	    
		/*public DatabaseHelper (Context context) {
			DatabaseHelper.context = context;
			OpenHelper openHelper = new OpenHelper(DatabaseHelper.context);
			DatabaseHelper.db = openHelper.getWritableDatabase();
			this.insertStmt = DatabaseHelper.db.compileStatement(INSERT);

		}*/
	    
		public DatabaseHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
	    
		public long createDevice(Device device) {
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("name", device.name);
			values.put("url", device.url);
			values.put("usr", device.usr);
			values.put("pwd", device.pwd);
			values.put("locationId", device.location);
			values.put("odTypeId", device.odType);

			
			long device_id = db.insert(T_DEVICES, null, values);
			
			return device_id;
		}
	    
		public void deleteDevicesAll() {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(T_DEVICES, null, null);
		}
	    
		public List<String[]> selectDevicesAll()
		{
			SQLiteDatabase db = this.getReadableDatabase();
			List<String[]> list = new ArrayList<String[]>();
			Cursor cursor = db.query(T_DEVICES, new String[] { "id","name","url","usr","pwd","locationId","odTypeId" },
					null, null, null, null, "odTypeId ASC, name ASC"); 

			int x=0;
			if (cursor.moveToFirst()) {
				do {
					String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)};

					list.add(b1);

					x=x+1;
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			} 
			cursor.close();
			
			return list;
		}
		
		
		/**
		 * Look into database for existing devices, this function is not key sensitive.
		 * @param mHost, contains the key to be searched in the device database
		 * @param field, contain the field name to look for could be "host" or "name"
		 * @return true if device exists, false if device does not exists
		 */
		public boolean DeviceExists(String mHost, String field) {
			SQLiteDatabase db = this.getReadableDatabase();
			
			String[] tableColumns = new String[] { field };
			String whereClause = field +" = ?";
			String[] whereArgs = new String[] {mHost};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			
			if (count>0){return true;}
			else{return false;}
			
		}
		
		public String getDeviceParamByName(String pDeviceName,String param)
		{
			SQLiteDatabase db = this.getReadableDatabase();
			
			String uri = "Error";
			String[] tableColumns = new String[] { param};
			String whereClause = "name = ?";
			String[] whereArgs = new String[] {pDeviceName};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			if (count>0){
		        cursor.moveToFirst();
		        uri = cursor.getString(cursor.getColumnIndex(param));
		    }
			
			return uri;
			
		}
		
		public String getDeviceURIbyName(String pDeviceName)
		{
			SQLiteDatabase db = this.getReadableDatabase();
			
			String uri = "Error";
			String[] tableColumns = new String[] { "url"};
			String whereClause = "name = ?";
			String[] whereArgs = new String[] {pDeviceName};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			if (count>0){
		        cursor.moveToFirst();
		        uri = cursor.getString(cursor.getColumnIndex("url"));
		    }
			
			return uri;
			
		}
		
		public String getDevicePwdbyName(String pDeviceName)
		{
			SQLiteDatabase db = this.getReadableDatabase();
			
			String uri = "Error";
			String[] tableColumns = new String[] { "pwd"};
			String whereClause = "name = ?";
			String[] whereArgs = new String[] {pDeviceName};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			if (count>0){
		        cursor.moveToFirst();
		        uri = cursor.getString(cursor.getColumnIndex("pwd"));
		    }
			
			return uri;
			
		}
		
		public String getDeviceUsrbyName(String pDeviceName)
		{
			SQLiteDatabase db = this.getReadableDatabase();
			
			String usr = "Error";
			String[] tableColumns = new String[] { "usr"};
			String whereClause = "name = ?";
			String[] whereArgs = new String[] {pDeviceName};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			if (count>0){
		        cursor.moveToFirst();
		        usr = cursor.getString(cursor.getColumnIndex("usr"));
		    }
			
			return usr;
			
		}
		

		public void deleteDevice(int deviceId) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(T_DEVICES, "id = ?",
					new String[] { String.valueOf(deviceId) });
			
		}
	    
	
		/**
		 * To query table devices in database to know if it is empty or not
		 * @return false if empty, true if at least there is one device in devices table
		 */
		public boolean hasDevices() {

			SQLiteDatabase db = this.getReadableDatabase();

			if(tableExists(T_DEVICES))
			{
				Cursor c = db.query(T_DEVICES, null, null, null, null, null, null);
				int result = c.getCount();

				if (result > 0) return true;
				else			return false;
			}
			else
			{
				return false;
			}
		}
		
		public boolean ODNetworkExists() {
			
			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + T_DEVICES + " WHERE "
					+ "odTypeId = 1";

			Cursor c = db.rawQuery(selectQuery, null);
			if (c != null)
			{
				if (c.moveToFirst())
				{
					c.close();
					
					return true;					
				}
				else
				{
					c.close();
					
					return false;
				}
			}
			else
			{
				c.close();
				
				return false;
			}
		}
		
		boolean tableExists(String tableName)
		{
			SQLiteDatabase db = this.getReadableDatabase();

		    if (tableName == null | db == null || !db.isOpen())
		    {
		        return false;
		    }
		    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
		    if (!cursor.moveToFirst())
		    {
		        return false;
		    }
		    int count = cursor.getInt(0);
		    cursor.close();
			
		    return count > 0;

		}
		

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + T_DEVICES + " (id INTEGER PRIMARY KEY, name TEXT not null unique, url TEXT not null unique, usr TEXT, pwd TEXT,locationId int, odTypeId int)");
			db.execSQL("CREATE TABLE " + T_ODDEVICETYPES + " (id INTEGER PRIMARY KEY, name TEXT not null unique, description TEXT)");	
			db.execSQL("CREATE TABLE " + T_LOCATIONS + " (id INTEGER PRIMARY KEY, name TEXT not null unique, description TEXT)");
			db.execSQL("CREATE TABLE " + T_EVENTS + " (id INTEGER PRIMARY KEY,day not null, time TEXT not null, transmitter TEXT not null, type TEXT not null, message TEXT not null,read int)");
			db.execSQL("CREATE TABLE " + T_SERVICES + " (id INTEGER PRIMARY KEY, description TEXT not null, minutes int not null, locationId int not null)");

			db.execSQL("INSERT INTO " + T_ODDEVICETYPES + " (id, name, description) VALUES(1,'ODNetwork','')");
			db.execSQL("INSERT INTO " + T_ODDEVICETYPES + " (id, name, description) VALUES(2,'ODControl','')");
			
			db.execSQL("INSERT INTO " + T_SERVICES + " (id, description,minutes,locationId) VALUES(1,'Geo',15,0)");
			db.execSQL("INSERT INTO " + T_SERVICES + " (id, description,minutes,locationId) VALUES(2,'Alerts',15,0)");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + T_DEVICES);
			db.execSQL("DROP TABLE IF EXISTS " + T_LOCATIONS);
			db.execSQL("DROP TABLE IF EXISTS " + T_ODDEVICETYPES);
			db.execSQL("DROP TABLE IF EXISTS " + T_EVENTS);
			db.execSQL("DROP TABLE IF EXISTS " + T_SERVICES);
			onCreate(db);
			
		}

		public Device getDevicebyID(Long deviceId) {

			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + T_DEVICES + " WHERE "
					+ "id" + " = " + deviceId;

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();

			Device dev = new Device();
			dev.id = c.getInt(c.getColumnIndex("id"));
			dev.name = c.getString(c.getColumnIndex("name"));
			dev.url = c.getString(c.getColumnIndex("url"));
			dev.usr = c.getString(c.getColumnIndex("usr"));
			dev.pwd = c.getString(c.getColumnIndex("pwd"));
			dev.location = c.getInt(c.getColumnIndex("locationId"));
			dev.odType = c.getInt(c.getColumnIndex("odTypeId"));
			c.close();
			
			return dev;	

		}
		
		public Device getDevicebyDevType(int odDevType, int locationId) {
			
			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + T_DEVICES + " WHERE "
					+ "odTypeId" + " = " + odDevType;

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();
			
			Device dev = new Device();
			dev.id = c.getInt(c.getColumnIndex("id"));
			dev.name = c.getString(c.getColumnIndex("name"));
			dev.url = c.getString(c.getColumnIndex("url"));
			dev.usr = c.getString(c.getColumnIndex("usr"));
			dev.pwd = c.getString(c.getColumnIndex("pwd"));
			dev.location = c.getInt(c.getColumnIndex("locationId"));
			dev.odType = c.getInt(c.getColumnIndex("odTypeId"));
			c.close();
			
			return dev;	

			
		}

		public int updateDevice(Device device) {

			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put("name", device.name);
			values.put("url", device.url);
			values.put("usr", device.usr);
			values.put("pwd", device.pwd);
			values.put("locationId", device.location);
			values.put("odTypeId", device.odType);

			
			int result = db.update(T_DEVICES, values, "id = ?",	
					new String[] { String.valueOf(device.id) });

			
			return result;
		}

		public OdDeviceTypes getODDeviceType(long id ) {

			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + T_ODDEVICETYPES + " WHERE "
					+ "id" + " = " + id;

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();

			OdDeviceTypes devType = new OdDeviceTypes();
			devType.id = c.getInt(c.getColumnIndex("id"));
			devType.name = c.getString(c.getColumnIndex("name"));
			devType.description = c.getString(c.getColumnIndex("description"));
			c.close();
			
			return devType;
		}
		
		public OdDeviceTypes getODDeviceType(String pName ) {

			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + T_ODDEVICETYPES + " WHERE "
					+ "name" + " = '" + pName +"'";

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();

			OdDeviceTypes devType = new OdDeviceTypes();
			devType.id = c.getInt(c.getColumnIndex("id"));
			devType.name = c.getString(c.getColumnIndex("name"));
			devType.description = c.getString(c.getColumnIndex("description"));
			c.close();
			
			return devType;
		}

		public int updateServiceConfig(String servcieDesc, int minutes) {
			
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put("minutes", minutes);

			int result =  db.update(T_SERVICES, values, "description = ?",	
					new String[] { String.valueOf(servcieDesc) });
			
			return result;
			
		}
		
		public int getServcieMinutes(String serviceDesc) {

			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  minutes FROM " + T_SERVICES + " WHERE "
					+ "description" + " = '" + serviceDesc + "'";

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();

			int result = c.getInt(c.getColumnIndex("minutes"));
			c.close();
			
			return result;

		}
		
		public long insertEvent(Event event) {
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("day", event.getDay());
			values.put("time", event.getTime());
			values.put("transmitter", event.getTransmitter());
			values.put("type", event.getType());
			values.put("message", event.getMessage());
			values.put("read", 0);

			
			long event_id = db.insert(T_EVENTS, null, values);
			
			return event_id;
		}

		public boolean existsEvent(Event event) {

			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT id  FROM " + T_EVENTS + " WHERE " + "day = '" + event.getDay()  +
					"' and time = '" + event.getTime() + "' and transmitter = '" + event.getTransmitter() +
					"' and type = '" + event.getType() + "' and message = '" + event.getMessage() + "'";
			
		    Cursor cursor = db.rawQuery(selectQuery,null);
		    if (!cursor.moveToFirst())
		    {
		    	cursor.close();
		        return false;
		    }
		    int count = cursor.getInt(0);
		    cursor.close();
			
		    return count > 0;
		}

		public List<EventCategory> selectCategoriesAll() {

			SQLiteDatabase db = this.getReadableDatabase();
			List<EventCategory> list = new ArrayList<EventCategory>();

			/*Cursor cursor = db.query(T_EVENTS, new String[] { "count(id)","transmitter" },
					"read = 0", null, "transmitter", null, "transmitter ASC"); */
			
			Cursor cursor = db.query(T_EVENTS, new String[] {"transmitter,sum(CASE WHEN read = 1 THEN 1 END) as read , sum(CASE WHEN read = 0 THEN 1 END) as unread" },
			null, null, "transmitter", null, "transmitter ASC"); 
		
			int x=0;
			if (cursor.moveToFirst()) {
				do {
					EventCategory b1 = new EventCategory(cursor.getString(0),cursor.getInt(1),cursor.getInt(2));

					list.add(b1);

					x=x+1;
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			} 
			cursor.close();
			
			return list;
			
		}
		
		public int getUnreadAlerts() {

			SQLiteDatabase db = this.getReadableDatabase();
			
			Cursor cursor = db.query(T_EVENTS, new String[] {"count(*)" },
					"read = 0", null, null, null, null); 
		
		    if (!cursor.moveToFirst())
		    {
		    	cursor.close();
		        return 0;
		    }
		    int alertsUnread = cursor.getInt(0);
		    cursor.close();
			
		    return alertsUnread;
			
		}
		

		public List<Event> selectActivityByCategory(String category) {

			SQLiteDatabase db = this.getReadableDatabase();
			List<Event> list = new ArrayList<Event>();

			Cursor cursor = db.query(T_EVENTS, new String[] { "id","day","time","transmitter","type","message","read" },
					"transmitter like '" + category+"'", null, null, null, "day,time"); 
			
			int x=0;
			if (cursor.moveToFirst()) {
				//cursor2.moveToFirst();
				do {
					Event b1 = new Event(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6));

					list.add(b1);

					x=x+1;
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				//cursor2.close();
			} 
			cursor.close();
			
			return list;
			
		}

		public void deleteAlertsAll() {
			
			SQLiteDatabase db = this.getReadableDatabase();
			db.execSQL("delete from "+ T_EVENTS);
			
		}

		public void markAsReadAlertsAll() {

			SQLiteDatabase db = this.getReadableDatabase();
			
			ContentValues args = new ContentValues();
			args.put("read", 1);
			int c =  db.update(T_EVENTS, args, " read = 0 ", null);

		}

		public void markActivityReadByCategory(String category) {

			SQLiteDatabase db = this.getReadableDatabase();
			
			ContentValues args = new ContentValues();
			args.put("read", 1);
			int c =  db.update(T_EVENTS, args, " transmitter = '"+category+"' ", null);
			
		}

		public void deleteAlertsByCategory(String category) {

			SQLiteDatabase db = this.getReadableDatabase();
			db.execSQL("delete from "+ T_EVENTS + " where transmitter like '"+category+"'");
			
		}
		
		


		
	}