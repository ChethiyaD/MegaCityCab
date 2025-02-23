package com.megacitycab.dao;

import com.megacitycab.models.Car;
import com.megacitycab.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private Connection conn;

    public CarDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all cars (Fix for manage_cars.jsp)
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Get a specific car by ID (Fix for edit_car.jsp)
    public Car getCarById(int carId) {
        String query = "SELECT * FROM cars WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new car
    public boolean addCar(Car car) {
        String query = "INSERT INTO cars (car_name, car_model, car_number, car_type, availability, image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarModel());
            stmt.setString(3, car.getCarNumber());
            stmt.setString(4, car.getCarType());
            stmt.setBoolean(5, car.isAvailable());
            stmt.setString(6, car.getImage());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update a car (Fix: Correct column names)
    public boolean updateCar(Car car) {
        String query = "UPDATE cars SET car_name=?, car_model=?, car_type=?, availability=?, image=? WHERE car_number=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarModel());
            stmt.setString(3, car.getCarType());
            stmt.setBoolean(4, car.isAvailable());
            stmt.setString(5, car.getImage());
            stmt.setString(6, car.getCarNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a car
    public boolean deleteCar(String carNumber) {
        String query = "DELETE FROM cars WHERE car_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get only available cars
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE availability = 1"; // Only fetch available cars

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                availableCars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("car_model"),
                        rs.getString("car_number"),
                        rs.getString("car_type"),
                        rs.getBoolean("availability"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
    }

}
