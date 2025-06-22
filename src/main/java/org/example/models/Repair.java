package org.example.models;

public class Repair {
    private int repairId;
    private String clientName;
    private int machineId;
    private String brand;
    private int year;
    private String repairType;
    private String startDate;
    private int durationDays;
    private double cost;
    private String contactInfo;
    private String country;

    public Repair(int repairId, String clientName, int machineId, String brand, int year, String repairType, String startDate, int durationDays, double cost, String contactInfo, String country) {
        this.repairId = repairId;
        this.clientName = clientName;
        this.machineId = machineId;
        this.brand = brand;
        this.year = year;
        this.repairType = repairType;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.cost = cost;
        this.contactInfo = contactInfo;
        this.country = country;
    }

    public int getRepairId() { return repairId; }
    public String getClientName() { return clientName; }
    public int getMachineId() { return machineId; }
    public String getBrand() { return brand; }
    public int getYear() { return year; }
    public String getRepairType() { return repairType; }
    public String getStartDate() { return startDate; }
    public int getDurationDays() { return durationDays; }
    public double getCost() { return cost; }
    public String getContactInfo() { return contactInfo; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %s (ID: %d)", clientName, brand, year, country, machineId);
    }
}