package com.abc.sih;

public class Logs {
    public String assgnBy,assgnto;
    public long assgnTime,returned;



    public Logs() {
    }

    public Logs(String assgnBy, String assgnto, long assgnTime, long returned) {
        this.assgnBy = assgnBy;
        this.assgnto = assgnto;
        this.assgnTime = assgnTime;
        this.returned = returned;
    }

    public String getAssgnBy() {
        return assgnBy;
    }

    public void setAssgnBy(String assgnBy) {
        this.assgnBy = assgnBy;
    }

    public String getAssgnto() {
        return assgnto;
    }

    public void setAssgnto(String assgnto) {
        this.assgnto = assgnto;
    }

    public long getAssgnTime() {
        return assgnTime;
    }

    public void setAssgnTime(long assgnTime) {
        this.assgnTime = assgnTime;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }
}
