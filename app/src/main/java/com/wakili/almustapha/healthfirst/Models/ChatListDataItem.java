package com.wakili.almustapha.healthfirst.Models;

public class ChatListDataItem {

    public String user_id,username, image, active_user_row;


    public ChatListDataItem(){

    }

    public ChatListDataItem(String user_id, String username, String image){
        this.user_id = user_id;
        this.username = username;
        this.image = image;
    }

    public void setActive_user_row(String active_user_row) {
        this.active_user_row = active_user_row;
    }

    public String getActive_user_row() {
        return active_user_row;
    }
}
