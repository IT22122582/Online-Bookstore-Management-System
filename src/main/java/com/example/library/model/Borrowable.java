package com.example.library.model;

public interface Borrowable {
    String getId();
    String getTitle();
    boolean isAvailable();
    void setAvailable(boolean available);
}
