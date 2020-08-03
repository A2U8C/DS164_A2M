package com.abc.sih;

public class Beat {
String id;
Long time;

    public Beat(String id, Long time) {
        this.id = id;
        this.time = time;
    }

    public Beat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
