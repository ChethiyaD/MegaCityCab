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
    List<Booking> assignedBookings = bookingDAO.getBookingsByDriver(driverUsername);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Assigned Bookings</title>
    <script>
        function calculateBill(input) {
            let row = input.closest("tr");
            let farePerKm = parseFloat(row.querySelector(".farePerKm").innerText.replace(" LKR", ""));
            let distance = parseFloat(input.value);
            if (!isNaN(farePerKm) && !isNaN(distance)) {
                let billAmount = farePerKm * distance;
                row.querySelector(".calculatedBill").value = billAmount.toFixed(2);
            }
        }
    </script>
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
        <th>Fare Per KM</th>
        <th>Distance (KM)</th>
        <th>Estimated Bill</th>
        <th>Confirm Booking</th>
        <th>Cancel Booking</th> <!-- New Cancel Button -->
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
        <td class="farePerKm">
            <%= booking.getCarId() != 0 ? new CarDAO().getFarePerKm(booking.getCarId()) : "N/A" %> LKR
        </td>
        <td>
            <!-- Input for Distance -->
            <input type="number" name="distance" step="0.01" placeholder="Enter distance" required oninput="calculateBill(this)">
        </td>
        <td>
            <!-- Auto-calculated Bill -->
            <input type="text" class="calculatedBill" readonly>
        </td>
        <td>
            <!-- Form to confirm booking -->
            <form action="confirm_booking.jsp" method="post">
                <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
                <input type="hidden" name="fare_per_km" value="<%= booking.getCarId() != 0 ? new CarDAO().getFarePerKm(booking.getCarId()) : "0" %>">
                <input type="hidden" name="bill_amount" class="calculatedBill">
                <input type="hidden" name="distance" value="">
                <input type="submit" value="Confirm Booking" onclick="this.form.distance.value = this.closest('tr').querySelector('input[name=distance]').value; this.form.bill_amount.value = this.closest('tr').querySelector('.calculatedBill').value;">
            </form>
        </td>
        <td>
            <!-- Cancel Booking Button -->
            <form action="CancelBookingServlet" method="post">
                <input type="hidden" name="booking_id" value="<%= booking.getId() %>">
                <input type="submit" value="Cancel Booking" onclick="return confirm('Are you sure you want to cancel this booking?');">
            </form>
        </td>
    </tr>
    <% } %>
</table>
<% } %>

<a href="driver_dashboard.jsp" class="btn btn-secondary mt-3">Back to Dashboard</a>

</body>
</html>