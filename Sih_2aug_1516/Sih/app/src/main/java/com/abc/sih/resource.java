package com.abc.sih;

public class resource {
    public String model,resId,lastallocated,alloted;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }



    public String getLastallocated() {
        return lastallocated;
    }

    public void setLastallocated(String lastallocated) {
        this.lastallocated = lastallocated;
    }

    public String getAlloted() {
        return alloted;
    }

    public void setAlloted(String alloted) {
        this.alloted = alloted;
    }

    public resource(String model, String resId, String lastallocated, String alloted) {
        this.model = model;
        this.resId = resId;
        this.lastallocated = lastallocated;
        this.alloted = alloted;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public resource() {
    }

}
