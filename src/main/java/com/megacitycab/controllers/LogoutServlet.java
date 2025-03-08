package com.megacitycab.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate session
        request.getSession().invalidate();

        // Remove the "username" cookie
        Cookie usernameCookie = new Cookie("username", null);
        usernameCookie.setMaxAge(0);
        response.addCookie(usernameCookie);

        // Redirect to login page
        response.sendRedirect("index.jsp");
    }
}
