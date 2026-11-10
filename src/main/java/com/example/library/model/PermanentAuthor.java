package com.example.library.model;

public class PermanentAuthor extends Author {
    private int contractPeriodYears;
    private String officeNumber;

    public PermanentAuthor() {
        super();
    }

    public PermanentAuthor(String id, String name, String bio, int contractPeriodYears, String officeNumber) {
        super(id, name, bio);
        this.contractPeriodYears = contractPeriodYears;
        this.officeNumber = officeNumber;
    }

    public int getContractPeriodYears() { return contractPeriodYears; }
    public void setContractPeriodYears(int contractPeriodYears) { this.contractPeriodYears = contractPeriodYears; }

    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }

    @Override
    public String getAuthorTypeSummary() {
        return "Permanent Author - Office: " + officeNumber + " (Contract: " + contractPeriodYears + " years)";
    }
}
