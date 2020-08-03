package com.abc.sih;

public class Doctor {

    private String id,name,image,descp,rating,number_of_rating,type;

    public Doctor() {
    }

    public Doctor(String id, String name, String image, String descp,String rating,String number_of_rating,String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.descp = descp;
        this.rating = rating;
        this.number_of_rating = number_of_rating;
        this.type = type;
    }

    public String getNumber_of_rating() {
        return number_of_rating;
    }

    public void setNumber_of_rating(String number_of_rating) {
        this.number_of_rating = number_of_rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
