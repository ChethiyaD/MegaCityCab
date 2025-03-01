<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.BookingDAO, com.megacitycab.models.Booking, java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    String username = (sessionObj != null) ? (String) sessionObj.getAttribute("username") : null;

    if (username == null) {
        response.sendRedirect("index.jsp?error=Unauthorized access");
        return;
    }

    BookingDAO bookingDAO = new BookingDAO();
    List<Booking> bookings = bookingDAO.getBookingsByCustomer(username);
%>

<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
</head>
<body>
<h2>My Bookings</h2>

<table border="1">
    <tr>
        <th>Booking ID</th>
        <th>Pickup Location</th>
        <th>Dropoff Location</th>
        <th>Car</th>
        <th>Driver</th>
        <th>Status</th>
        <th>Distance (KM)</th> <!-- Updated Column -->
        <th>Estimated Bill (LKR)</th> <!-- Updated Column -->
    </tr>
    <% if (bookings != null && !bookings.isEmpty()) { %>
    <% for (Booking booking : bookings) { %>
    <tr>
        <td><%= booking.getId() %></td>
        <td><%= booking.getPickupLocation() %></td>
        <td><%= booking.getDropoffLocation() %></td>
        <td>
            <%= booking.getCarNumber() %> (<%= booking.getCarName() %>)<br>
            <img src="uploads/<%= booking.getCarImage() %>" alt="Car Image" width="80">
        </td>
        <td>
            <%= booking.getDriverUsername() != null ? booking.getDriverUsername() : "Not Assigned" %><br>
            <% if (booking.getDriverImage() != null && !booking.getDriverImage().isEmpty()) { %>
            <img src="uploads/<%= booking.getDriverImage() %>" alt="Driver Image" width="80">
            <% } %>
        </td>
        <td><%= booking.getStatus() %></td>

        <!-- Display 'Pending' if distance is 0.0 -->
        <td><%= booking.getDistance() > 0 ? booking.getDistance() + " KM" : "Pending" %></td>

        <!-- Display 'Calculating...' if bill is 0.0 -->
        <td><%= booking.getEstimatedBill() > 0 ? "LKR " + String.format("%.2f", booking.getEstimatedBill()) : "Calculating..." %></td>
    </tr>
    <% } %>
    <% } else { %>
    <tr>
        <td colspan="8">No bookings found.</td>
    </tr>
    <% } %>
</table>

<br>
<a href="customer_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
