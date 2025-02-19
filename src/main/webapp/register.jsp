<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>
<h2>Registration</h2>

<%-- Show error messages if any --%>
<%
    String error = request.getParameter("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
%>

<form action="register" method="post" enctype="multipart/form-data">
    <label>Username:</label>
    <input type="text" name="username" required><br>

    <label>Password:</label>
    <input type="password" name="password" required><br>

    <label>Role:</label>
    <select name="role" required>
        <option value="customer">Customer</option>
        <option value="driver">Driver</option>
    </select><br>

    <label>Name:</label>
    <input type="text" name="name" required><br>

    <label>Address:</label>
    <input type="text" name="address" required><br>

    <label>Phone:</label>
    <input type="text" name="phone" required><br>

    <label>NIC:</label>
    <input type="text" name="nic" required><br>

    <label>Profile Picture:</label>
    <input type="file" name="profile_picture" accept="image/*"><br>

    <button type="submit">Register</button>
</form>
</body>
</html>
