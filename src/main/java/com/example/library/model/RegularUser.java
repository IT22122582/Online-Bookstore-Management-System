package com.example.library.model;

public class RegularUser extends User {
    private String membershipType; // "STANDARD", "PREMIUM"
    private int maxBooksAllowed;

    public RegularUser() {
        super();
        setRole("REGULAR");
    }

    public RegularUser(String id, String username, String password, String email, String membershipType) {
        super(id, username, password, email, "REGULAR");
        this.membershipType = membershipType;
        this.maxBooksAllowed = "PREMIUM".equalsIgnoreCase(membershipType) ? 10 : 5;
    }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { 
        this.membershipType = membershipType;
        this.maxBooksAllowed = "PREMIUM".equalsIgnoreCase(membershipType) ? 10 : 5;
    }

    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }

    @Override
    public String getUserSummary() {
        return "Library Member (" + membershipType + " Membership) - Username: " + getUsername() + ", Limit: " + maxBooksAllowed + " books";
    }
}
