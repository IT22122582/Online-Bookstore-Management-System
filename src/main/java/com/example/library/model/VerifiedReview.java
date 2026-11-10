package com.example.library.model;

public class VerifiedReview extends Review {
    private String borrowTransactionId;

    public VerifiedReview() {
        super();
    }

    public VerifiedReview(String id, String userId, String bookId, int rating, String comment, String borrowTransactionId) {
        super(id, userId, bookId, rating, comment);
        this.borrowTransactionId = borrowTransactionId;
    }

    public String getBorrowTransactionId() { return borrowTransactionId; }
    public void setBorrowTransactionId(String borrowTransactionId) { this.borrowTransactionId = borrowTransactionId; }

    @Override
    public String getReviewBadge() {
        return "Verified Borrower Review [Tx: " + borrowTransactionId + "]";
    }
}
