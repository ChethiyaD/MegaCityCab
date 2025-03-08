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
    <!-- Add jQuery for search functionality -->
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
        function openEditModal(username, name, phone, nic, address, experience, status, profilePicture) {
            document.getElementById("editModalTitle").innerText = "Edit Driver";
            document.getElementById("editUsername").value = username;
            document.getElementById("editName").value = name;
            document.getElementById("editPhone").value = phone;
            document.getElementById("editNic").value = nic;
            document.getElementById("editAddress").value = address;
            document.getElementById("editExperience").value = experience;
            document.getElementById("editStatus").value = status || "Available";
            document.getElementById("currentProfilePicture").src = "uploads/" + profilePicture;
            document.getElementById("editForm").action = "EditDriverServlet";
            new bootstrap.Modal(document.getElementById("editDriverModal")).show();
        }

        // Add search function
        function searchDrivers() {
            let input = document.getElementById("searchInput").value.toLowerCase();
            let rows = document.querySelectorAll("#driversTable tbody tr");

            rows.forEach(row => {
                let username = row.cells[0].textContent.toLowerCase();
                let name = row.cells[1].textContent.toLowerCase();
                let phone = row.cells[2].textContent.toLowerCase();
                let nic = row.cells[3].textContent.toLowerCase();
                let address = row.cells[4].textContent.toLowerCase();
                let experience = row.cells[5].textContent.toLowerCase();
                let status = row.cells[6].textContent.toLowerCase();

                if (username.includes(input) || name.includes(input) || phone.includes(input) ||
                    nic.includes(input) || address.includes(input) || experience.includes(input) ||
                    status.includes(input)) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            });
        }
    </script>
</head>
<body>
<h2>Manage Drivers</h2>

<!-- Add Search Bar -->
<input type="text" id="searchInput" class="form-control mb-3"
       placeholder="Search by Username, Name, Phone, NIC, Address, Experience, or Status"
       onkeyup="searchDrivers()">

<!-- "Add Driver" Button (Opens Modal) -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editDriverModal"
        onclick="openEditModal('', '', '', '', '', '0', 'Available', 'default.png')">
    Add Driver
</button>

<!-- Add ID to table -->
<table border="1" class="mt-3" id="driversTable">
    <tr>
        <th>Username</th>
        <th>Name</th>
        <th>Phone</th>
        <th>NIC</th>
        <th>Address</th>
        <th>Experience</th>
        <th>Status</th>
        <th>Profile Picture</th>
        <th>Actions</th>
    </tr>
    <tbody>
    <% for (User driver : drivers) { %>
    <tr>
        <td><%= driver.getUsername() %></td>
        <td><%= driver.getName() %></td>
        <td><%= driver.getPhone() %></td>
        <td><%= driver.getNic() %></td>
        <td><%= driver.getAddress() != null ? driver.getAddress() : "Not Set" %></td>
        <td><%= driver.getExperience() %> years</td>
        <td><%= driver.getStatus() != null ? driver.getStatus() : "Available" %></td>
        <td>
            <img src="uploads/<%= driver.getProfilePicture() %>" alt="Profile Picture" width="50">
        </td>
        <td class="action-buttons">
            <!-- Edit Button -->
            <button class="btn btn-warning btn-sm" onclick="openEditModal(
                    '<%= driver.getUsername() %>',
                    '<%= driver.getName() %>',
                    '<%= driver.getPhone() %>',
                    '<%= driver.getNic() %>',
                    '<%= driver.getAddress() %>',
                    '<%= driver.getExperience() %>',
                    '<%= driver.getStatus() != null ? driver.getStatus() : "Available" %>',
                    '<%= driver.getProfilePicture() %>'
                    )">Edit</button>

            <!-- Delete Button -->
            <form action="DeleteDriverServlet" method="post" style="display:inline;">
                <input type="hidden" name="username" value="<%= driver.getUsername() %>">
                <input type="submit" class="btn btn-danger btn-sm" value="Delete">
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<!-- Bootstrap Modal for Adding/Editing a Driver -->
<div class="modal fade" id="editDriverModal" tabindex="-1" aria-labelledby="editModalTitle" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalTitle">Edit Driver</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="editForm" action="AddDriverServlet" method="post" enctype="multipart/form-data">
                    <label>Username:</label>
                    <input type="text" name="username" id="editUsername" class="form-control" required readonly><br>

                    <label>Name:</label>
                    <input type="text" name="name" id="editName" class="form-control" required><br>

                    <label>Phone:</label>
                    <input type="text" name="phone" id="editPhone" class="form-control" required><br>

                    <label>NIC:</label>
                    <input type="text" name="nic" id="editNic" class="form-control" required><br>

                    <label>Address:</label>
                    <input type="text" name="address" id="editAddress" class="form-control" required><br>

                    <label>Experience (years):</label>
                    <input type="number" name="experience" id="editExperience" class="form-control" required><br>

                    <label>Status:</label>
                    <select name="status" id="editStatus" class="form-control">
                        <option value="Available">Available</option>
                        <option value="Unavailable">Unavailable</option>
                    </select><br>

                    <label>Profile Picture:</label>
                    <input type="file" name="profile_picture" class="form-control"><br>

                    <!-- Show current picture -->
                    <img id="currentProfilePicture" src="uploads/default.png" width="100"><br>

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