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
		static final String TABLE_NAME = "devices";
		private static Context context;
		static SQLiteDatabase db;

		private SQLiteStatement insertStmt;
		
	    private static final String INSERT = "insert into "
			+ TABLE_NAME + " (name,url,usr,pwd) values (?,?,?,?)";
	    
	    
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
			db.delete(TABLE_NAME, null, null);
		}
	    
		public List<String[]> selectAll()
		{

			List<String[]> list = new ArrayList<String[]>();
			Cursor cursor = db.query(TABLE_NAME, new String[] { "id","name","url","usr","pwd" },
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
	    
		public void delete(int rowId) {
			db.delete(TABLE_NAME, null, null); 
		}
	    
		private static class OpenHelper extends SQLiteOpenHelper {

			OpenHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, name TEXT, url TEXT, usr TEXT, pwd TEXT)");
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
				onCreate(db);
			}
		}
	}