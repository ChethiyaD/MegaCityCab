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
    <script>
        function confirmCancellation(form) {
            if (confirm('Are you sure you want to cancel this booking?')) {
                form.submit();
            }
        }
    </script>
    <style>
        .cancelled {
            background-color: #f8d7da;
            color: red;
            font-weight: bold;
        }
        .no-actions {
            color: gray;
        }
    </style>
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
        <th>Distance (KM)</th>
        <th>Estimated Bill (LKR)</th>
        <th>Actions</th>
    </tr>

    <% if (bookings != null && !bookings.isEmpty()) { %>
    <% for (Booking booking : bookings) { %>
    <tr class="<%= "Cancelled".equals(booking.getStatus()) ? "cancelled" : "" %>">
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
        <td>
            <% if ("Cancelled".equals(booking.getStatus())) { %>
            <span style="color: red;">Cancelled</span>
            <% } else { %>
            <%= booking.getStatus() %>
            <% } %>
        </td>

        <td><%= booking.getDistance() > 0 ? booking.getDistance() + " KM" : "Pending" %></td>

        <td><%= booking.getEstimatedBill() > 0 ? "LKR " + String.format("%.2f", booking.getEstimatedBill()) : "Calculating..." %></td>

        <td>
            <% if ("Confirmed".equals(booking.getStatus())) { %>
            <!-- Pay Now Button (Only for Confirmed Bookings) -->
            <form action="payment_details.jsp" method="get">
                <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
                <input type="hidden" name="total_amount" value="<%= booking.getEstimatedBill() %>">
                <input type="submit" value="Pay Now">
            </form>

            <!-- Cancel Booking Button -->
            <form action="CancelBookingByCustomerServlet" method="post" onsubmit="event.preventDefault(); confirmCancellation(this);">
                <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
                <input type="submit" value="Cancel Booking">
            </form>
            <% } else if ("Cancelled".equals(booking.getStatus())) { %>
            <span class="no-actions">No Actions</span>
            <% } else if ("Paid".equals(booking.getStatus())) { %>
            <span class="no-actions">Paid</span>
            <% } %>
        </td>

    </tr>
    <% } %>
    <% } else { %>
    <tr>
        <td colspan="9">No bookings found.</td>
    </tr>
    <% } %>
</table>

<br>
<a href="customer_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
