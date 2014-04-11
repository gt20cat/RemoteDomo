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
