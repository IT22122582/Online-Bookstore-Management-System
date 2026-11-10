package com.example.library.model;

public class GuestAuthor extends Author {
    private String institution;
    private int visitDurationDays;

    public GuestAuthor() {
        super();
    }

    public GuestAuthor(String id, String name, String bio, String institution, int visitDurationDays) {
        super(id, name, bio);
        this.institution = institution;
        this.visitDurationDays = visitDurationDays;
    }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public int getVisitDurationDays() { return visitDurationDays; }
    public void setVisitDurationDays(int visitDurationDays) { this.visitDurationDays = visitDurationDays; }

    @Override
    public String getAuthorTypeSummary() {
        return "Guest Author from " + institution + " (Visiting for " + visitDurationDays + " days)";
    }
}
