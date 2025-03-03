<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.megacitycab.dao.BookingDAO, com.megacitycab.models.Booking" %>

<%
    HttpSession sessionObj = request.getSession(false);
    String username = (sessionObj != null) ? (String) sessionObj.getAttribute("username") : null;

    if (username == null) {
        response.sendRedirect("index.jsp?error=Unauthorized access");
        return;
    }

    String bookingIdParam = request.getParameter("booking_id");
    if (bookingIdParam == null) {
        response.sendRedirect("view_bookings.jsp?error=Invalid booking ID");
        return;
    }

    int bookingId = Integer.parseInt(bookingIdParam);
    BookingDAO bookingDAO = new BookingDAO();
    Booking booking = bookingDAO.getBookingById(bookingId);

    if (booking == null) {
        response.sendRedirect("view_bookings.jsp?error=Booking not found");
        return;
    }

    double totalAmount = Double.parseDouble(request.getParameter("total_amount"));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Payment Successful</title>
    <style>
        .success-message {
            color: green;
            font-size: 18px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h2 class="success-message">Payment Successful!</h2>
<p>Thank you, <b><%= username %></b>. Your payment has been successfully processed.</p>

<h3>Payment Details</h3>
<table border="1">
    <tr><td>Booking ID:</td><td><%= booking.getId() %></td></tr>
    <tr><td>Pickup Location:</td><td><%= booking.getPickupLocation() %></td></tr>
    <tr><td>Dropoff Location:</td><td><%= booking.getDropoffLocation() %></td></tr>
    <tr><td>Driver:</td><td><%= booking.getDriverUsername() %></td></tr>
    <tr><td>Total Paid:</td><td><b>LKR <%= String.format("%.2f", totalAmount) %></b></td></tr>
    <tr><td>Status:</td><td><b>Paid</b></td></tr>
</table>

<br>
<a href="view_bookings.jsp">Back to My Bookings</a>
</body>
</html>
