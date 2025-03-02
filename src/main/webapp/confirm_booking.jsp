<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.BookingDAO" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    // Retrieve the booking ID, distance, and bill amount
    String bookingIdStr = request.getParameter("booking_id");
    String distanceStr = request.getParameter("distance");
    String billAmountStr = request.getParameter("bill_amount");

    if (bookingIdStr == null || distanceStr == null || billAmountStr == null) {
        response.sendRedirect("view_assigned_bookings.jsp?error=Invalid booking");
        return;
    }

    int bookingId = Integer.parseInt(bookingIdStr);
    double distance = Double.parseDouble(distanceStr);
    double billAmount = Double.parseDouble(billAmountStr);

    // Update the booking in the database
    BookingDAO bookingDAO = new BookingDAO();
    boolean isUpdated = bookingDAO.confirmBooking(bookingId, billAmount, distance);

    if (isUpdated) {
        response.sendRedirect("view_assigned_bookings.jsp?success=Booking confirmed");
    } else {
        response.sendRedirect("view_assigned_bookings.jsp?error=Failed to confirm booking");
    }
%>
