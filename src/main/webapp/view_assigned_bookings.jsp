<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.BookingDAO, com.megacitycab.models.Booking, java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.megacitycab.dao.CarDAO" %>

<%
    // Get the session object and check if the user is logged in as a driver
    HttpSession sessionObj = request.getSession(false);
    String driverUsername = (sessionObj != null) ? (String) sessionObj.getAttribute("username") : null;

    if (driverUsername == null) {
        response.sendRedirect("index.jsp?error=Unauthorized access");
        return;
    }

    // Get bookings assigned to this driver
    BookingDAO bookingDAO = new BookingDAO();
    List<Booking> assignedBookings = bookingDAO.getBookingsByDriver(driverUsername); // Get all the bookings assigned to this driver
%>

<!DOCTYPE html>
<html>
<head>
    <title>Assigned Bookings</title>
</head>
<body>
<h2>Assigned Bookings</h2>

<% if (assignedBookings.isEmpty()) { %>
<p>No assigned bookings.</p>
<% } else { %>
<table border="1">
    <tr>
        <th>Booking ID</th>
        <th>Customer Name</th>
        <th>Car</th>
        <th>Pickup Location</th>
        <th>Dropoff Location</th>
        <th>Driver</th>
        <th>Status</th>
        <th>Fare Per Km</th> <!-- Fare Per Km Column -->
        <th>Add Bill</th>
        <th>Confirm Booking</th>
    </tr>

    <% for (Booking booking : assignedBookings) { %>
    <tr>
        <td><%= booking.getId() %></td>
        <td><%= booking.getCustomerUsername() %></td>
        <td>
            <%= booking.getCarName() %><br>
            <img src="uploads/<%= booking.getCarImage() %>" alt="Car Image" width="80">
        </td>
        <td><%= booking.getPickupLocation() %></td>
        <td><%= booking.getDropoffLocation() %></td>
        <td><%= booking.getDriverUsername() %></td>
        <td><%= booking.getStatus() %></td>
        <td>
            <!-- Show Fare Per Km -->
            <%= booking.getCarId() != 0 ? new CarDAO().getCarById(booking.getCarId()).getFarePerKm() : "N/A" %> LKR
        </td>
        <td>
            <!-- Form to add bill -->
            <form action="confirm_booking.jsp" method="post">
                <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
                <input type="number" name="bill_amount" step="0.01" placeholder="Enter amount" required>
                <input type="submit" value="Confirm Booking">
            </form>
        </td>
    </tr>
    <% } %>
</table>
<% } %>

</body>
</html>
