<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.CarDAO, com.megacitycab.models.Car, java.util.List" %>

<%
    CarDAO carDAO = new CarDAO();
    List<Car> availableCars = carDAO.getAvailableCars(); // Ensure this method exists in CarDAO
%>

<!DOCTYPE html>
<html>
<head>
    <title>Book a Cab</title>
    <script>
        function updateCarImage() {
            var selectedCar = document.getElementById("carSelect");
            var carImage = document.getElementById("carImage");

            var image = selectedCar.options[selectedCar.selectedIndex].getAttribute("data-image");
            carImage.src = "uploads/" + image;
        }
    </script>
</head>
<body>
<h2>Book a Cab</h2>

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
    <img id="carImage" src="uploads/<%= availableCars.size() > 0 ? availableCars.get(0).getImage() : "default.png" %>" alt="Car Image" width="150"><br>

    <input type="submit" value="Book Now">
</form>

<br>
<a href="customer_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
