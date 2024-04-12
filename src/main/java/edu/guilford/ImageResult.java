package edu.guilford;

import java.util.HashMap;

import javafx.scene.image.Image;

public class ImageResult {
    private String id;
    private String created_at;
    private int width;
    private int height;
    private int likes;
    private String description;
    private HashMap<String, String> urls;
    private HashMap<String, String> links;

    public ImageResult() {}

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLikes() {
        return likes;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public HashMap<String, String> getLinks() {
        return links;
    }

    public Image getImage() {
        return new Image(urls.get("regular"));
    }

    public Image getSmallImage() {
        return new Image(urls.get("small"));
    }
}