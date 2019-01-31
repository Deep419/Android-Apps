package com.example.chatroomapp;

import java.util.ArrayList;
import java.util.List;

public class MessagesResponse {

    List<Messages> messages = new ArrayList<>();
    String status;

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
