<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, com.megacitycab.dao.CarDAO, com.megacitycab.models.Car" %>
<%
  if (session.getAttribute("role") == null || !"admin".equals(session.getAttribute("role"))) {
    response.sendRedirect("index.jsp");
    return;
  }

  CarDAO carDAO = new CarDAO();
  List<Car> cars = carDAO.getAllCars();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Manage Cars</title>
  <!-- Bootstrap for Modal -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

  <style>
    table {
      border-collapse: collapse;
      width: 100%;
    }
    th, td {
      border: 1px solid black;
      padding: 8px;
      text-align: center;
    }
    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 5px;
    }
  </style>
</head>
<body>
<h2>Manage Cars</h2>

<!-- "Add Car" Button (Opens Modal) -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCarModal">
  Add Car
</button>

<table border="1" class="mt-3">
  <tr>
    <th>Car Name</th>
    <th>Model</th>
    <th>Car Number</th>
    <th>Type</th>
    <th>Availability</th>
    <th>Image</th>
    <th>Fare per KM (Rs.)</th> <!-- ✅ Title updated -->
    <th>Actions</th>
  </tr>
  <% for (Car car : cars) { %>
  <tr>
    <td><%= car.getCarName() %></td>
    <td><%= car.getCarModel() %></td>
    <td><%= car.getCarNumber() %></td>
    <td><%= car.getCarType() %></td>
    <td><%= car.isAvailable() ? "Available" : "Not Available" %></td>
    <td>
      <img src="uploads/<%= car.getImage() %>" alt="Car Image" width="50">
    </td>
    <td><%= car.getFarePerKmLKR() %></td> <!-- ✅ Displaying fare in LKR -->
    <td class="action-buttons">
      <!-- Edit Button -->
      <form action="edit_car.jsp" method="get" style="display:inline;">
        <input type="hidden" name="car_id" value="<%= car.getId() %>">
        <input type="submit" class="btn btn-warning btn-sm" value="Edit">
      </form>

      <!-- Delete Button -->
      <form action="DeleteCarServlet" method="post" style="display:inline;">
        <input type="hidden" name="car_number" value="<%= car.getCarNumber() %>">
        <input type="submit" class="btn btn-danger btn-sm" value="Delete">
      </form>
    </td>
  </tr>
  <% } %>
</table>

<!-- Bootstrap Modal for Adding a New Car -->
<div class="modal fade" id="addCarModal" tabindex="-1" aria-labelledby="addCarModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addCarModalLabel">Add a New Car</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form action="AddCarServlet" method="post" enctype="multipart/form-data">
          <label>Car Name:</label> <input type="text" name="car_name" class="form-control" required><br>
          <label>Model:</label> <input type="text" name="car_model" class="form-control" required><br>
          <label>Car Number:</label> <input type="text" name="car_number" class="form-control" required><br>
          <label>Type:</label>
          <select name="car_type" class="form-control">
            <option value="Sedan">Sedan</option>
            <option value="SUV">SUV</option>
            <option value="Hatchback">Hatchback</option>
          </select><br>
          <label>Fare per KM (Rs.):</label> <input type="number" step="0.01" name="fare_per_km" class="form-control" required><br> <!-- ✅ Updated label -->
          <label>Car Image:</label> <input type="file" name="image" class="form-control"><br>
          <button type="submit" class="btn btn-success">Add Car</button>
        </form>
      </div>
    </div>
  </div>
</div>

<br>
<a href="logout.jsp" class="btn btn-secondary mt-3">Logout</a>
</body>
</html>
