<%@ page import="com.megacitycab.dao.CarDAO, com.megacitycab.models.Car" %>
<%
    if (session.getAttribute("role") == null || !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.jsp");
        return;
    }

    String carIdParam = request.getParameter("car_id");
    if (carIdParam == null) {
        response.sendRedirect("manage_cars.jsp?error=Invalid car ID");
        return;
    }

    int carId = Integer.parseInt(carIdParam);
    CarDAO carDAO = new CarDAO();
    Car car = carDAO.getCarById(carId);

    if (car == null) {
        response.sendRedirect("manage_cars.jsp?error=Car not found");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Car</title>
</head>
<body>
<h2>Edit Car</h2>

<form action="EditCarServlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="car_id" value="<%= car.getId() %>">

    <label>Car Name:</label>
    <input type="text" name="car_name" value="<%= car.getCarName() %>" required><br>

    <label>Model:</label>
    <input type="text" name="car_model" value="<%= car.getCarModel() %>" required><br>

    <label>Car Number:</label>
    <input type="text" name="car_number" value="<%= car.getCarNumber() %>" required readonly><br>

    <label>Type:</label>
    <select name="car_type">
        <option value="Sedan" <%= car.getCarType().equals("Sedan") ? "selected" : "" %>>Sedan</option>
        <option value="SUV" <%= car.getCarType().equals("SUV") ? "selected" : "" %>>SUV</option>
        <option value="Hatchback" <%= car.getCarType().equals("Hatchback") ? "selected" : "" %>>Hatchback</option>
    </select><br>

    <label>Fare Per KM (LKR):</label>
    <input type="number" step="0.01" name="fare_per_km" class="form-control"
           value="<%= car.getFarePerKm() %>" required><br>

    <label>Availability:</label>
    <select name="availability">
        <option value="1" <%= car.isAvailable() ? "selected" : "" %>>Available</option>
        <option value="0" <%= !car.isAvailable() ? "selected" : "" %>>Not Available</option>
    </select><br>

    <label>Current Image:</label>
    <img src="uploads/<%= car.getImage() %>" alt="Car Image" width="100"><br>

    <label>New Image:</label>
    <input type="file" name="image"><br>

    <input type="hidden" name="existing_image" value="<%= car.getImage() %>">

    <input type="submit" value="Update Car">
</form>

<a href="manage_cars.jsp">Back to Car Management</a>
</body>
</html>
