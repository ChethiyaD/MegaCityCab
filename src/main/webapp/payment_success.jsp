<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.megacitycab.dao.BookingDAO, com.megacitycab.models.Booking" %>

<%
    HttpSession sessionObj = request.getSession(false);
    String username = (sessionObj != null) ? (String) sessionObj.getAttribute("username") : null;

    if (username == null) {
        response.sendRedirect("index.jsp?error=Unauthorized access");
        return;
    }

    String bookingIdParam = request.getParameter("booking_id");
    if (bookingIdParam == null) {
        response.sendRedirect("view_bookings.jsp?error=Invalid booking ID");
        return;
    }

    int bookingId = Integer.parseInt(bookingIdParam);
    BookingDAO bookingDAO = new BookingDAO();
    Booking booking = bookingDAO.getBookingById(bookingId);

    if (booking == null) {
        response.sendRedirect("view_bookings.jsp?error=Booking not found");
        return;
    }

    double totalAmount = Double.parseDouble(request.getParameter("total_amount"));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Payment Successful</title>
    <style>
        .success-message {
            color: green;
            font-size: 18px;
            font-weight: bold;
        }
    </style>

    <!-- Include jsPDF library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
</head>
<body>
<h2 class="success-message">Payment Successful!</h2>
<p>Thank you, <b><%= username %></b>. Your payment has been successfully processed.</p>

<h3>Payment Details</h3>
<table border="1">
    <tr><td>Booking ID:</td><td><%= booking.getId() %></td></tr>
    <tr><td>Pickup Location:</td><td><%= booking.getPickupLocation() %></td></tr>
    <tr><td>Dropoff Location:</td><td><%= booking.getDropoffLocation() %></td></tr>
    <tr><td>Driver:</td><td><%= booking.getDriverUsername() %></td></tr>
    <tr><td>Total Paid:</td><td><b>LKR <%= String.format("%.2f", totalAmount) %></b></td></tr>
    <tr><td>Status:</td><td><b>Paid</b></td></tr>
</table>

<!-- Download Bill Button -->
<button onclick="generatePDF()">Download Bill</button>

<br>
<a href="view_bookings.jsp">Back to My Bookings</a>

<!-- JavaScript to generate PDF -->
<script>
    function generatePDF() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        doc.setFont("helvetica", "bold");
        doc.text("MegaCityCab - Invoice", 105, 20, { align: "center" });

        // Add invoice details
        doc.setFont("helvetica", "normal");
        doc.text("Booking ID: <%= booking.getId() %>", 20, 40);
        doc.text("Customer: <%= booking.getCustomerUsername() %>", 20, 50);
        doc.text("Driver: <%= booking.getDriverUsername() %>", 20, 60);
        doc.text("Pickup Location: <%= booking.getPickupLocation() %>", 20, 70);
        doc.text("Dropoff Location: <%= booking.getDropoffLocation() %>", 20, 80);
        doc.text("Total Paid: LKR <%= String.format("%.2f", totalAmount) %>", 20, 90);
        doc.text("Status: Paid", 20, 100);

        // Save the PDF
        doc.save("Invoice_<%= booking.getId() %>.pdf");
    }
</script>
</body>
</html>
