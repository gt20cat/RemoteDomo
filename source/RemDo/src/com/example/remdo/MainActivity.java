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

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ListActivity {

	TextView selection;
	public int idToModify; 
	DataManipulator dm;
	
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dm = new DataManipulator(this);
	      names2 = dm.selectAll();

		stg1=new String[names2.size()]; 

		int x=0;
		String stg;

		for (String[] name : names2) {
			stg = name[1]+" - "+name[2]+ " - "+name[3]+" - "+name[4];

			stg1[x]=stg;
			x++;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
				this,android.R.layout.simple_list_item_1,   
				stg1);
        this.setListAdapter(adapter);
		selection=(TextView)findViewById(R.id.selection);

		
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		selection.setText(stg1[position]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
