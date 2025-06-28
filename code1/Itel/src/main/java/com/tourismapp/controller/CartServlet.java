package com.tourismapp.controller;

import com.tourismapp.common.Status;
import com.tourismapp.config.ProjectPaths;
import com.tourismapp.dao.DBConnection;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.Orders;
import com.tourismapp.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.json.JSONObject;

@WebServlet(name = "CartServlet", urlPatterns = {"/Itel/cart", "/Itel/cart/add", "/Itel/cart/update", "/Itel/cart/remove", "/Itel/orders", "/Itel/cart/createOrder", "/Itel/cart/count"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println(">>> [DEBUG] doGet servlet path: " + path);
        
        if ("/Itel/cart".equals(path)) {
            request.getRequestDispatcher(ProjectPaths.JSP_CARTPAGE_PATH).forward(request, response);
        } else if ("/Itel/orders".equals(path)) {
            request.getRequestDispatcher(ProjectPaths.JSP_ORDERPAGE_PATH).forward(request, response);
        } else if ("/Itel/cart/count".equals(path)) {
            try {
                handleGetCartCount(request, response, new JSONObject());
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String path = request.getServletPath();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    System.out.println(">>> [DEBUG] doPost servlet path: " + path);
    JSONObject jsonResponse = new JSONObject();

    try {
        switch (path) {
            case "/Itel/cart/add" -> handleAddToCart(request, response, jsonResponse);
            case "/Itel/cart/update" -> handleUpdateCart(request, response, jsonResponse);
            case "/Itel/cart/remove" -> handleRemoveFromCart(request, response, jsonResponse);
            case "/Itel/cart/createOrder" -> handleCreateOrder(request, response, jsonResponse);
            case "/Itel/cart/count" -> handleGetCartCount(request, response, jsonResponse);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
        }
    } catch (Exception e) {
        e.printStackTrace();
        jsonResponse.put("success", false);
        jsonResponse.put("message", "Server error: " + e.getMessage());
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
}
    
private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, JSONObject jsonResponse)
            throws SQLException, IOException {
        String productIdStr = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");

        // Debug thông tin chi tiết
        System.out.println(">>> [DEBUG] Request URL: " + request.getRequestURL());
        System.out.println(">>> [DEBUG] Content Type: " + request.getContentType());
        System.out.println(">>> [DEBUG] Character Encoding: " + request.getCharacterEncoding());

        System.out.println(">>> [DEBUG] All Parameters:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(">>> [DEBUG] " + key + " = " + String.join(", ", value));
        });

        System.out.println(">>> [DEBUG] productIdStr = " + productIdStr + ", quantityStr = " + quantityStr);

        if (productIdStr == null || productIdStr.isBlank() || quantityStr == null || quantityStr.isBlank()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Thiếu productId hoặc quantity");
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
                out.flush();
            }
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            Integer userId = (Integer) request.getSession().getAttribute("userId");

            System.out.println(">>> [DEBUG] userId = " + userId);

            if (userId == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Vui lòng đăng nhập trước khi thêm sản phẩm vào giỏ hàng");
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                    out.flush();
                }
                return;
            }

            Product product = getProductById(productId);
            System.out.println(">>> [DEBUG] product = " + product);

            if (product == null || !product.getStatus().equals(Status.ACTIVE)) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Sản phẩm không tồn tại hoặc đã bị khóa");
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                    out.flush();
                }
                return;
            }

            if (product.getStockQuantity() < quantity) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Sản phẩm không đủ hàng tồn kho");
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                    out.flush();
                }
                return;
            }

            Orders cartOrder = getCartOrder(userId);
            if (cartOrder == null) {
                cartOrder = new Orders();
                cartOrder.setUserId(userId);
                cartOrder.setOrderDate(LocalDateTime.now());
                cartOrder.setStatus(Status.PENDING);
                cartOrder.setTotalAmount(BigDecimal.ZERO);
                int orderId = createOrder(cartOrder);
                cartOrder.setOrderId(orderId);
            }

            OrderDetail existingDetail = getOrderDetail(cartOrder.getOrderId(), productId);
            if (existingDetail != null) {
                existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                updateOrderDetail(existingDetail);
            } else {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(cartOrder);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setUnitPrice(product.getPrice());
                addOrderDetail(orderDetail);
            }

            updateOrderTotal(cartOrder.getOrderId());

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Đã thêm sản phẩm vào giỏ hàng");
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
                out.flush();
            }
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Định dạng productId hoặc quantity không hợp lệ");
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
                out.flush();
            }
            return;
        }
    }

    

    private Product getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM Product WHERE product_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
//            stmt.setString(2, Status.ACTIVE.name());
            stmt.setString(2, Status.ACTIVE.getValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setStatus(Status.valueOf(rs.getString("status")));
                    product.setStockQuantity(rs.getInt("stock"));
                    return product;
                }
            }
        }
        return null;
    }
    
    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response, JSONObject jsonResponse) throws SQLException, IOException {
        String orderDetailIdStr = request.getParameter("orderDetailId");
        String quantityStr = request.getParameter("quantity");

        if (orderDetailIdStr == null || quantityStr == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Missing orderDetailId or quantity");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        int orderDetailId = Integer.parseInt(orderDetailIdStr);
        int quantity = Integer.parseInt(quantityStr);
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Please login to update cart");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        if (quantity <= 0) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Quantity must be greater than 0");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        // Kiểm tra xem orderDetail có thuộc về user không
        if (!isOrderDetailBelongsToUser(orderDetailId, userId)) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Unauthorized access");
           try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        updateOrderDetailQuantity(orderDetailId, quantity);

        int orderId = getOrderIdByOrderDetailId(orderDetailId);
        updateOrderTotal(orderId);

        jsonResponse.put("success", true);
        jsonResponse.put("message", "Cart updated successfully");
        try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
}

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response, JSONObject jsonResponse) throws SQLException, IOException {
        String orderDetailIdStr = request.getParameter("orderDetailId");

        if (orderDetailIdStr == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Missing orderDetailId");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        int orderDetailId = Integer.parseInt(orderDetailIdStr);
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Please login to remove from cart");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        if (!isOrderDetailBelongsToUser(orderDetailId, userId)) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Unauthorized access");
           try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        int orderId = getOrderIdByOrderDetailId(orderDetailId);

        removeOrderDetail(orderDetailId);

        updateOrderTotal(orderId);

        jsonResponse.put("success", true);
        jsonResponse.put("message", "Item removed from cart successfully");
        try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
    }

    private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response, JSONObject jsonResponse) throws SQLException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Please login to create order");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        Orders cartOrder = getCartOrder(userId);
        if (cartOrder == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Cart is empty");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        if (!hasCartItems(cartOrder.getOrderId())) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Cart is empty");
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        updateOrderStatus(cartOrder.getOrderId(), Status.PROCESSING);

        jsonResponse.put("success", true);
        jsonResponse.put("orderId", cartOrder.getOrderId());
        jsonResponse.put("message", "Order created successfully");
        try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
    }

    private void handleGetCartCount(HttpServletRequest request, HttpServletResponse response, JSONObject jsonResponse) throws SQLException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            jsonResponse.put("success", true);
            jsonResponse.put("count", 0);
            try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
            return;
        }

        int count = getCartItemCount(userId);
        jsonResponse.put("success", true);
        jsonResponse.put("count", count);
        try (PrintWriter out = response.getWriter()) {
    out.print(jsonResponse.toString());
    out.flush();
}
    }


    private boolean isOrderDetailBelongsToUser(int orderDetailId, int userId) throws SQLException {
        String sql = "SELECT od.order_detail_id FROM Order_Detail od " +
                     "JOIN Orders o ON od.order_id = o.order_id " +
                     "WHERE od.order_detail_id = ? AND o.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void updateOrderDetailQuantity(int orderDetailId, int quantity) throws SQLException {
        String sql = "UPDATE Order_Detail SET quantity = ? WHERE order_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, orderDetailId);
            stmt.executeUpdate();
        }
    }

    private int getOrderIdByOrderDetailId(int orderDetailId) throws SQLException {
        String sql = "SELECT order_id FROM Order_Detail WHERE order_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("order_id");
                }
            }
        }
        throw new SQLException("Order detail not found");
    }

    private void removeOrderDetail(int orderDetailId) throws SQLException {
        String sql = "DELETE FROM Order_Detail WHERE order_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            stmt.executeUpdate();
        }
    }

    private boolean hasCartItems(int orderId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Order_Detail WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    private void updateOrderStatus(int orderId, Status status) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    private int getCartItemCount(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(od.quantity), 0) as count " +
                     "FROM Orders o JOIN Order_Detail od ON o.order_id = od.order_id " +
                     "WHERE o.user_id = ? AND o.status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, Status.PENDING.value());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }

    private Orders getCartOrder(int userId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE user_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, Status.PENDING.value());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Orders order = new Orders();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                    order.setStatus(Status.valueOf(rs.getString("status")));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    return order;
                }
            }
        }
        return null;
    }

    private int createOrder(Orders order) throws SQLException {
        String sql = "INSERT INTO Orders (user_id, order_date, status, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setString(3, order.getStatus().name());
            stmt.setBigDecimal(4, order.getTotalAmount());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create order");
    }

    private OrderDetail getOrderDetail(int orderId, int productId) throws SQLException {
        String sql = "SELECT * FROM Order_Detail WHERE order_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderDetailId(rs.getInt("order_detail_id"));
                    Orders order = new Orders();
                    order.setOrderId(rs.getInt("order_id"));
                    detail.setOrder(order);
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    detail.setProduct(product);
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                    return detail;
                }
            }
        }
        return null;
    }

    private void addOrderDetail(OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO Order_Detail (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getOrder().getOrderId()); // Extract order_id from Orders object
            stmt.setInt(2, detail.getProduct().getProductId()); // Extract product_id from Product object
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getUnitPrice());
            stmt.executeUpdate();
        }
    }

    private void updateOrderDetail(OrderDetail detail) throws SQLException {
        String sql = "UPDATE Order_Detail SET quantity = ? WHERE order_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getQuantity());
            stmt.setInt(2, detail.getOrderDetailId());
            stmt.executeUpdate();
        }
    }

    private void updateOrderTotal(int orderId) throws SQLException {
        String sql = "UPDATE Orders SET total_amount = (SELECT SUM(quantity * unit_price) FROM Order_Detail WHERE order_id = ?) WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }
}