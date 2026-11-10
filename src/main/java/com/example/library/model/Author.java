package com.example.library.model;

public abstract class Author {
    private String id;
    private String name;
    private String bio;

    public Author() {}

    public Author(String id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public abstract String getAuthorTypeSummary();
}
