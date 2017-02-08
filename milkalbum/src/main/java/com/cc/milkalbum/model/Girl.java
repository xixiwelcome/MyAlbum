package com.cc.milkalbum.model;

/**
 * Created by Administrator on 2017/2/7.
 */
public class Girl {
    private int id;
    private String name;
    private String catalog;
    private int issue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Girl(int resImg, String name){
        id = resImg;
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
