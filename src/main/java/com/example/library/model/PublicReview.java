package com.example.library.model;

public class PublicReview extends Review {
    public PublicReview() {
        super();
    }

    public PublicReview(String id, String userId, String bookId, int rating, String comment) {
        super(id, userId, bookId, rating, comment);
    }

    @Override
    public String getReviewBadge() {
        return "Public Reader Feedback";
    }
}
