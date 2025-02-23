<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, com.megacitycab.dao.UserDAO, com.megacitycab.models.User" %>
<%
    if (session.getAttribute("role") == null || !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.jsp");
        return;
    }

    UserDAO userDAO = new UserDAO();
    List<User> drivers = userDAO.getAllDrivers();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Drivers</title>
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
<h2>Manage Drivers</h2>

<!-- "Add Driver" Button (Opens Modal) -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addDriverModal">
    Add Driver
</button>

<table border="1" class="mt-3">
    <tr>
        <th>Username</th>
        <th>Name</th>
        <th>Phone</th>
        <th>Experience</th>
        <th>Status</th>
        <th>Profile Picture</th>
        <th>Actions</th>
    </tr>
    <% for (User driver : drivers) { %>
    <tr>
        <td><%= driver.getUsername() %></td>
        <td><%= driver.getName() %></td>
        <td><%= driver.getPhone() %></td>
        <td><%= driver.getExperience() %> years</td>
        <td><%= (driver.getStatus() != null) ? driver.getStatus() : "Not Set" %></td>
        <td>
            <img src="uploads/<%= driver.getProfilePicture() %>" alt="Profile Picture" width="50">
        </td>
        <td class="action-buttons">
            <!-- Edit Button -->
            <form action="edit_driver.jsp" method="get" style="display:inline;">
                <input type="hidden" name="username" value="<%= driver.getUsername() %>">
                <input type="submit" class="btn btn-warning btn-sm" value="Edit">
            </form>

            <!-- Delete Button -->
            <form action="DeleteDriverServlet" method="post" style="display:inline;">
                <input type="hidden" name="username" value="<%= driver.getUsername() %>">
                <input type="submit" class="btn btn-danger btn-sm" value="Delete">
            </form>
        </td>
    </tr>
    <% } %>
</table>

<!-- Bootstrap Modal for Adding a New Driver -->
<div class="modal fade" id="addDriverModal" tabindex="-1" aria-labelledby="addDriverModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addDriverModalLabel">Add a New Driver</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="AddDriverServlet" method="post" enctype="multipart/form-data">
                    <label>Username:</label> <input type="text" name="username" class="form-control" required><br>
                    <label>Password:</label> <input type="password" name="password" class="form-control" required><br>
                    <label>Name:</label> <input type="text" name="name" class="form-control" required><br>
                    <label>Phone:</label> <input type="text" name="phone" class="form-control" required><br>
                    <label>NIC:</label> <input type="text" name="nic" class="form-control" required><br>
                    <label>Address:</label> <input type="text" name="address" class="form-control" required><br>
                    <label>Experience (years):</label> <input type="number" name="experience" class="form-control" required><br>
                    <label>Status:</label>
                    <select name="status" class="form-control">
                        <option value="Available">Available</option>
                        <option value="Unavailable">Unavailable</option>
                    </select><br>
                    <label>Profile Picture:</label> <input type="file" name="profile_picture" class="form-control"><br>
                    <button type="submit" class="btn btn-success">Add Driver</button>
                </form>
            </div>
        </div>
    </div>
</div>

<br>
<a href="admin_dashboard.jsp" class="btn btn-secondary mt-3">Back to Dashboard</a>
</body>
</html>
