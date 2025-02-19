<%@ page import="java.util.List, com.megacitycab.dao.CarDAO, com.megacitycab.models.Car" %>
<%
  CarDAO carDAO = new CarDAO();
  List<Car> cars = carDAO.getAllCars();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Manage Cars</title>
</head>
<body>
<h1>Manage Cars</h1>

<h2>Car List</h2>
<table border="1">
  <tr>
    <th>Car Name</th>
    <th>Model</th>
    <th>Car Number</th>
    <th>Type</th>
    <th>Availability</th>
    <th>Image</th>
    <th>Actions</th>
  </tr>
  <% for (Car car : cars) { %>
  <tr>
    <td><%= car.getCarName() %></td>
    <td><%= car.getCarModel() %></td>
    <td><%= car.getCarNumber() %></td>
    <td><%= car.getCarType() %></td>
    <td><%= car.isAvailable() ? "Available" : "Not Available" %></td>
    <td><img src="uploads/<%= car.getImage() %>" width="50"></td>
    <td>
      <form action="DeleteCarServlet" method="post">
        <input type="hidden" name="car_number" value="<%= car.getCarNumber() %>">
        <input type="submit" value="Delete">
      </form>
    </td>
  </tr>
  <% } %>
</table>

<h2>Add a New Car</h2>
<form action="AddCarServlet" method="post" enctype="multipart/form-data">
  <label>Car Name:</label> <input type="text" name="car_name" required><br>
  <label>Model:</label> <input type="text" name="car_model" required><br>
  <label>Car Number:</label> <input type="text" name="car_number" required><br>
  <label>Type:</label>
  <select name="car_type">
    <option value="Sedan">Sedan</option>
    <option value="SUV">SUV</option>
    <option value="Hatchback">Hatchback</option>
  </select><br>
  <label>Car Image:</label> <input type="file" name="image"><br>
  <input type="submit" value="Add Car">
</form>

<br>
<a href="admin_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
