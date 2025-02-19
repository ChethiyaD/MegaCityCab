package com.megacitycab.controllers;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AdminDriverServlet")
public class AdminDriverServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String nic = request.getParameter("nic");
            int experience = Integer.parseInt(request.getParameter("experience"));

            User driver = new User(username, password, "driver", name, address, phone, nic, "default.png", experience, "available");
            userDAO.registerDriver(driver);
        } else if ("delete".equals(action)) {
            String username = request.getParameter("username");
            userDAO.deleteDriver(username);
        }

        response.sendRedirect("manage_drivers.jsp");
    }
}
