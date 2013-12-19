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
package com.example.remdo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class DataManipulator {
		private static final  String DATABASE_NAME = "RemoteDomo.db";
		private static final int DATABASE_VERSION = 1;
		static final String T_DEVICES = "devices";
		private static Context context;
		static SQLiteDatabase db;

		private SQLiteStatement insertStmt;
		
	    private static final String INSERT = "insert into "
			+ T_DEVICES + " (name,url,usr,pwd) values (?,?,?,?)";
	    
	    
		public DataManipulator(Context context) {
			DataManipulator.context = context;
			OpenHelper openHelper = new OpenHelper(DataManipulator.context);
			DataManipulator.db = openHelper.getWritableDatabase();
			this.insertStmt = DataManipulator.db.compileStatement(INSERT);

		}
		public long insert(String name,String url,String usr,String pwd) {
			this.insertStmt.bindString(1, name);
			this.insertStmt.bindString(2, url);
			this.insertStmt.bindString(3, usr);
			this.insertStmt.bindString(4, pwd);
			return this.insertStmt.executeInsert();
		}
	    
		public void deleteAll() {
			db.delete(T_DEVICES, null, null);
		}
	    
		public List<String[]> selectAll()
		{

			List<String[]> list = new ArrayList<String[]>();
			Cursor cursor = db.query(T_DEVICES, new String[] { "id","name","url","usr","pwd","location" },
					null, null, null, null, "name asc"); 

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
		 * Look into database for existing devices
		 * @param mHost contains the key to be searched in the device database
		 * @param string contain the field to look for could be "host" or name
		 * @return true if device exists, false if device does not exists
		 */
		public boolean exists(String mHost, String string) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public String getParamByName(String pDeviceName,String param)
		{
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
		
		public String getURIbyName(String pDeviceName)
		{
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
		
		public String getpwdbyName(String pDeviceName)
		{
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
		
		public String getusrbyName(String pDeviceName)
		{
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

		public void delete(int rowId) {
			db.delete(T_DEVICES, null, null); 
		}
	    
		private static class OpenHelper extends SQLiteOpenHelper {

			OpenHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL("CREATE TABLE " + T_DEVICES + " (id INTEGER PRIMARY KEY, name TEXT not null unique, url TEXT not null unique, usr TEXT, pwd TEXT,location TEXT)");
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + T_DEVICES);
				onCreate(db);
			}
		}
		
		/**
		 * To query table devices in database to know if it is empty or not
		 * @return false if empty, true if at least there is one device in devices table
		 */
		public boolean hasDevices() {

			Cursor c = db.query(T_DEVICES, null, null, null, null, null, null);
			int result = c.getCount();
			c.close();
			
			if (result > 0) return true;
			else			return false;
		}
		
	}