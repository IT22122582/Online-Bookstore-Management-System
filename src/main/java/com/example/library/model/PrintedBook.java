package com.example.library.model;

public class PrintedBook extends Book {
    private String shelfLocation;
    private double weightKg;

    public PrintedBook() {
        super();
    }

    public PrintedBook(String id, String title, String authorId, String isbn, String genre, boolean available, String shelfLocation, double weightKg) {
        super(id, title, authorId, isbn, genre, available);
        this.shelfLocation = shelfLocation;
        this.weightKg = weightKg;
    }

    public String getShelfLocation() { return shelfLocation; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    @Override
    public String getTypeSpecificDetails() {
        return "Physical Printed Book - Shelf: " + shelfLocation + " (Weight: " + weightKg + " kg)";
    }
}
