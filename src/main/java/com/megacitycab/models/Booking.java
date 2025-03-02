package com.megacitycab.models;

public class Booking {
    private int id;
    private String customerUsername;
    private int carId;
    private String carNumber;
    private String carName;
    private String carImage;
    private String driverUsername;
    private String driverImage;
    private String pickupLocation;
    private String dropoffLocation;
    private String status;
    private double estimatedBill; // Fare in LKR
    private double distance; // Distance in KM


    public Booking(int id, String customerUsername, int carId, String carNumber, String carName, String carImage,
                   String driverUsername, String driverImage, String pickupLocation, String dropoffLocation,
                   String status, double estimatedBill, double distance) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.carId = carId;
        this.carNumber = carNumber;
        this.carName = carName;
        this.carImage = carImage;
        this.driverUsername = driverUsername;
        this.driverImage = driverImage;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.estimatedBill = estimatedBill;
        this.distance = distance;
    }


    public Booking(int id, String customerUsername, int carId, String pickupLocation, String dropoffLocation,
                   String status, double estimatedBill) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.carId = carId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.estimatedBill = estimatedBill;
        this.distance = 0.0; // Default distance to 0 until driver updates it
        this.carNumber = "";
        this.carName = "";
        this.carImage = "";
        this.driverUsername = "";
        this.driverImage = "";
    }


    public Booking(int id, String customerUsername, int carId, String driverUsername, String pickupLocation,
                   String dropoffLocation, String status, double estimatedBill, double distance) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.carId = carId;
        this.driverUsername = driverUsername;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.estimatedBill = estimatedBill;
        this.distance = distance;
        this.carNumber = "";
        this.carName = "";
        this.carImage = "";
        this.driverImage = "";
    }


    public int getId() { return id; }
    public String getCustomerUsername() { return customerUsername; }
    public int getCarId() { return carId; }
    public String getCarNumber() { return carNumber; }
    public String getCarName() { return carName; }
    public String getCarImage() { return carImage; }
    public String getDriverUsername() { return driverUsername; }
    public String getDriverImage() { return driverImage; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; }
    public String getStatus() { return status; }
    public double getEstimatedBill() { return estimatedBill; }
    public double getDistance() { return distance; }


    public void setId(int id) { this.id = id; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }
    public void setCarId(int carId) { this.carId = carId; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }
    public void setCarName(String carName) { this.carName = carName; }
    public void setCarImage(String carImage) { this.carImage = carImage; }
    public void setDriverUsername(String driverUsername) { this.driverUsername = driverUsername; }
    public void setDriverImage(String driverImage) { this.driverImage = driverImage; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
    public void setStatus(String status) { this.status = status; }
    public void setEstimatedBill(double estimatedBill) { this.estimatedBill = estimatedBill; }
    public void setDistance(double distance) { this.distance = distance; }


    public boolean isCarOrDriverInfoMissing() {
        return (carNumber == null || carNumber.isEmpty()) ||
                (carName == null || carName.isEmpty()) ||
                (carImage == null || carImage.isEmpty()) ||
                (driverUsername == null || driverUsername.isEmpty()) ||
                (driverImage == null || driverImage.isEmpty());
    }
}
