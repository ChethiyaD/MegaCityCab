package com.megacitycab.controllers;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.dao.PaymentDAO;
import com.megacitycab.models.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/ProcessPaymentServlet")
public class ProcessPaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("booking_id"));
        double totalAmount = Double.parseDouble(request.getParameter("total_amount"));
        String cardNumber = request.getParameter("card_number");
        String cardHolderName = request.getParameter("card_holder_name");
        String expiryDate = request.getParameter("expiry_date");
        String cvv = request.getParameter("cvv");

        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            response.sendRedirect("view_bookings.jsp?error=Booking not found");
            return;
        }

        // Update booking status to "Paid"
        boolean paymentSuccess = bookingDAO.updateBookingStatus(bookingId, totalAmount);

        if (paymentSuccess) {
            // Save payment details in the `payment` table
            boolean paymentRecorded = paymentDAO.savePayment(
                    bookingId, booking.getCustomerUsername(), cardNumber, cardHolderName, expiryDate, cvv, totalAmount
            );

            if (paymentRecorded) {
                response.sendRedirect("payment_success.jsp?booking_id=" + bookingId + "&total_amount=" + totalAmount);
            } else {
                response.sendRedirect("view_bookings.jsp?error=Payment not saved to database");
            }
        } else {
            response.sendRedirect("view_bookings.jsp?error=Payment processing failed");
        }
    }
}
