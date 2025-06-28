<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cart</title>
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
        .cart-container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .cart-item {
            background-color: white;
            border-radius: 8px;
            margin-bottom: 20px;
            padding: 15px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        .cart-item img {
            max-width: 100px;
            border-radius: 4px;
        }
        .total-section {
            background-color: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        .btn-remove {
            color: #dc3545;
            cursor: pointer;
        }
        .btn-remove:hover {
            color: #b02a37;
        }
    </style>
</head>
<body>
    <div>
        <jsp:include page="/WEB-INF/view/components/navbar.jsp" />
    </div>

    <div class="content">
        <div class="cart-container">
            <h1 class="text-center mb-4">Your Cart</h1>
            <c:choose>
                <c:when test="${empty cartItems}">
                    <div class="alert alert-info text-center">
                        Your cart is empty. <a href="/products" class="alert-link">Continue shopping</a>.
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="cartItems">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="cart-item" data-order-detail-id="${item.orderDetailId}">
                                <div class="row align-items-center">
                                    <div class="col-md-2">
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}" class="img-fluid">
                                    </div>
                                    <div class="col-md-4">
                                        <h5>${item.product.name}</h5>
                                    </div>
                                    <div class="col-md-2">
                                        <input type="number" class="form-control quantity-input" value="${item.quantity}" min="1" data-unit-price="${item.unitPrice}">
                                    </div>
                                    <div class="col-md-2">
                                        <p class="item-total"><fmt:formatNumber value="${item.quantity * item.unitPrice}" type="currency" currencySymbol="$" /></p>
                                    </div>
                                    <div class="col-md-2 text-end">
                                        <i class="fas fa-trash btn-remove"></i>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="total-section">
                        <h4>Total: <span id="cartTotal"><fmt:formatNumber value="${cartItems.stream().sum('quantity * unitPrice')}" type="currency" currencySymbol="$" /></span></h4>
                        <div class="text-end">
                            <form id="createOrderForm" action="/cart/createOrder" method="post">
                                <input type="hidden" id="orderId" name="orderId">
                            </form>
                            <button id="payMomoBtn" class="btn btn-primary me-2">Pay with MoMo</button>
                            <button id="payVNPayBtn" class="btn btn-success">Pay with VNPay</button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-5">
        <jsp:include page="/WEB-INF/view/components/footer.jsp" />
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', function() {
                const orderDetailId = this.closest('.cart-item').dataset.orderDetailId;
                const quantity = this.value;
                const unitPrice = this.dataset.unitPrice;
                const itemTotal = this.closest('.cart-item').querySelector('.item-total');

                itemTotal.textContent = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })
                    .format(quantity * unitPrice);

                fetch('/cart/update', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `orderDetailId=${orderDetailId}&quantity=${quantity}`
                }).then(response => response.json())
                  .then(data => {
                      if (data.success) {
                          calculateTotal();
                      } else {
                          alert(data.message);
                      }
                  });
            });
        });

        document.querySelectorAll('.btn-remove').forEach(button => {
            button.addEventListener('click', function() {
                const orderDetailId = this.closest('.cart-item').dataset.orderDetailId;
                fetch('/cart/remove', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `orderDetailId=${orderDetailId}`
                }).then(response => response.json())
                  .then(data => {
                      if (data.success) {
                          this.closest('.cart-item').remove();
                          calculateTotal();
                          if (!document.querySelector('.cart-item')) {
                              document.getElementById('cartItems').innerHTML = `
                                  <div class="alert alert-info text-center">
                                      Your cart is empty. <a href="/products" class="alert-link">Continue shopping</a>.
                                  </div>`;
                          }
                      } else {
                          alert(data.message);
                      }
                  });
            });
        });

        function calculateTotal() {
            let total = 0;
            document.querySelectorAll('.cart-item').forEach(item => {
                const quantity = parseInt(item.querySelector('.quantity-input').getInt());
                const unitPrice = parseFloat(item.querySelector('.quantity-input').dataset.unitPrice);
                total += quantity * unitPrice;
            });
            document.getElementById('cartTotal').textContent = new Intl.NumberFormat('en-US', {
                styleType: 'currency',
                currency: 'USD'
            }).format(total);
            return total;
        }

        document.getElementById('payMomoBtn').addEventListener('click', function() {
            fetch('/cart/createOrder', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            }).then(response => response.json())
              .then(data => {
                  if (data.success) {
                      const total = calculateTotal();
                      window.location.href = `/payment/momo?orderId=${data.orderId}&amount=${total}`;
                  } else {
                      alert(data.message);
                  }
              });
        });

        document.getElementById('payVNPayBtn').addEventListener('click', function() {
            fetch('/cart/createOrder', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            }).then(response => response.json())
              .then(data => {
                  if (data.success) {
                      const total = calculateTotal();
                      fetch('/payment/vnpay', {
                          method: 'POST',
                          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                          body: `orderId=${data.orderId}&amount=${total}`
                      }).then(response => {
                          if (response.redirected) {
                              window.location.href = response.url;
                          } else {
                              return response.json().then(json => alert(json.message));
                          }
                      });
                  } else {
                      alert(data.message);
                  }
              });
        });
    </script>
</body>
</html>