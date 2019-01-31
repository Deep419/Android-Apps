package com.example.dghaghar.mailboxapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Message implements Serializable {
    String data, from_id, from_name, id, time, to_id, to_name;
    boolean isRead;

    public Message() {
    }

    public Message(String data, String from_id, String from_name, String id, String time, String to_id, String to_name, boolean isRead) {
        this.data = data;
        this.from_id = from_id;
        this.from_name = from_name;
        this.id = id;
        this.time = time;
        this.to_id = to_id;
        this.to_name = to_name;
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data='" + data + '\'' +
                ", from_id='" + from_id + '\'' +
                ", from_name='" + from_name + '\'' +
                ", id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", to_id='" + to_id + '\'' +
                ", to_name='" + to_name + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}