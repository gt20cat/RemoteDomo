/* ************************************************************************  
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
package com.remdo.adapters;

import remdo.sqlite.model.EventCategory;

import com.remdo.app.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<EventCategory>{

	Context context;
    int layoutResourceId;   
    EventCategory data[] = null;
	
    public CategoryAdapter(Context context, int resource, EventCategory[] data) {
		super(context, resource,data);

		this.layoutResourceId = resource;
		this.context = context;
		this.data = data;
		
	}
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CategoryHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new CategoryHolder();
            holder.tvName = (TextView)row.findViewById(R.id.tvcategoryname);
            holder.tvRead = (TextView)row.findViewById(R.id.tvread);
            holder.tvUnread = (TextView)row.findViewById(R.id.tvunread);
           
            row.setTag(holder);
        }
        else
        {
            holder = (CategoryHolder)row.getTag();
        }
       
        EventCategory category = data[position];
        holder.tvName.setText(category.name);
        holder.tvRead.setText(  "Read:   " +  String.valueOf(category.readAlerts));
        holder.tvUnread.setText("Unread: " + String.valueOf(category.unreadAlerts));
       
        return row;
    }
    
    static class CategoryHolder
    {
    	TextView tvName;
        TextView tvRead;
        TextView tvUnread;
    }
    
    
}

