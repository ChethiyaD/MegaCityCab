package com.megacitycab.controllers;

import com.megacitycab.dao.CarDAO;
import com.megacitycab.models.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/EditCarServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class EditCarServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve car details from the form
            int carId = Integer.parseInt(request.getParameter("car_id")); // Ensure this is in the form
            String carNumber = request.getParameter("car_number");
            String carName = request.getParameter("car_name");
            String carModel = request.getParameter("car_model");
            String carType = request.getParameter("car_type");
            boolean availability = "1".equals(request.getParameter("availability"));

            // Handle file upload
            Part filePart = request.getPart("image");
            String fileName = (filePart != null && filePart.getSize() > 0)
                    ? Paths.get(filePart.getSubmittedFileName()).getFileName().toString()
                    : request.getParameter("existing_image"); // Keep old image if no new upload

            // Save new image if uploaded
            if (filePart != null && filePart.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("") + "uploads";
                Files.createDirectories(Paths.get(uploadPath));
                Files.copy(filePart.getInputStream(), Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
            }

            // Update car in database
            CarDAO carDAO = new CarDAO();
            boolean success = carDAO.updateCar(new Car(carId, carName, carModel, carNumber, carType, availability, fileName));

            if (success) {
                response.sendRedirect("manage_cars.jsp?success=Car updated successfully");
            } else {
                response.sendRedirect("manage_cars.jsp?error=Failed to update car");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage_cars.jsp?error=Invalid input data");
        }
    }
}
