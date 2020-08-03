package com.abc.sih;

public class Notif {
    String type,date,time,desc;

    public Notif(String type, String date, String time, String desc) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.desc = desc;
    }

    public Notif() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
