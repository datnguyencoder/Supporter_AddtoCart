package com.tourismapp.dao.order;

import com.tourismapp.common.Status;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.Orders;
import com.tourismapp.model.OrderTracking;
import java.util.List;

public interface IOrderDAO {
    // Create a temporary order (cart) for a user
    int createCartOrder(int userId);

    // Add a product to the cart (Order_Detail)
    void addToCart(int orderId, int productId, int quantity);

    // Get all items in the cart (Order_Detail) for an order
    List<OrderDetail> getCartItems(int orderId);

    // Update quantity of an item in the cart
    void updateCartItemQuantity(int orderDetailId, int quantity);

    // Remove an item from the cart
    void removeCartItem(int orderDetailId);

    // Calculate total amount for an order
    double calculateTotalAmount(int orderId);

    // Finalize order (update status and total amount)
    void finalizeOrder(int orderId, double totalAmount);

    // Get all orders for a user
    List<Orders> getUserOrders(int userId);

    // Get order details by orderId
    List<OrderDetail> getOrderDetails(int orderId);

    // Add tracking record
    void addOrderTracking(int orderId, Status status, String note);

    // Get tracking history for an order
    List<OrderTracking> getOrderTrackingHistory(int orderId);
}