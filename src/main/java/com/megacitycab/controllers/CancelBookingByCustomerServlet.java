package com.megacitycab.controllers;

import com.megacitycab.dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/CancelBookingByCustomerServlet")
public class CancelBookingByCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdStr = request.getParameter("booking_id");

        if (bookingIdStr == null) {
            response.sendRedirect("view_bookings.jsp?error=Invalid booking ID");
            return;
        }

        int bookingId = Integer.parseInt(bookingIdStr);
        BookingDAO bookingDAO = new BookingDAO();

        boolean isCanceled = bookingDAO.cancelBookingByCustomer(bookingId);

        if (isCanceled) {
            response.sendRedirect("view_bookings.jsp?success=Booking canceled successfully");
        } else {
            response.sendRedirect("view_bookings.jsp?error=Failed to cancel booking");
        }
    }
}
