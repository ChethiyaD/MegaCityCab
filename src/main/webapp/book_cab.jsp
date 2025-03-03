<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.CarDAO, com.megacitycab.models.Car, java.util.List" %>

<%
    CarDAO carDAO = new CarDAO();
    List<Car> availableCars = carDAO.getAvailableCars(); // Fetch only available cars
%>

<!DOCTYPE html>
<html>
<head>
    <title>Book a Cab</title>
    <script>
        function updateCarImage() {
            var selectedCar = document.getElementById("carSelect");
            var carImage = document.getElementById("carImage");

            if (selectedCar.value) {
                var image = selectedCar.options[selectedCar.selectedIndex].getAttribute("data-image");
                carImage.src = "uploads/" + image;
            } else {
                carImage.src = "uploads/default.png"; // Show default if no car selected
            }
        }
    </script>
</head>
<body>
<h2>Book a Cab</h2>

<% if (availableCars.isEmpty()) { %>
<p style="color: red;">No available cars at the moment. Please check back later.</p>
<a href="customer_dashboard.jsp">Back to Dashboard</a>
<% } else { %>

<form action="BookCabServlet" method="post">
    <label>Pickup Location:</label>
    <input type="text" name="pickup_location" required><br>

    <label>Dropoff Location:</label>
    <input type="text" name="dropoff_location" required><br>

    <label>Select Car:</label>
    <select name="car_id" id="carSelect" onchange="updateCarImage()">
        <% for (Car car : availableCars) { %>
        <option value="<%= car.getId() %>" data-image="<%= car.getImage() %>">
            <%= car.getCarName() %> - <%= car.getCarNumber() %>
        </option>
        <% } %>
    </select>

    <br>
    <img id="carImage" src="uploads/<%= availableCars.get(0).getImage() %>" alt="Car Image" width="150"><br>

    <input type="submit" value="Book Now">
</form>

<br>
<a href="customer_dashboard.jsp">Back to Dashboard</a>

<% } %>

</body>
</html>
