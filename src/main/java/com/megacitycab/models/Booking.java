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

    // Constructor with Car & Driver details (12 parameters)
    public Booking(int id, String customerUsername, int carId, String carNumber, String carName, String carImage,
                   String driverUsername, String driverImage, String pickupLocation, String dropoffLocation, String status, double estimatedBill) {
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
    }

    // Constructor with fewer parameters (useful when car/driver data may not be available)
    public Booking(int id, String customerUsername, int carId, String driverUsername,
                   String pickupLocation, String dropoffLocation, String status, double estimatedBill) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.carId = carId;
        this.driverUsername = driverUsername;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.estimatedBill = estimatedBill;
        this.carNumber = ""; // Placeholder if car data isn't available
        this.carName = "";
        this.carImage = "";
        this.driverImage = ""; // Placeholder if driver data isn't available
    }

    // Getters
    public int getId() { return id; }
    public String getCustomerUsername() { return customerUsername; }
    public int getCarId() { return carId; }
    public String getCarNumber() { return carNumber; } // For JSP
    public String getCarName() { return carName; } // For JSP
    public String getCarImage() { return carImage; } // For JSP
    public String getDriverUsername() { return driverUsername; }
    public String getDriverImage() { return driverImage; } // For JSP
    public String getPickupLocation() { return pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; }
    public String getStatus() { return status; }
    public double getEstimatedBill() { return estimatedBill; }

    // Setters
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

    // Convenience method to check if any car/driver info is missing
    public boolean isCarOrDriverInfoMissing() {
        return carNumber.isEmpty() || carName.isEmpty() || carImage.isEmpty() ||
                driverUsername.isEmpty() || driverImage.isEmpty();
    }
}
