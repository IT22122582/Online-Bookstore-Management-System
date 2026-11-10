package com.example.library.model;

public class EBook extends Book {
    private double fileSizeMB;
    private String downloadUrl;

    public EBook() {
        super();
    }

    public EBook(String id, String title, String authorId, String isbn, String genre, boolean available, double fileSizeMB, String downloadUrl) {
        super(id, title, authorId, isbn, genre, available);
        this.fileSizeMB = fileSizeMB;
        this.downloadUrl = downloadUrl;
    }

    public double getFileSizeMB() { return fileSizeMB; }
    public void setFileSizeMB(double fileSizeMB) { this.fileSizeMB = fileSizeMB; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    @Override
    public String getTypeSpecificDetails() {
        return "Digital E-Book (" + fileSizeMB + " MB) - Download URL: " + downloadUrl;
    }
}
