package com.tourismapp.common;

public enum PaymentMethod {
    CASH("tiền mặt"),
    BANKING("chuyển khoản"),
    CASH_ON_DELIVERY("thanh toán khi nhận hàng"),
    MOMO("MoMo"),
    VNPAY("VNPay");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}