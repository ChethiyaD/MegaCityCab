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
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

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

  <script>
    function openEditModal(id, carName, carModel, carNumber, carType, farePerKm, availability, image) {
      document.getElementById("editModalTitle").innerText = id ? "Edit Car" : "Add Car";
      document.getElementById("editCarId").value = id || '';
      document.getElementById("editCarName").value = carName || '';
      document.getElementById("editCarModel").value = carModel || '';
      document.getElementById("editCarNumber").value = carNumber || '';
      document.getElementById("editCarType").value = carType || 'Sedan';
      document.getElementById("editFarePerKm").value = farePerKm || '0.00';
      document.getElementById("editAvailability").value = availability || 'Available';

      if (image) {
        document.getElementById("currentCarImage").src = "uploads/" + image;
        document.getElementById("existingImage").value = image;
      } else {
        document.getElementById("currentCarImage").src = "uploads/default.png";
        document.getElementById("existingImage").value = "default.png";
      }

      var modal = new bootstrap.Modal(document.getElementById("editCarModal"));
      modal.show();
    }

    function searchCars() {
      let input = document.getElementById("searchInput").value.toLowerCase();
      let rows = document.querySelectorAll("#carsTable tbody tr");

      rows.forEach(row => {
        let carName = row.cells[0].textContent.toLowerCase();
        let carModel = row.cells[1].textContent.toLowerCase();
        let carNumber = row.cells[2].textContent.toLowerCase();
        let carType = row.cells[3].textContent.toLowerCase();
        let availability = row.cells[4].textContent.toLowerCase();
        let farePerKm = row.cells[6].textContent.toLowerCase();

        if (carName.includes(input) || carModel.includes(input) || carNumber.includes(input) ||
                carType.includes(input) || availability.includes(input) || farePerKm.includes(input)) {
          row.style.display = "";
        } else {
          row.style.display = "none";
        }
      });
    }
  </script>
</head>
<body>
<h2>Manage Cars</h2>

<!-- Add Search Bar -->
<input type="text" id="searchInput" class="form-control mb-3"
       placeholder="Search by Name, Model, Number, Type, Availability, or Fare"
       onkeyup="searchCars()">

<!-- "Add Car" Button (Opens Modal) -->
<button type="button" class="btn btn-primary" onclick="openEditModal('', '', '', '', 'Sedan', '0.00', 'Available', 'default.png')">
  Add Car
</button>

<!-- Add ID to table and tbody -->
<table border="1" class="mt-3" id="carsTable">
  <thead>
  <tr>
    <th>Car Name</th>
    <th>Model</th>
    <th>Car Number</th>
    <th>Type</th>
    <th>Availability</th>
    <th>Image</th>
    <th>Fare per KM (Rs.)</th>
    <th>Actions</th>
  </tr>
  </thead>
  <tbody>
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
    <td><%= car.getFarePerKmLKR() %></td>
    <td class="action-buttons">
      <!-- Edit Button -->
      <button class="btn btn-warning btn-sm" onclick="openEditModal(
              '<%= car.getId() %>',
              '<%= car.getCarName() %>',
              '<%= car.getCarModel() %>',
              '<%= car.getCarNumber() %>',
              '<%= car.getCarType() %>',
              '<%= car.getFarePerKmLKR() %>',
              '<%= car.isAvailable() ? "Available" : "Not Available" %>',
              '<%= car.getImage() %>'
              )">Edit</button>

      <!-- Delete Button -->
      <form action="DeleteCarServlet" method="post" style="display:inline;">
        <input type="hidden" name="car_id" value="<%= car.getId() %>">
        <input type="submit" class="btn btn-danger btn-sm" value="Delete">
      </form>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>

<!-- Bootstrap Modal for Adding/Editing a Car -->
<div class="modal fade" id="editCarModal" tabindex="-1" aria-labelledby="editModalTitle" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editModalTitle">Edit Car</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editForm" action="AddCarServlet" method="post" enctype="multipart/form-data">
          <input type="hidden" name="car_id" id="editCarId">
          <input type="hidden" name="existing_image" id="existingImage">

          <label>Car Name:</label>
          <input type="text" name="car_name" id="editCarName" class="form-control" required><br>

          <label>Model:</label>
          <input type="text" name="car_model" id="editCarModel" class="form-control" required><br>

          <label>Car Number:</label>
          <input type="text" name="car_number" id="editCarNumber" class="form-control" required><br>

          <label>Type:</label>
          <select name="car_type" id="editCarType" class="form-control">
            <option value="Sedan">Sedan</option>
            <option value="SUV">SUV</option>
            <option value="Hatchback">Hatchback</option>
          </select><br>

          <label>Fare per KM (Rs.):</label>
          <input type="number" step="0.01" name="fare_per_km" id="editFarePerKm" class="form-control" required><br>

          <label>Availability:</label>
          <select name="availability" id="editAvailability" class="form-control">
            <option value="Available">Available</option>
            <option value="Not Available">Not Available</option>
          </select><br>

          <label>Car Image:</label>
          <input type="file" name="image" class="form-control"><br>

          <!-- Show current picture -->
          <img id="currentCarImage" src="uploads/default.png" width="100"><br>

          <button type="submit" class="btn btn-success">Save Changes</button>
        </form>
      </div>
    </div>
  </div>
</div>

<br>
<a href="admin_dashboard.jsp" class="btn btn-secondary mt-3">Back to Dashboard</a>
</body>
</html>