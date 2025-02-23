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

        String customerUsername = (String) session.getAttribute("username");
        int carId = Integer.parseInt(request.getParameter("car_id"));
        String pickupLocation = request.getParameter("pickup_location");
        String dropoffLocation = request.getParameter("dropoff_location");

        // Find an available driver
        UserDAO userDAO = new UserDAO();
        User availableDriver = userDAO.getAvailableDriver();

        if (availableDriver == null) {
            response.sendRedirect("book_cab.jsp?error=No available drivers at the moment");
            return;
        }

        // Save booking
        BookingDAO bookingDAO = new BookingDAO();
        boolean success = bookingDAO.createBooking(new Booking(0, customerUsername, carId, availableDriver.getUsername(), pickupLocation, dropoffLocation, "Pending"));

        if (success) {
            response.sendRedirect("view_bookings.jsp?success=Cab booked successfully");
        } else {
            response.sendRedirect("book_cab.jsp?error=Booking failed");
        }
    }
}
