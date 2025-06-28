package com.tourismapp.service.order;

import com.tourismapp.common.Status;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.OrderTracking;
import com.tourismapp.model.Orders;
import java.util.List;

public interface IOrderService {
    // Create a cart for the user if not exists
    int getOrCreateCartOrder(int userId);

    // Add a product to the cart
    void addToCart(int userId, int productId, int quantity);

    // Get all items in the cart
    List<OrderDetail> getCartItems(int userId);

    // Update quantity of an item in the cart
    void updateCartItemQuantity(int orderDetailId, int quantity);

    // Remove an item from the cart
    void removeCartItem(int orderDetailId);

    // Calculate total amount for the cart
    double calculateCartTotal(int userId);

    // Finalize order for payment
    int finalizeOrderForPayment(int userId);
    
    List<Orders> getUserOrders(int userId);
    List<OrderDetail> getOrderDetails(int orderId);

    // New methods for order tracking
    void addOrderTracking(int orderId, Status status, String note);
    List<OrderTracking> getOrderTrackingHistory(int orderId);
}