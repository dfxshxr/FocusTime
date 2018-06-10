package com.xidian.mktime.bean;

import org.litepal.crud.DataSupport;


public class Monitor extends DataSupport{

    private long id;
    private long startMilliseconds;
    private long endMilliseconds;

    public Monitor() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartMilliseconds() {
        return startMilliseconds;
    }

    public void setStartMilliseconds(long startMilliseconds) {
        this.startMilliseconds = startMilliseconds;
    }

    public long getEndMilliseconds() {
        return endMilliseconds;
    }

    public void setEndMilliseconds(long endMilliseconds) {
        this.endMilliseconds = endMilliseconds;
    }
}