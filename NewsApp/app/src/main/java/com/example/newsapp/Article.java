/*
Article.java
InClass 06

Deep Ghaghara, Pranathi Kallem
Group 23

 */

package com.example.newsapp;

/**
 * Created by dghaghar on 2/19/2018.
 */

public class Article {
    String title,publishedAt,urlToImage,description;

    public Article(String title, String publishedAt, String urlToImage, String description) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
