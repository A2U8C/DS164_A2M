package com.abc.sih.Reports;

public class Reports {

    public String name;
    public String image;
    //public String status;
    public String comp_image;
    //public String  likes;

    public Reports(){

    }

    public Reports(String name, String image, String comp_image) {
        this.name = name;
        this.image = image;
        this.comp_image = comp_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComp_image() {
        return comp_image;
    }

    public void setComp_image(String comp_image) {
        this.comp_image = comp_image;
    }
}


