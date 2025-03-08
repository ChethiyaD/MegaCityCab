package com.megacitycab.controllers;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/AddDriverServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class AddDriverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password"); // Capture password
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String nic = request.getParameter("nic");
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            String role = "driver";

            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() ||
                    nic == null || nic.trim().isEmpty()) {
                response.sendRedirect("manage_drivers.jsp?error=Username, Password, and NIC cannot be empty");
                return;
            }

            String experienceStr = request.getParameter("experience");
            int experience = (experienceStr != null && !experienceStr.isEmpty()) ? Integer.parseInt(experienceStr) : 0;

            UserDAO userDAO = new UserDAO();

            // Check if username is already taken
            if (userDAO.isUsernameTaken(username)) {
                response.sendRedirect("manage_drivers.jsp?error=Username already exists");
                return;
            }

            // Handle profile picture upload
            Part filePart = request.getPart("profile_picture");
            String fileName = "default.png";

            if (filePart != null && filePart.getSize() > 0) {
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("") + "uploads";
                Files.createDirectories(Paths.get(uploadPath));
                Files.copy(filePart.getInputStream(), Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
            }

            User driver = new User(username, password, role, name, address, phone, nic, fileName, experience, status);

            if (userDAO.registerDriver(driver)) {
                response.sendRedirect("manage_drivers.jsp?success=Driver added successfully");
            } else {
                response.sendRedirect("manage_drivers.jsp?error=Failed to add driver");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage_drivers.jsp?error=Invalid input data");
        }
    }
}
