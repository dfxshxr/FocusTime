package com.xidian.focustime.bean;

import org.litepal.crud.DataSupport;


public class StudyStatistics extends DataSupport{

    private long id;
    private long startMilliseconds;
    private long endMilliseconds;
    private long thisMilliseconds;
    private long settingMilliseconds;
    private long playMilliseconds;
    private long errorMilliseconds;

    public StudyStatistics() {
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

    public long getThisMilliseconds() {
        return thisMilliseconds;
    }

    public void setThisMilliseconds(long thisMilliseconds) {
        this.thisMilliseconds = thisMilliseconds;
    }

    public long getSettingMilliseconds() {
        return settingMilliseconds;
    }

    public void setSettingMilliseconds(long settingMilliseconds) {
        this.settingMilliseconds = settingMilliseconds;
    }

    public long getPlayMilliseconds() {
        return playMilliseconds;
    }

    public void setPlayMilliseconds(long playMilliseconds) {
        this.playMilliseconds = playMilliseconds;
    }

    public long getErrorMilliseconds() {
        return errorMilliseconds;
    }

    public void setErrorMilliseconds(long errorMilliseconds) {
        this.errorMilliseconds = errorMilliseconds;
    }
}