<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.dao.UserDAO, com.megacitycab.models.User" %>

<%
    if (session.getAttribute("role") == null || !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.jsp");
        return;
    }

    String username = request.getParameter("username");
    if (username == null || username.trim().isEmpty()) {
        response.sendRedirect("manage_drivers.jsp?error=Invalid driver selection");
        return;
    }

    UserDAO userDAO = new UserDAO();
    User driver = userDAO.getDriverByUsername(username);

    if (driver == null) {
        response.sendRedirect("manage_drivers.jsp?error=Driver not found");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Driver</title>
</head>
<body>
<h2>Edit Driver - <%= driver.getUsername() %></h2>

<form action="EditDriverServlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="username" value="<%= driver.getUsername() %>">

    <label>Name:</label>
    <input type="text" name="name" value="<%= driver.getName() != null ? driver.getName() : "" %>" required><br>

    <label>Phone:</label>
    <input type="text" name="phone" value="<%= driver.getPhone() != null ? driver.getPhone() : "" %>" required><br>

    <label>NIC:</label>
    <input type="text" name="nic" value="<%= driver.getNic() != null ? driver.getNic() : "" %>" required><br>

    <label>Address:</label>
    <input type="text" name="address" value="<%= driver.getAddress() != null ? driver.getAddress() : "" %>" required><br>

    <label>Experience (years):</label>
    <input type="number" name="experience" value="<%= driver.getExperience() %>" required><br>

    <label>Status:</label>
    <select name="status">
        <option value="Available" <%= "Available".equals(driver.getStatus()) ? "selected" : "" %>>Available</option>
        <option value="Unavailable" <%= "Unavailable".equals(driver.getStatus()) ? "selected" : "" %>>Unavailable</option>
    </select><br>

    <label>Profile Picture:</label>
    <input type="file" name="profile_picture"><br>

    <!-- Show current picture -->
    <img src="uploads/<%= driver.getProfilePicture() %>" width="100"><br>

    <input type="submit" value="Update Driver">
</form>

<br>
<a href="manage_drivers.jsp">Back to Manage Drivers</a>
</body>
</html>
