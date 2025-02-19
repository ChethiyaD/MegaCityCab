<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
<form action="login" method="post">
    <label>Username:</label>
    <input type="text" name="username" required><br>

    <label>Password:</label>
    <input type="password" name="password" required><br>

    <button type="submit">Login</button>
</form>

<%-- Show login errors if any --%>
<%
    String error = request.getParameter("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
%>
</body>
</html>
