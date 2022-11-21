package com.example.androidassignment12015.Model;

public class Item {

    private  int id;
    private String name;
    private String location;
    private int isImported;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;


    }

    public Item(int id,String name,  String location, int isImported) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.isImported = isImported;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int isImported() {
        return isImported;
    }

    public void setImported(int imported) {
        isImported = imported;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", location='" + location + '\'' +
                ", isImported=" + isImported +
                '}';
    }
}
