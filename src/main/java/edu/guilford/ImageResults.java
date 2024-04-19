/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.guilford;

/**
 * Represents the results of an Unsplash image search
 * This type is deserialized (by GSON) from each response from the Unsplash API
 * 
 * @author Ari Singh
 */
public class ImageResults {
    private int total;
    private int total_pages;
    private ImageResult[] results;

    public ImageResults() {}

    public int getTotal() {
        return total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ImageResult[] getResults() {
        return results;
    }
}
