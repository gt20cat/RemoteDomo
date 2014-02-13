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

import remdo.sqlite.model.Device;

public class DatabaseHelper extends SQLiteOpenHelper {

		private static final  String DATABASE_NAME = "RemoteDomo.db";
		private static final int DATABASE_VERSION = 1;
		static final String T_DEVICES = "devices";
		static final String T_LOCATIONS = "locations";
		static final String T_ODDEVICETYPES = "odDeviceTypes";
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
					null, null, null, null, "name ASC"); 

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
			
			String uri = "Error";
			String[] tableColumns = new String[] { "usr"};
			String whereClause = "name = ?";
			String[] whereArgs = new String[] {pDeviceName};
					
			Cursor cursor = db.query(T_DEVICES, tableColumns, whereClause, whereArgs,
			        null, null,null);
			
			int count = cursor.getCount();
			if (count>0){
		        cursor.moveToFirst();
		        uri = cursor.getString(cursor.getColumnIndex("usr"));
		    }
			return uri;
			
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
				c.close();
				
				if (result > 0) return true;
				else			return false;
			}
			else
			{
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
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + T_DEVICES);
			db.execSQL("DROP TABLE IF EXISTS " + T_LOCATIONS);
			db.execSQL("DROP TABLE IF EXISTS " + T_ODDEVICETYPES);
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

			return dev;
			
		}

		public int updateDevice(Device device) {

			SQLiteDatabase db = this.getWritableDatabase();
			
			/*String selectQuery = "UPDATE " + T_DEVICES + 
								" SET name = " + currentDevice.name +
								", url = " + currentDevice.url +
								", usr = " + currentDevice.usr +
								", pwd = " + currentDevice.pwd +" WHERE " +
								" id" + " = " + currentDevice.id;
			
			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();*/
					
			ContentValues values = new ContentValues();
			values.put("name", device.name);
			values.put("url", device.url);
			values.put("usr", device.usr);
			values.put("pwd", device.pwd);
			values.put("locationId", device.location);
			values.put("odTypeId", device.odType);

			
			return db.update(T_DEVICES, values, "id = ?",	
					new String[] { String.valueOf(device.id) });
			
		}
		
	}