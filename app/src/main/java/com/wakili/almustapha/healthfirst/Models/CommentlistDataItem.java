package com.wakili.almustapha.healthfirst.Models;

public class CommentlistDataItem {

    public String Comment,Username, Postid, Userid, Image;


    public CommentlistDataItem(){

    }

    public CommentlistDataItem(String comment, String username, String postid, String userid, String image){
        this.Comment = comment;
        this.Username = username;
        this.Postid = postid;
        this.Userid = userid;
        this.Image = image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getImage() {
        return Image;
    }
}
