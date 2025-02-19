package com.megacitycab.controllers;

import com.megacitycab.dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteCarServlet")
public class DeleteCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String carNumber = request.getParameter("car_number");
        CarDAO carDAO = new CarDAO();

        if (carDAO.deleteCar(carNumber)) {
            response.sendRedirect("manage_cars.jsp?success=Car deleted successfully");
        } else {
            response.sendRedirect("admin_dashboard.jsp?error=Failed to delete car");
        }
    }
}
