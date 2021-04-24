package com.masarcardriver.model;

public class MenuModel {
    String item;
    int id ;

    public MenuModel(String item, int id) {
        this.item = item;
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
