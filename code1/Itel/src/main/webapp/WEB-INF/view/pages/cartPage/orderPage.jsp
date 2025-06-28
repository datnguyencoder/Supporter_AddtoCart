<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>My Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            background-color: #f8f9fa;
        }
        .content {
            padding-top: 100px;
            padding-bottom: 50px;
            min-height: 100vh;
        }
        .order-container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .order-card {
            background-color: white;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s;
        }
        .order-card:hover {
            transform: translateY(-5px);
        }
        .order-header {
            background-color: #007bff;
            color: white;
            padding: 10px;
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
        }
        .tracking-timeline {
            padding: 20px;
        }
        .tracking-item {
            position: relative;
            padding-left: 30px;
            margin-bottom: 20px;
        }
        .tracking-item::before {
            content: '';
            position: absolute;
            left: 10px;
            top: 0;
            bottom: 0;
            width: 2px;
            background-color: #ddd;
        }
        .tracking-item::after {
            content: '';
            position: absolute;
            left: 5px;
            top: 5px;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background-color: #007bff;
        }
    </style>
</head>
<body>
    <div>
        <jsp:include page="/WEB-INF/view/components/navbar.jsp" />
    </div>

    <div class="content">
        <div class="order-container">
            <h1 class="text-center mb-4">My Orders</h1>
            <c:choose>
                <c:when test="${empty orders}">
                    <div class="alert alert-info text-center">
                        You have no orders. <a href="/products" class="alert-link">Start shopping</a>.
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="order" items="${orders}">
                        <div class="order-card">
                            <div class="order-header">
                                <h5>Order #${order.orderId} - <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/> - ${order.status}</h5>
                            </div>
                            <div class="card-body">
                                <h6>Order Details:</h6>
                                <table class="table table-borderless">
                                    <thead>
                                        <tr>
                                            <th>Product</th>
                                            <th>Price</th>
                                            <th>Quantity</th>
                                            <th>Total</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="detail" items="${order.orderDetails}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <img src="${detail.product.imageUrl}" alt="${detail.product.name}" class="me-3" style="max-width: 50px;">
                                                        ${detail.product.name}
                                                    </div>
                                                </td>
                                                <td><fmt:formatNumber value="${detail.unitPrice}" type="currency" currencySymbol="$" /></td>
                                                <td>${detail.quantity}</td>
                                                <td><fmt:formatNumber value="${detail.quantity * detail.unitPrice}" type="currency" currencySymbol="$" /></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <h6>Total: <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$" /></h6>

                                <h6 class="mt-4">Order Tracking:</h6>
                                <div class="tracking-timeline">
                                    <c:forEach var="tracking" items="${order.trackingHistory}">
                                        <div class="tracking-item">
                                            <p><strong>${tracking.status}</strong> - <fmt:formatDate value="${tracking.updateDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                                            <p>${tracking.note}</p>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-5">
        <jsp:include page="/WEB-INF/view/components/footer.jsp" />
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>