<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.BookingDAO, com.megacitycab.models.Booking" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

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

    // Tax and Discount Settings
    double taxRate = 0.05; // 5% tax
    double discountRate = bookingDAO.hasPreviousBookingWithSameDriver(username, booking.getDriverUsername()) ? 0.10 : 0.0; // 10% discount if same driver before

    double estimatedBill = booking.getEstimatedBill();
    double taxAmount = estimatedBill * taxRate;
    double discountAmount = estimatedBill * discountRate;
    double totalAmount = estimatedBill + taxAmount - discountAmount;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Payment Summary</title>
</head>
<body>
<h2>Payment Summary</h2>

<table border="1">
    <tr><td>Booking ID:</td><td><%= booking.getId() %></td></tr>
    <tr><td>Pickup Location:</td><td><%= booking.getPickupLocation() %></td></tr>
    <tr><td>Dropoff Location:</td><td><%= booking.getDropoffLocation() %></td></tr>
    <tr><td>Driver:</td><td><%= booking.getDriverUsername() %></td></tr>
    <tr><td>Estimated Bill:</td><td>LKR <%= String.format("%.2f", estimatedBill) %></td></tr>
    <tr><td>Tax (5%):</td><td>LKR <%= String.format("%.2f", taxAmount) %></td></tr>
    <tr><td>Discount (<%= discountRate * 100 %>% if applicable):</td><td>LKR <%= String.format("%.2f", discountAmount) %></td></tr>
    <tr><td><b>Total Amount:</b></td><td><b>LKR <%= String.format("%.2f", totalAmount) %></b></td></tr>
</table>

<!-- Redirect to payment_details.jsp -->
<form action="payment_details.jsp" method="get">
    <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
    <input type="hidden" name="total_amount" value="<%= totalAmount %>">
    <input type="submit" value="Proceed to Payment">
</form>

<br>
<a href="view_bookings.jsp">Back to Bookings</a>
</body>
</html>
