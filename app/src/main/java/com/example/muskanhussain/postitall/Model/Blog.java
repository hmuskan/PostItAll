package com.example.muskanhussain.postitall.Model;

public class Blog {
    public String postTitle, postDesc, postImage, userId, timeStamp;

    public Blog()
    {

    }

    public Blog(String postTitle, String postDesc, String postImage, String userId, String timeStamp) {
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postImage = postImage;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
