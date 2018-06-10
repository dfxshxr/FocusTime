package com.xidian.mktime.bean;




public class Wallpaper{

    private long id;
    private String name;
    private int resId;

    public Wallpaper (String name,int resId){
        this.name=name;
        this.resId=resId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}