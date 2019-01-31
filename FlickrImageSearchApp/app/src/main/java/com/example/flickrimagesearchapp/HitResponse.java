package com.example.flickrimagesearchapp;

import java.util.ArrayList;
import java.util.List;

public class HitResponse {
    String totalHits, total;
    List<Hits> hits = new ArrayList<>();

    public String getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(String totalHits) {
        this.totalHits = totalHits;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Hits> getHits() {
        return hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }
}
