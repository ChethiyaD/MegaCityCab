package com.megacitycab.controllers;

import com.megacitycab.dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdStr = request.getParameter("booking_id");

        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect("view_assigned_bookings.jsp?error=Invalid booking ID");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            BookingDAO bookingDAO = new BookingDAO();

            boolean isCanceled = bookingDAO.cancelBooking(bookingId);

            if (isCanceled) {
                response.sendRedirect("view_assigned_bookings.jsp?success=Booking canceled successfully");
            } else {
                response.sendRedirect("view_assigned_bookings.jsp?error=Failed to cancel booking");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("view_assigned_bookings.jsp?error=Invalid booking ID format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view_assigned_bookings.jsp?error=An unexpected error occurred");
        }
    }
}
