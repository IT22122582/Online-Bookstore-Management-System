package com.example.library.model;

public abstract class Review {
    private String id;
    private String userId;
    private String bookId;
    private int rating; // 1 to 5
    private String comment;

    public Review() {}

    public Review(String id, String userId, String bookId, int rating, String comment) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public abstract String getReviewBadge();
}
