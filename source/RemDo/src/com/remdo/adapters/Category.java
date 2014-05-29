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

public class Category {

    public String name;
    public int read;
    public int unread;
    public Category(){
        super();
    }
   
    public Category(String name, int read, int unread) {
        super();
        this.name = name;
        this.read = read;
        this.unread = unread;
    }
}
