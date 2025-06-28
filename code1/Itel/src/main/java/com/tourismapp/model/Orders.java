package com.tourismapp.model;

import com.tourismapp.common.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Orders {
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private Status status;
    private BigDecimal totalAmount;
    private List<OrderDetail> orderDetails;
    private List<OrderTracking> trackingHistory;

    public Orders() {}

    public Orders(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderTracking> getTrackingHistory() {
        return trackingHistory;
    }

    public void setTrackingHistory(List<OrderTracking> trackingHistory) {
        this.trackingHistory = trackingHistory;
    }
}