package com.tourismapp.dao.order;

import com.tourismapp.common.Status;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.OrderTracking;
import com.tourismapp.model.Orders;
import com.tourismapp.model.Product;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements IOrderDAO {
    private final DataSource dataSource;

    public OrderDAO() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/TourismDB");
        } catch (NamingException e) {
            throw new RuntimeException("Cannot initialize DataSource", e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public int createCartOrder(int userId) {
        String sql = "INSERT INTO Orders (user_id, order_date, status, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, Status.PENDING.name());
            stmt.setBigDecimal(4, java.math.BigDecimal.ZERO);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create cart order", e);
        }
        return -1;
    }

    @Override
    public void addToCart(int orderId, int productId, int quantity) {
        // Check if product already exists in cart
        String checkSql = "SELECT order_detail_id, quantity FROM Order_Detail WHERE order_id = ? AND product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, productId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Update quantity if product exists
                    int orderDetailId = rs.getInt("order_detail_id");
                    int newQuantity = rs.getInt("quantity") + quantity;
                    updateCartItemQuantity(orderDetailId, newQuantity);
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check product in cart", e);
        }

        // Add new item to cart
        String sql = "INSERT INTO Order_Detail (order_id, product_id, quantity, unit_price) " +
                     "SELECT ?, ?, ?, price FROM Product WHERE product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }

    @Override
    public List<OrderDetail> getCartItems(int orderId) {
        List<OrderDetail> cartItems = new ArrayList<>();
        String sql = "SELECT od.order_detail_id, od.order_id, od.quantity, od.unit_price, " +
                     "p.product_id, p.name, p.image_url, p.price " +
                     "FROM Order_Detail od JOIN Product p ON od.product_id = p.product_id " +
                     "WHERE od.order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_url"),
                        rs.getBigDecimal("price")
                    );
                    Orders order = new Orders(rs.getInt("order_id"));
                    OrderDetail item = new OrderDetail(
                        rs.getInt("order_detail_id"),
                        order,
                        product,
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price")
                    );
                    cartItems.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve cart items", e);
        }
        return cartItems;
    }

    @Override
    public void updateCartItemQuantity(int orderDetailId, int quantity) {
        String sql = "UPDATE Order_Detail SET quantity = ? WHERE order_detail_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, orderDetailId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cart item quantity", e);
        }
    }

    @Override
    public void removeCartItem(int orderDetailId) {
        String sql = "DELETE FROM Order_Detail WHERE order_detail_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    @Override
    public double calculateTotalAmount(int orderId) {
        String sql = "SELECT SUM(quantity * unit_price) as total FROM Order_Detail WHERE order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to calculate total amount", e);
        }
        return 0.0;
    }

    @Override
    public void finalizeOrder(int orderId, double totalAmount) {
        String sql = "UPDATE Orders SET total_amount = ?, status = ? WHERE order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, new java.math.BigDecimal(totalAmount));
            stmt.setString(2, Status.PENDING.name());
            stmt.setInt(3, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to finalize order", e);
        }
    }

    @Override
    public List<Orders> getUserOrders(int userId) {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, order_date, status, total_amount FROM Orders WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Orders order = new Orders();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                    order.setStatus(Status.valueOf(rs.getString("status")));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user orders", e);
        }
        return orders;
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT od.order_detail_id, od.order_id, od.quantity, od.unit_price, " +
                     "p.product_id, p.name, p.image_url, p.price " +
                     "FROM Order_Detail od JOIN Product p ON od.product_id = p.product_id " +
                     "WHERE od.order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_url"),
                        rs.getBigDecimal("price")
                    );
                    Orders order = new Orders(rs.getInt("order_id"));
                    OrderDetail detail = new OrderDetail(
                        rs.getInt("order_detail_id"),
                        order,
                        product,
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price")
                    );
                    orderDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve order details", e);
        }
        return orderDetails;
    }

    @Override
    public void addOrderTracking(int orderId, Status status, String note) {
        String sql = "INSERT INTO Order_Tracking (order_id, status, update_date, note) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setString(2, status.name());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, note != null ? note : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order tracking", e);
        }
    }

    @Override
    public List<OrderTracking> getOrderTrackingHistory(int orderId) {
        List<OrderTracking> trackingHistory = new ArrayList<>();
        String sql = "SELECT tracking_id, order_id, status, update_date, note FROM Order_Tracking WHERE order_id = ? ORDER BY update_date ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderTracking tracking = new OrderTracking();
                    tracking.setTrackingId(rs.getInt("tracking_id"));
                    tracking.setOrder(new Orders(rs.getInt("order_id")));
                    tracking.setStatus(Status.valueOf(rs.getString("status")));
                    tracking.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                    tracking.setNote(rs.getString("note"));
                    trackingHistory.add(tracking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve order tracking history", e);
        }
        return trackingHistory;
    }
}