package com.megacitycab.controllers;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.CarDAO;
import com.megacitycab.dao.UserDAO;
import com.megacitycab.models.Booking;
import com.megacitycab.models.Car;
import com.megacitycab.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/BookCabServlet")
public class BookCabServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("index.jsp?error=Please login first");
            return;
        }

        try {
            String customerUsername = (String) session.getAttribute("username");
            int carId = Integer.parseInt(request.getParameter("car_id"));
            String pickupLocation = request.getParameter("pickup_location");
            String dropoffLocation = request.getParameter("dropoff_location");

            // Retrieve selected car from the database
            CarDAO carDAO = new CarDAO();
            Car selectedCar = carDAO.getCarById(carId);

            if (selectedCar == null) {
                response.sendRedirect("book_cab.jsp?error=Invalid car selection");
                return;
            }

            // Estimate fare based on car and distance
            double farePerKm = selectedCar.getFarePerKm(); // Uses LKR directly
            int estimatedDistance = getEstimatedDistance(pickupLocation, dropoffLocation);
            double estimatedBill = estimatedDistance * farePerKm;

            // Find an available driver
            UserDAO userDAO = new UserDAO();
            User availableDriver = userDAO.getAvailableDriver();

            if (availableDriver == null) {
                response.sendRedirect("book_cab.jsp?error=No available drivers at the moment");
                return;
            }

            // Save the booking with the estimated bill
            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.createBooking(
                    new Booking(0, customerUsername, carId, availableDriver.getUsername(),
                            pickupLocation, dropoffLocation, "Pending", estimatedBill));

            if (success) {
                response.sendRedirect("view_bookings.jsp?success=Cab booked successfully&bill=LKR " + String.format("%.2f", estimatedBill));
            } else {
                response.sendRedirect("book_cab.jsp?error=Booking failed");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("book_cab.jsp?error=Invalid car selection. Please try again.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("book_cab.jsp?error=Something went wrong. Please try again.");
        }
    }

    // Dummy method for distance estimation (replace with real logic)
    private int getEstimatedDistance(String pickup, String dropoff) {
        // Define sample distances for now
        if (pickup.equalsIgnoreCase("Kurunegala") && dropoff.equalsIgnoreCase("Colombo")) return 100;
        if (pickup.equalsIgnoreCase("Kurunegala") && dropoff.equalsIgnoreCase("Polgahawela")) return 15;
        if (pickup.equalsIgnoreCase("Colombo") && dropoff.equalsIgnoreCase("Kandy")) return 120;
        return 10; // Default distance if not matched
    }
}
