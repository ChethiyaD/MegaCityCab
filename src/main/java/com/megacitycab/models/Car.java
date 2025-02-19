package com.megacitycab.models;

public class Car {
    private int id;
    private String carName;
    private String carModel;
    private String carNumber;
    private String carType;
    private boolean availability;
    private String image;

    // Constructor
    public Car(int id, String carName, String carModel, String carNumber, String carType, boolean availability, String image) {
        this.id = id;
        this.carName = carName;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carType = carType;
        this.availability = availability;
        this.image = image;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public boolean isAvailable() { return availability; }
    public void setAvailable(boolean availability) { this.availability = availability; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
