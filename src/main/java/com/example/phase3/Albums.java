package com.example.phase3;

import java.util.ArrayList;

public class Albums {
    private String name;

    public ArrayList<Photos> photoList;

    public Albums(){
        name = "no album";
    }

    public Albums(String name){
        this.name = name;
    }

    public String getAlbumName(){
        return name;
    }

    public void setAlbumName(String newName){
        this.name = newName;
    }
}
