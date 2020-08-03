package com.abc.sih;

public class news {
    public String post,location,content,title;

    public news(String post, String location, String content, String title) {
        this.post = post;
        this.location = location;
        this.content = content;
        this.title = title;
    }

    public news() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
