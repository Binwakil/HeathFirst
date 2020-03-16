package com.wakili.almustapha.healthfirst.Models;

/**
 * Created by almustapha on 10/14/18.
 */

public class QuestionsDataitem {


    public String Question, Username, Qid, Userid, Image;


    public QuestionsDataitem(){

    }

    public QuestionsDataitem(String question, String username, String qid, String userid, String image){
        this.Question = question;
        this.Username = username;
        this.Qid = qid;
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
