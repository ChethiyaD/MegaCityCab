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

    // Get all cars
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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

    // Add a new car
    public boolean addCar(Car car) {
        String query = "INSERT INTO cars (car_name, car_model, car_number, car_type, availability, image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarModel());
            stmt.setString(3, car.getCarNumber());
            stmt.setString(4, car.getCarType());
            stmt.setBoolean(5, true); // Default availability
            stmt.setString(6, car.getImage());
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


}
