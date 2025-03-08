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
    double taxAmount = booking.getEstimatedBill() * 0.05;  // Assuming 5% tax
    double distance = booking.getDistance(); // Assuming distance is part of booking object
    String paymentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
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

    <!-- Include Bootstrap for modal -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Include jsPDF library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
</head>
<body>

<!-- Modal for Payment Success -->
<div class="modal fade" id="paymentSuccessModal" tabindex="-1" aria-labelledby="paymentSuccessModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="paymentSuccessModalLabel">Payment Successful</h5>
            </div>
            <div class="modal-body">
                <p class="success-message">Thank you, <b><%= username %></b>. Your payment has been successfully processed.</p>

                <h3>Payment Details</h3>
                <table class="table">
                    <tr><td>Booking ID:</td><td><%= booking.getId() %></td></tr>
                    <tr><td>Pickup Location:</td><td><%= booking.getPickupLocation() %></td></tr>
                    <tr><td>Dropoff Location:</td><td><%= booking.getDropoffLocation() %></td></tr>
                    <tr><td>Driver:</td><td><%= booking.getDriverUsername() %></td></tr>
                    <tr><td>Total Paid:</td><td><b>LKR <%= String.format("%.2f", totalAmount) %></b></td></tr>
                    <tr><td>Tax (5%):</td><td>LKR <%= String.format("%.2f", taxAmount) %></td></tr>
                    <tr><td>Distance:</td><td><%= String.format("%.2f", distance) %> KM</td></tr>
                    <tr><td>Payment Time:</td><td><%= paymentTime %></td></tr>
                    <tr><td>Status:</td><td><b>Paid</b></td></tr>
                </table>

                <!-- Download Bill Button -->
                <button class="btn btn-success" id="downloadBillBtn">Download Bill</button>
                <br><br>

                <p>Redirecting to payment page in <span id="countdown">20</span> seconds...</p>
            </div>
            <div class="modal-footer">
                <a href="view_bookings.jsp" class="btn btn-primary">Back to My Bookings</a>
            </div>
        </div>
    </div>
</div>

<script>
    // Show the modal after the payment is successful
    var paymentSuccessModal = new bootstrap.Modal(document.getElementById('paymentSuccessModal'));
    paymentSuccessModal.show();

    // Countdown and redirect
    let countdown = 20;
    let countdownElement = document.getElementById("countdown");

    setInterval(() => {
        if (countdown > 1) {
            countdown--;
            countdownElement.textContent = countdown;
        } else {
            window.location.href = "payment.jsp"; // Redirect after 20 seconds
        }
    }, 1000);

    // Function to generate and download the PDF invoice
    document.getElementById("downloadBillBtn").addEventListener("click", function () {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        doc.setFont("helvetica", "bold");
        doc.text("MegaCityCab - Invoice", 105, 20, { align: "center" });

        // Add invoice details dynamically from JSP variables
        doc.setFont("helvetica", "normal");
        doc.text("Booking ID: <%= booking.getId() %>", 20, 40);
        doc.text("Customer: <%= booking.getCustomerUsername() %>", 20, 50);
        doc.text("Driver: <%= booking.getDriverUsername() %>", 20, 60);
        doc.text("Pickup Location: <%= booking.getPickupLocation() %>", 20, 70);
        doc.text("Dropoff Location: <%= booking.getDropoffLocation() %>", 20, 80);
        doc.text("Total Paid: LKR <%= String.format("%.2f", totalAmount) %>", 20, 90);
        doc.text("Tax: LKR <%= String.format("%.2f", taxAmount) %>", 20, 100);
        doc.text("Distance: <%= String.format("%.2f", distance) %> KM", 20, 110);
        doc.text("Payment Time: <%= paymentTime %>", 20, 120);
        doc.text("Status: Paid", 20, 130);

        // Save the PDF
        doc.save("Invoice_<%= booking.getId() %>.pdf");
    });
</script>

</body>
</html>
