package com.megacitycab.models;

public class Car {
    private int id;
    private String carName;
    private String carModel;
    private String carNumber;
    private String carType;
    private boolean availability;
    private String image;
    private double farePerKm; // Stored in LKR directly

    // Constructor with farePerKm in LKR
    public Car(int id, String carName, String carModel, String carNumber, String carType,
               boolean availability, String image, double farePerKm) {
        this.id = id;
        this.carName = carName;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carType = carType;
        this.availability = availability;
        this.image = image;
        this.farePerKm = farePerKm;
    }

    // Constructor WITHOUT farePerKm (for backward compatibility)
    public Car(int id, String carName, String carModel, String carNumber, String carType,
               boolean availability, String image) {
        this(id, carName, carModel, carNumber, carType, availability, image, 0.0);
    }

    // Getters
    public int getId() { return id; }
    public String getCarName() { return carName; }
    public String getCarModel() { return carModel; }
    public String getCarNumber() { return carNumber; }
    public String getCarType() { return carType; }
    public boolean isAvailable() { return availability; }
    public String getImage() { return image; }
    public double getFarePerKm() { return farePerKm; }

    // Formats fare per KM in LKR (Rs.)
    public String getFarePerKmLKR() {
        return "Rs. " + String.format("%.2f", farePerKm);
    }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCarName(String carName) { this.carName = carName; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }
    public void setCarType(String carType) { this.carType = carType; }
    public void setAvailable(boolean availability) { this.availability = availability; }
    public void setImage(String image) { this.image = image; }
    public void setFarePerKm(double farePerKm) { this.farePerKm = farePerKm; }
}
