package com.megacitycab.controllers;

import com.megacitycab.dao.CarDAO;
import com.megacitycab.models.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/AddCarServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String carName = request.getParameter("car_name");
        String carModel = request.getParameter("car_model");
        String carNumber = request.getParameter("car_number");
        String carType = request.getParameter("car_type");

        // Handle Image Upload
        Part filePart = request.getPart("image");
        String imageFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDirPath = new File(uploadDir);
        if (!uploadDirPath.exists()) {
            uploadDirPath.mkdirs();
        }
        String filePath = uploadDir + File.separator + imageFileName;
        filePart.write(filePath);

        // Save Car Data
        CarDAO carDAO = new CarDAO();
        Car car = new Car(0, carName, carModel, carNumber, carType, true, imageFileName);

        if (carDAO.addCar(car)) {
            response.sendRedirect("manage_cars.jsp?success=Car added successfully");
        } else {
            response.sendRedirect("manage_cars.jsp?error=Failed to add car");
        }
    }
}
