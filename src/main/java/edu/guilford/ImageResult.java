package edu.guilford;

import java.util.HashMap;

import javafx.scene.image.ImageView;

public class ImageResult {
    private String id;
    private String created_at;
    private int width;
    private int height;
    private int likes;
    private String description;
    private HashMap<String, String> urls;
    private HashMap<String, String> links;

    private transient ImageView smallImage;
    private transient ImageView regularImage;

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

    public ImageView getImage() {
        if (regularImage == null) {
            regularImage = new ImageView(urls.get("regular"));
        }
        return regularImage;
    }

    public ImageView getSmallImage() {
        if (smallImage == null) {
            smallImage = new ImageView(urls.get("regular"));
        }
        return smallImage;
    }
}