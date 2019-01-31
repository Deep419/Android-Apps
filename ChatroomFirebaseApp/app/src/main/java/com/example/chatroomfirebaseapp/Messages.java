package com.example.chatroomfirebaseapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Messages {

    public String created_at, id, message,thread_id, user_fname, user_id, user_lname;

    public Messages() {
    }

    public Messages(String created_at, String id, String message, String thread_id, String user_fname, String user_id, String user_lname) {
        this.created_at = created_at;
        this.id = id;
        this.message = message;
        this.thread_id = thread_id;
        this.user_fname = user_fname;
        this.user_id = user_id;
        this.user_lname = user_lname;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "created_at='" + created_at + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", thread_id='" + thread_id + '\'' +
                ", user_fname='" + user_fname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_lname='" + user_lname + '\'' +
                '}';
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
