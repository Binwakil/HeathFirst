package com.wakili.almustapha.healthfirst.Models;

public class TipsListDataItem {

    public String tipid, username, image, tiptitle, tipdetail, active_user_row;


    public TipsListDataItem(){

    }

    public TipsListDataItem(String tipid, String username, String image, String tiptitle, String tipdetail){
        this.tipid = tipid;
        this.username = username;
        this.image = image;
        this.tiptitle = tiptitle;
        this.tipdetail = tipdetail;
    }

    public void setActive_user_row(String active_user_row) {
        this.active_user_row = active_user_row;
    }

    public String getActive_user_row() {
        return active_user_row;
    }
}
