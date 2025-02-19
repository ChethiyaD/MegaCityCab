package com.megacitycab.controllers;

import com.megacitycab.dao.UserDAO;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DeleteDriverServlet")
public class DeleteDriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        UserDAO userDAO = new UserDAO();

        if (userDAO.deleteDriver(username)) {
            response.sendRedirect("manage_drivers.jsp?success=Driver deleted successfully");
        } else {
            response.sendRedirect("admin_dashboard.jsp?error=Failed to delete driver");
        }
    }
}
