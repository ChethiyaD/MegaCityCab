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
    <title>Payment Summary & Details</title>
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

<h2>Enter Payment Details</h2>

<form action="ProcessPaymentServlet" method="post">
    <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
    <input type="hidden" name="total_amount" value="<%= totalAmount %>">

    <label>Card Holder Name:</label>
    <input type="text" name="card_holder_name" required><br>

    <label>Card Number:</label>
    <input type="text" name="card_number" required pattern="\d{16}" maxlength="16"><br>

    <label>Expiry Date (MM/YY):</label>
    <input type="text" name="expiry_date" required pattern="\d{2}/\d{2}" maxlength="5"><br>

    <label>CVV:</label>
    <input type="text" name="cvv" required pattern="\d{3}" maxlength="3"><br>

    <input type="submit" value="Submit Payment">
</form>

<br>
<a href="view_bookings.jsp">Cancel</a>
</body>
</html>
