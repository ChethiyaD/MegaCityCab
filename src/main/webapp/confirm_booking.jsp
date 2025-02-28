<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.BookingDAO" %>
<%@ page import="com.megacitycab.models.Booking" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    // Retrieve the booking ID and amount
    String bookingIdStr = request.getParameter("booking_id");
    String billAmountStr = request.getParameter("bill_amount");

    if (bookingIdStr == null || billAmountStr == null) {
        response.sendRedirect("view_assigned_bookings.jsp?error=Invalid booking");
        return;
    }

    int bookingId = Integer.parseInt(bookingIdStr);
    double billAmount = Double.parseDouble(billAmountStr);

    // Update the booking in the database
    BookingDAO bookingDAO = new BookingDAO();
    boolean isUpdated = bookingDAO.confirmBooking(bookingId, billAmount);

    if (isUpdated) {
        response.sendRedirect("view_assigned_bookings.jsp?success=Booking confirmed");
    } else {
        response.sendRedirect("view_assigned_bookings.jsp?error=Failed to confirm booking");
    }
%>
