package com.tourismapp.controller.paymentController;

import com.tourismapp.common.PaymentMethod;
import com.tourismapp.model.Payment;
import com.tourismapp.model.Orders;
import com.tourismapp.common.Status;
import com.tourismapp.service.order.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/payment/momo", "/payment/momo/callback"})
public class MomoServlet extends HttpServlet {

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String MOMO_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    private static final String RETURN_URL = "http://localhost:3000/api/v1/payment/momo/callback";
    private static final String IPN_URL = "http://localhost:3000/api/v1/payment/momo/ipn-handler";
    private static final String REQUEST_TYPE = "captureWallet";
    private DataSource dataSource;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/TourismDB");
            orderService = new OrderService();
        } catch (NamingException e) {
            throw new ServletException("Cannot initialize DataSource", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/payment/momo".equals(path)) {
            initiateMoMoPayment(request, response);
        } else if ("/payment/momo/callback".equals(path)) {
            handleMoMoCallback(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
        }
    }

    private void initiateMoMoPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String orderId = request.getParameter("orderId");
            String amountStr = request.getParameter("amount");

            if (orderId == null || amountStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId or amount");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr);
            String requestId = UUID.randomUUID().toString();
            String orderInfo = "Payment for order " + orderId;
            String extraData = "";

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("partnerCode", PARTNER_CODE);
            jsonRequest.put("accessKey", ACCESS_KEY);
            jsonRequest.put("requestId", requestId);
            jsonRequest.put("amount", amount.toString());
            jsonRequest.put("orderId", orderId);
            jsonRequest.put("orderInfo", orderInfo);
            jsonRequest.put("redirectUrl", RETURN_URL);
            jsonRequest.put("ipnUrl", IPN_URL);
            jsonRequest.put("extraData", extraData);
            jsonRequest.put("requestType", REQUEST_TYPE);
            jsonRequest.put("lang", "en");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(MOMO_ENDPOINT))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString(), java.nio.charset.StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject responseJson = new JSONObject(httpResponse.body());

            if (httpResponse.statusCode() == 200 && responseJson.getInt("resultCode") == 0) {
                Payment payment = new Payment();
                payment.setOrder(new Orders(Integer.parseInt(orderId)));
                payment.setPaymentDate(LocalDateTime.now());
                payment.setPaymentMethod(PaymentMethod.MOMO);
                payment.setAmount(amount);
                payment.setStatus(Status.PENDING);
                savePaymentToDatabase(payment);

                String payUrl = responseJson.getString("payUrl");
                response.sendRedirect(payUrl);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, responseJson.getString("message"));
            }
        } catch (IOException | InterruptedException | NumberFormatException | JSONException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Payment initiation failed: " + e.getMessage());
        }
    }

    private void handleMoMoCallback(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            StringBuilder jsonBody = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }
            JSONObject callbackData = new JSONObject(jsonBody.toString());

            int resultCode = callbackData.getInt("resultCode");
            String orderId = callbackData.getString("orderId");
            Payment payment = getPaymentByOrderId(orderId);

            if (payment != null) {
                if (resultCode == 0) {
                    payment.setStatus(Status.COMPLETED);
                    orderService.addOrderTracking(Integer.parseInt(orderId), Status.COMPLETED, "Payment successful via MoMo");
                } else {
                    payment.setStatus(Status.FAILED);
                    orderService.addOrderTracking(Integer.parseInt(orderId), Status.FAILED, "Payment failed: " + callbackData.getString("message"));
                }
                updatePaymentInDatabase(payment);
            }

            response.setContentType("application/json");
            response.getWriter().write(new JSONObject().put("status", "success").toString());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Callback processing failed: " + e.getMessage());
        }
    }

    private void savePaymentToDatabase(Payment payment) {
        String sql = "INSERT INTO Payment (order_id, payment_date, payment_method, amount, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getOrder().getOrderId());
            stmt.setTimestamp(2, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setBigDecimal(4, payment.getAmount());
            stmt.setString(5, payment.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setPaymentId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save payment to database", e);
        }
    }

    private Payment getPaymentByOrderId(String orderId) {
        String sql = "SELECT * FROM Payment WHERE order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(orderId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setOrder(new Orders(rs.getInt("order_id")));
                    payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                    payment.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setStatus(Status.valueOf(rs.getString("status")));
                    return payment;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve payment by orderId", e);
        }
        return null;
    }

    private void updatePaymentInDatabase(Payment payment) {
        String sql = "UPDATE Payment SET status = ? WHERE payment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, payment.getStatus().name());
            stmt.setInt(2, payment.getPaymentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update payment in database", e);
        }
    }
}