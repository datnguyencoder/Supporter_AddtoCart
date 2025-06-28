package com.tourismapp.controller.paymentController;

import com.tourismapp.common.PaymentMethod;
import com.tourismapp.common.Status;
import com.tourismapp.model.Payment;
import com.tourismapp.model.Orders;
import com.tourismapp.service.order.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@WebServlet(name = "VNPayServlet", urlPatterns = {"/payment/vnpay", "/payment/vnpay/callback"})
public class VNPayServlet extends HttpServlet {

    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String TMN_CODE = "58X4B4HP";
    private static final String SECRET_KEY = "VRLDWNVWDNPCOEPBZUTWSEDQAGXJCNGZ";
    private static final String RETURN_URL = "http://localhost:3000/api/v1/payment/vn-pay-callback";
    private static final String VERSION = "2.1.0";
    private static final String COMMAND = "pay";
    private static final String ORDER_TYPE = "other";
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/payment/vnpay/callback".equals(path)) {
            handleVNPayCallback(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/payment/vnpay".equals(path)) {
            initiateVNPayPayment(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
        }
    }

    private void initiateVNPayPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String orderId = request.getParameter("orderId");
            String amountStr = request.getParameter("amount");

            if (orderId == null || amountStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId or amount");
                return;
            }

            int orderIdInt = Integer.parseInt(orderId);
            BigDecimal amount = new BigDecimal(amountStr).multiply(new BigDecimal(100)); // VNPay requires amount in VND, multiplied by 100
            String vnp_TxnRef = orderId; // Use orderId as transaction reference
            String vnp_IpAddr = getClientIpAddress(request);
            String vnp_CreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String vnp_ExpireDate = LocalDateTime.now().plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            SortedMap<String, String> vnp_Params = new TreeMap<>();
            vnp_Params.put("vnp_Version", VERSION);
            vnp_Params.put("vnp_Command", COMMAND);
            vnp_Params.put("vnp_TmnCode", TMN_CODE);
            vnp_Params.put("vnp_Amount", amount.toString());
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Payment for order " + orderId);
            vnp_Params.put("vnp_OrderType", ORDER_TYPE);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", RETURN_URL);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            String vnp_SecureHash = createSecureHash(vnp_Params);
            vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                           .append("=")
                           .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            }

            String paymentUrl = VNPAY_URL + "?" + queryString.toString();

            Payment payment = new Payment();
            payment.setOrder(new Orders(orderIdInt));
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(PaymentMethod.VNPAY);
            payment.setAmount(new BigDecimal(amountStr)); // Store original amount
            payment.setStatus(Status.PENDING);
            savePaymentToDatabase(payment);

            response.sendRedirect(paymentUrl);
        } catch (IOException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Payment initiation failed: " + e.getMessage());
        }
    }

    private void handleVNPayCallback(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            SortedMap<String, String> vnp_Params = new TreeMap<>();
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                vnp_Params.put(entry.getKey(), entry.getValue()[0]);
            }

            String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
            String calculatedHash = createSecureHash(vnp_Params);

            String vnp_ResponseCode = vnp_Params.get("vnp_ResponseCode");
            String orderId = vnp_Params.get("vnp_TxnRef");

            if (!calculatedHash.equals(vnp_SecureHash)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid secure hash");
                return;
            }

            Payment payment = getPaymentByOrderId(orderId);
            if (payment == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment not found for orderId: " + orderId);
                return;
            }

            if ("00".equals(vnp_ResponseCode)) {
                payment.setStatus(Status.COMPLETED);
                orderService.addOrderTracking(Integer.parseInt(orderId), Status.COMPLETED, "Payment successful via VNPay");
            } else {
                payment.setStatus(Status.FAILED);
                orderService.addOrderTracking(Integer.parseInt(orderId), Status.FAILED, "Payment failed with response code: " + vnp_ResponseCode);
            }
            updatePaymentInDatabase(payment);

            response.sendRedirect("/orders");
        } catch (IOException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Callback processing failed: " + e.getMessage());
        }
    }

    private String createSecureHash(SortedMap<String, String> params) {
        try {
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    data.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()))
                        .append("&");
                }
            }
            if (data.length() > 0) {
                data.deleteCharAt(data.length() - 1); // Remove last &
            }

            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hashBytes = mac.doFinal(data.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException | IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create secure hash", e);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
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