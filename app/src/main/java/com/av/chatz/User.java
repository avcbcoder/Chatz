package com.av.chatz;

/**
 * Created by Ankit on 06-02-2018.
 */

public class User {
    String uid;
    String username;
    String status;
    String image_link;
String mail;
    public User(){

    }

    public User(String uid, String mail,String username, String status, String image_link) {
        this.uid = uid;
        this.mail=mail;
        this.username = username;
        this.status = status;
        this.image_link = image_link;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getImage_link() {
        return image_link;
    }
}
