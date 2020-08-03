package com.abc.sih;

public class Divisions {

private String descp,name,num_of_range,num;

    public Divisions() {
    }

    public Divisions(String descp, String name, String num_of_range, String num) {
        this.descp = descp;
        this.name = name;
        this.num_of_range = num_of_range;
        this.num = num;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum_of_range() {
        return num_of_range;
    }

    public void setNum_of_range(String num_of_range) {
        this.num_of_range = num_of_range;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
