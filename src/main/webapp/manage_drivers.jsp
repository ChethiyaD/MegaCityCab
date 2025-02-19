<%@ page import="java.util.List, com.megacitycab.dao.UserDAO, com.megacitycab.models.User" %>
<%
    UserDAO userDAO = new UserDAO();
    List<User> drivers = userDAO.getAllDrivers();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Drivers</title>
</head>
<body>
<h1>Manage Drivers</h1>

<h2>Driver List</h2>
<table border="1">
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
        <td><%= driver.getStatus() %></td>
        <td><img src="uploads/<%= driver.getProfilePicture() %>" width="50"></td>
        <td>
            <form action="DeleteDriverServlet" method="post">
                <input type="hidden" name="username" value="<%= driver.getUsername() %>">
                <input type="submit" value="Delete">
            </form>
        </td>
    </tr>
    <% } %>
</table>

<h2>Add a New Driver</h2>
<form action="AddDriverServlet" method="post" enctype="multipart/form-data">
    <label>Username:</label> <input type="text" name="username" required><br>
    <label>Password:</label> <input type="password" name="password" required><br>
    <label>Name:</label> <input type="text" name="name" required><br>
    <label>Phone:</label> <input type="text" name="phone" required><br>
    <label>NIC:</label> <input type="text" name="nic" required><br>  <!-- ✅ Added NIC field -->
    <label>Address:</label> <input type="text" name="address" required><br> <!-- ✅ Added Address field -->
    <label>Experience (years):</label> <input type="number" name="experience" required><br>
    <label>Status:</label>
    <select name="status">
        <option value="Available">Available</option>
        <option value="Unavailable">Unavailable</option>
    </select><br>
    <label>Profile Picture:</label> <input type="file" name="profile_picture"><br>
    <input type="submit" value="Add Driver">
</form>


<br>
<a href="admin_dashboard.jsp">Back to Dashboard</a>
</body>
</html>
