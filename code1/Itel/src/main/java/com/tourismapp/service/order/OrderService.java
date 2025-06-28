package com.tourismapp.service.order;

import com.tourismapp.common.Status;
import com.tourismapp.dao.order.OrderDAO;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.OrderTracking;
import com.tourismapp.model.Orders;
import java.util.List;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderService implements IOrderService {
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    @Override
    public int getOrCreateCartOrder(int userId) {
        // Check if user has a pending order
        String sql = "SELECT order_id FROM Orders WHERE user_id = ? AND status = 'PENDING'";
        try (Connection conn = orderDAO.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("order_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check pending order", e);
        }
        // Create new cart order if none exists
        return orderDAO.createCartOrder(userId);
    }

    @Override
    public void addToCart(int userId, int productId, int quantity) {
        int orderId = getOrCreateCartOrder(userId);
        orderDAO.addToCart(orderId, productId, quantity);
    }

    @Override
    public List<OrderDetail> getCartItems(int userId) {
        int orderId = getOrCreateCartOrder(userId);
        return orderDAO.getCartItems(orderId);
    }

    @Override
    public void updateCartItemQuantity(int orderDetailId, int quantity) {
        orderDAO.updateCartItemQuantity(orderDetailId, quantity);
    }

    @Override
    public void removeCartItem(int orderDetailId) {
        orderDAO.removeCartItem(orderDetailId);
    }

    @Override
    public double calculateCartTotal(int userId) {
        int orderId = getOrCreateCartOrder(userId);
        return orderDAO.calculateTotalAmount(orderId);
    }

    @Override
    public int finalizeOrderForPayment(int userId) {
        int orderId = getOrCreateCartOrder(userId);
        double totalAmount = orderDAO.calculateTotalAmount(orderId);
        orderDAO.finalizeOrder(orderId, totalAmount);
        return orderId;
    }
    
    @Override
    public List<Orders> getUserOrders(int userId) {
        return orderDAO.getUserOrders(userId);
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDAO.getOrderDetails(orderId);
    }

    @Override
    public void addOrderTracking(int orderId, Status status, String note) {
        orderDAO.addOrderTracking(orderId, status, note);
    }

    @Override
    public List<OrderTracking> getOrderTrackingHistory(int orderId) {
        return orderDAO.getOrderTrackingHistory(orderId);
    }
}