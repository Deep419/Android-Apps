package com.example.chatroomapp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ThreadResponse {
    List<Threads> threads = new ArrayList<>();
    String status;

    public List<Threads> getThreads() {
        return threads;
    }

    public void setThreads(List<Threads> threads) {
        this.threads = threads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ThreadResponse{" +
                "threads=" + threads +
                ", status='" + status + '\'' +
                '}';
    }
}
