package com.abc.sih.Reports;

public class ReportComments {
    private String user_name;
    private String comment_time;
    private String comment;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReportComments(String user_name, String comment_time, String comment) {
        this.user_name = user_name;
        this.comment_time = comment_time;
        this.comment = comment;
    }
    public ReportComments()
    {

    }

}


