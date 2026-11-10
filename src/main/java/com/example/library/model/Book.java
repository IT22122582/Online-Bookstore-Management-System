package com.example.library.model;

public abstract class Book implements Borrowable {
    private String id;
    private String title;
    private String authorId;
    private String isbn;
    private String genre;
    private boolean available;

    public Book() {}

    public Book(String id, String title, String authorId, String isbn, String genre, boolean available) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.isbn = isbn;
        this.genre = genre;
        this.available = available;
    }

    @Override
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    @Override
    public boolean isAvailable() { return available; }
    @Override
    public void setAvailable(boolean available) { this.available = available; }

    public abstract String getTypeSpecificDetails();
}
