package com.megacitycab.controllers;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class AuthController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            // Store the full User object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("profilePicture", user.getProfilePicture());

            System.out.println("DEBUG: Authenticated User: " + user.getUsername() + ", Role: " + user.getRole());

            // Redirect based on role
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("admin_dashboard.jsp");
            } else if ("driver".equals(user.getRole())) {
                response.sendRedirect("driver_dashboard.jsp");
            } else if ("customer".equals(user.getRole())) {
                response.sendRedirect("customer_dashboard.jsp");
            } else {
                response.sendRedirect("index.jsp?error=Unauthorized");
            }
        } else {
            System.out.println("DEBUG: Invalid credentials for username: " + username);
            response.sendRedirect("index.jsp?error=Invalid credentials");
        }
    }
}
