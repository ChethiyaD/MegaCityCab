<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.megacitycab.models.User" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    User user = (sessionObj != null) ? (User) sessionObj.getAttribute("user") : null;

    if (user == null || user.getRole() == null || !"customer".equals(user.getRole())) {
        response.sendRedirect("index.jsp?error=Unauthorized access");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Dashboard</title>
</head>
<body>
<h2>Welcome, <%= user.getName() %>!</h2>
<p>You are logged in as: <strong><%= user.getUsername() %></strong></p>

<!-- Display Profile Picture -->
<img src="uploads/<%= user.getProfilePicture() %>" alt="Profile Picture" width="150">

<ul>
    <li><a href="book_cab.jsp">Book a Cab</a></li>
    <li><a href="view_bookings.jsp">View My Bookings</a></li>
    <!-- Add a Manage Profile option -->
    <li><a href="manage_profile.jsp">Manage Profile</a></li>
    <a href="LogoutServlet" class="btn btn-danger">Logout</a>
</ul>
</body>
</html>
