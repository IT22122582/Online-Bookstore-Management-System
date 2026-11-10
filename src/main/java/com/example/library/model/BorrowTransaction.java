package com.example.library.model;

import java.time.LocalDate;

public class BorrowTransaction {
    private String id;
    private String userId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate; // due date initially, actual return date after returned is true
    private boolean returned;
    private double finePaid;

    public BorrowTransaction() {}

    public BorrowTransaction(String id, String userId, String bookId, LocalDate borrowDate, LocalDate returnDate, boolean returned, double finePaid) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.finePaid = finePaid;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public double getFinePaid() { return finePaid; }
    public void setFinePaid(double finePaid) { this.finePaid = finePaid; }
}
