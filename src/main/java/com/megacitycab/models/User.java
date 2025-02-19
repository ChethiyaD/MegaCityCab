package com.megacitycab.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String name;
    private String address;
    private String phone;
    private String nic;
    private String profilePicture;
    private int experience;  // Driver experience
    private String status;   // Driver availability status

    // ✅ Default Constructor
    public User() {}

    // ✅ Constructor for Customers (no experience & status)
    public User(String username, String password, String role, String name, String address, String phone, String nic, String profilePicture) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.nic = nic;
        this.profilePicture = profilePicture;
        this.experience = 0;  // Default for customers
        this.status = null;   // Customers don't have a status
    }

    // ✅ Constructor for Drivers (includes experience & status)
    public User(String username, String password, String role, String name, String address, String phone, String nic, String profilePicture, int experience, String status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.nic = nic;
        this.profilePicture = profilePicture;
        this.experience = experience;
        this.status = status;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
