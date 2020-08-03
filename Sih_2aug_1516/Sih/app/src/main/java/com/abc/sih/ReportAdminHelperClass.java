package com.abc.sih;

public class ReportAdminHelperClass {
    String type,geo_lat,geo_long,uploader_name,time_date_case;

    public ReportAdminHelperClass(String type, String geo_lat, String geo_long, String uploader_name, String time_date_case) {
        this.type = type;
        this.geo_lat = geo_lat;
        this.geo_long = geo_long;
        this.uploader_name = uploader_name;
        this.time_date_case = time_date_case;
    }

    public ReportAdminHelperClass() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGeo_lat() {
        return geo_lat;
    }

    public void setGeo_lat(String geo_lat) {
        this.geo_lat = geo_lat;
    }

    public String getGeo_long() {
        return geo_long;
    }

    public void setGeo_long(String geo_long) {
        this.geo_long = geo_long;
    }

    public String getUploader_name() {
        return uploader_name;
    }

    public void setUploader_name(String uploader_name) {
        this.uploader_name = uploader_name;
    }

    public String getTime_date_case() {
        return time_date_case;
    }

    public void setTime_date_case(String time_date_case) {
        this.time_date_case = time_date_case;
    }
}
