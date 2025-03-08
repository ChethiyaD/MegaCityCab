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

@WebServlet("/EditDriverServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class EditDriverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Get password
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String nic = request.getParameter("nic");
        String address = request.getParameter("address");
        String experienceStr = request.getParameter("experience");
        String status = request.getParameter("status");

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("manage_drivers.jsp?error=Invalid username");
            return;
        }

        int experience = (experienceStr != null && !experienceStr.isEmpty()) ? Integer.parseInt(experienceStr) : 0;

        Part filePart = request.getPart("profile_picture");
        String fileName = null;
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + "uploads";
            Files.createDirectories(Paths.get(uploadPath));
            Files.copy(filePart.getInputStream(), Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
        }

        UserDAO userDAO = new UserDAO();
        User existingDriver = userDAO.getDriverByUsername(username);
        if (existingDriver == null) {
            response.sendRedirect("manage_drivers.jsp?error=Driver not found");
            return;
        }

        if (fileName == null) {
            fileName = existingDriver.getProfilePicture();
        }

        if (password == null || password.trim().isEmpty()) {
            password = existingDriver.getPassword(); // Keep old password if not changed
        }

        User driver = new User(username, password, "driver", name, address, phone, nic, fileName, experience, status);

        if (userDAO.updateDriver(driver)) {
            response.sendRedirect("manage_drivers.jsp?success=Driver updated successfully");
        } else {
            response.sendRedirect("manage_drivers.jsp?error=Failed to update driver");
        }
    }
}
