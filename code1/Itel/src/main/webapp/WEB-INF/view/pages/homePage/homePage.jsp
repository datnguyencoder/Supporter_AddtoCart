<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.tourismapp.config.ProjectPaths" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Home Page</title>

        <!-- Bootstrap 5.3 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous"/>

        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

        <!-- Add AOS CSS -->
        <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    </head>

    <body>
        <!-- Navbar -->
        <jsp:include page="/WEB-INF/view/components/navbar.jsp" />

        <!-- Main Banner -->
        <div class="banner-wrapper">
            <div id="bannerCarousel" class="carousel slide banner-container" data-bs-ride="carousel" data-bs-interval="3000">
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img src="${pageContext.request.contextPath}/assets/images/banner/b1.png" alt="Banner 1" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                        <img src="${pageContext.request.contextPath}/assets/images/banner/b2.png" alt="Banner 2" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                        <img src="${pageContext.request.contextPath}/assets/images/banner/b3.png" alt="Banner 3" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                        <img src="${pageContext.request.contextPath}/assets/images/banner/b4.png" alt="Banner 4" class="d-block w-100">
                    </div>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#bannerCarousel" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#bannerCarousel" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
        </div>

        <div class="container my-5">
            <div class="row">
                <div class="col-12 col-md-4 col-lg-3">
                    <div class="sidebar list-group" data-aos="fade-up">
                        <!-- Mega menu -->
                        <div id="mega-menu" class="mega-menu shadow bg-white"></div>
                        <!-- Categories -->
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Asus</a></li>
                             <li><a href="#">Acer</a></li>
                             <li><a href="#">MSI</a></li>
                             <li><a href="#">Lenovo</a></li>
                             <li><a href="#">Gigabyte</a></li>
                             <li><a href="#">Apple</a></li>
                             <li><a href="#">Dell</a></li>
                             <li><a href="#">HP</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Giá bán</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dưới 15 triệu</a></li>
                             <li><a href="#">Từ 15 đến 20 triệu</a></li>
                             <li><a href="#">Trên 20 triệu</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">CPU Intel - AMD</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Intel Core i3</a></li>
                             <li><a href="#">Intel Core i5</a></li>
                             <li><a href="#">Intel Core i7</a></li>
                             <li><a href="#">Intel Core i9</a></li>
                             <li><a href="#">AMD Ryzen 5</a></li>
                             <li><a href="#">AMD Ryzen 7</a></li>
                             <li><a href="#">AMD Ryzen 9</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Nhu cầu sử dụng</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Đồ họa - Studio</a></li>
                             <li><a href="#">Học sinh - Sinh viên</a></li>
                             <li><a href="#">Mỏng nhẹ cao cấp</a></li>
                             <li><a href="#">Ổ cứng độ bền</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Linh phụ kiện Laptop</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Ram laptop</a></li>
                             <li><a href="#">SSD laptop</a></li>
                             <li><a href="#">Ổ cứng di động</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-laptop me-2"></i> Laptop
                            </a>
                        </div>
                        <!-- Other categories remain unchanged -->
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Asus</a></li>
                             <li><a href="#">Acer</a></li>
                             <li><a href="#">MSI</a></li>
                             <li><a href="#">Lenovo</a></li>
                             <li><a href="#">Gigabyte</a></li>
                             <li><a href="#">Apple</a></li>
                             <li><a href="#">Dell</a></li>
                             <li><a href="#">HP</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Giá bán</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dưới 15 triệu</a></li>
                             <li><a href="#">Từ 15 đến 20 triệu</a></li>
                             <li><a href="#">Trên 20 triệu</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">CPU Intel - AMD</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Intel Core i3</a></li>
                             <li><a href="#">Intel Core i5</a></li>
                             <li><a href="#">Intel Core i7</a></li>
                             <li><a href="#">Intel Core i9</a></li>
                             <li><a href="#">AMD Ryzen 5</a></li>
                             <li><a href="#">AMD Ryzen 7</a></li>
                             <li><a href="#">AMD Ryzen 9</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Nhu cầu sử dụng</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Đồ họa - Studio</a></li>
                             <li><a href="#">Học sinh - Sinh viên</a></li>
                             <li><a href="#">Mỏng nhẹ cao cấp</a></li>
                             <li><a href="#">Ổ cứng độ bền</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Linh phụ kiện PC</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Ram PC</a></li>
                             <li><a href="#">SSD PC</a></li>
                             <li><a href="#">Ổ cứng di động</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-desktop me-2"></i> PC
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Apple</a></li>
                             <li><a href="#">Samsung</a></li>
                             <li><a href="#">Xiaomi</a></li>
                             <li><a href="#">Oppo</a></li>
                             <li><a href="#">Vivo</a></li>
                             <li><a href="#">Realme</a></li>
                             <li><a href="#">OnePlus</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Khoảng giá</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dưới 3 triệu</a></li>
                             <li><a href="#">Từ 3 đến 7 triệu</a></li>
                             <li><a href="#">Từ 7 đến 15 triệu</a></li>
                             <li><a href="#">Trên 15 triệu</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Hệ điều hành</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Android</a></li>
                             <li><a href="#">iOS</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Tính năng nổi bật</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">5G</a></li>
                             <li><a href="#">Chống nước</a></li>
                             <li><a href="#">Sạc nhanh</a></li>
                             <li><a href="#">Màn hình lớn</a></li>
                             <li><a href="#">Camera chất lượng cao</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện điện thoại</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Ốp lưng</a></li>
                             <li><a href="#">Sạc dự phòng</a></li>
                             <li><a href="#">Tai nghe Bluetooth</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-mobile-alt me-2"></i> Điện thoại
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Sony</a></li>
                             <li><a href="#">Bose</a></li>
                             <li><a href="#">JBL</a></li>
                             <li><a href="#">Apple</a></li>
                             <li><a href="#">Sennheiser</a></li>
                             <li><a href="#">Anker</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Loại tai nghe</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Tai nghe không dây</a></li>
                             <li><a href="#">Tai nghe có dây</a></li>
                             <li><a href="#">Tai nghe chống ồn</a></li>
                             <li><a href="#">Tai nghe gaming</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện tai nghe</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Đệm tai</a></li>
                             <li><a href="#">Cáp sạc</a></li>
                             <li><a href="#">Bảo vệ case</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-headphones-alt me-2"></i> Tai nghe
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Logitech</a></li>
                             <li><a href="#">Razer</a></li>
                             <li><a href="#">Corsair</a></li>
                             <li><a href="#">Keychron</a></li>
                             <li><a href="#">SteelSeries</a></li>
                             <li><a href="#">Microsoft</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Loại bàn phím</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Bàn phím cơ</a></li>
                             <li><a href="#">Bàn phím membrane</a></li>
                             <li><a href="#">Bàn phím không dây</a></li>
                             <li><a href="#">Bàn phím gaming</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện bàn phím</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Keycap</a></li>
                             <li><a href="#">Dây cáp</a></li>
                             <li><a href="#">Tấm kê tay</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-keyboard me-2"></i> Bàn phím
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Logitech</a></li>
                             <li><a href="#">Razer</a></li>
                             <li><a href="#">Corsair</a></li>
                             <li><a href="#">SteelSeries</a></li>
                             <li><a href="#">Apple</a></li>
                             <li><a href="#">Microsoft</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Loại chuột</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Chuột không dây</a></li>
                             <li><a href="#">Chuột có dây</a></li>
                             <li><a href="#">Chuột gaming</a></li>
                             <li><a href="#">Chuột chuyên dụng đồ họa</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện chuột</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Lót chuột</a></li>
                             <li><a href="#">Dây cáp</a></li>
                             <li><a href="#">Bảo vệ chuột</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-mouse me-2"></i> Chuột
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dell</a></li>
                             <li><a href="#">HP</a></li>
                             <li><a href="#">Lenovo</a></li>
                             <li><a href="#">Asus</a></li>
                             <li><a href="#">Acer</a></li>
                             <li><a href="#">MSI</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Loại PC</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">PC Gaming</a></li>
                             <li><a href="#">PC Văn phòng</a></li>
                             <li><a href="#">PC Đồ họa - Thiết kế</a></li>
                             <li><a href="#">PC Server</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện PC</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Bàn phím</a></li>
                             <li><a href="#">Chuột</a></li>
                             <li><a href="#">Màn hình</a></li>
                             <li><a href="#">Loa</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-desktop me-2"></i> PC - Máy tính bàn
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thương Hiệu</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dell</a></li>
                             <li><a href="#">LG</a></li>
                             <li><a href="#">Samsung</a></li>
                             <li><a href="#">Asus</a></li>
                             <li><a href="#">BenQ</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Kích thước màn hình</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dưới 24 inch</a></li>
                             <li><a href="#">24 - 27 inch</a></li>
                             <li><a href="#">Trên 27 inch</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Loại màn hình</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">IPS</a></li>
                             <li><a href="#">VA</a></li>
                             <li><a href="#">TN</a></li>
                             <li><a href="#">Màn hình cong</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-tv me-2"></i> Màn hình máy tính
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thành phần linh kiện</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Mainboard</a></li>
                             <li><a href="#">CPU</a></li>
                             <li><a href="#">RAM</a></li>
                             <li><a href="#">Card màn hình</a></li>
                             <li><a href="#">Ổ cứng SSD/HDD</a></li>
                             <li><a href="#">Nguồn máy tính</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện linh kiện</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Quạt tản nhiệt</a></li>
                             <li><a href="#">Tản nhiệt nước</a></li>
                             <li><a href="#">Cáp nối</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-microchip me-2"></i> Linh kiện máy tính
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện phổ biến</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Dây cáp USB</a></li>
                             <li><a href="#">Hub USB</a></li>
                             <li><a href="#">Bàn di chuột</a></li>
                             <li><a href="#">Sạc dự phòng</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện mở rộng</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Đế tản nhiệt</a></li>
                             <li><a href="#">Tấm chắn bụi</a></li>
                             <li><a href="#">Kẹp giữ dây</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-plug me-2"></i> Phụ kiện máy tính
                            </a>
                        </div>
                        <div class="list-group-item list-group-item-action d-flex align-items-center category-item position-relative" data-mega-menu='
                             <div class="mega-menu-content">
                             <div class="menu-column">
                             <h6 class="fw-bold">Thiết bị chơi game</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Bàn phím gaming</a></li>
                             <li><a href="#">Chuột gaming</a></li>
                             <li><a href="#">Ghế gaming</a></li>
                             <li><a href="#">Tai nghe gaming</a></li>
                             </ul>
                             </div>
                             <div class="menu-column">
                             <h6 class="fw-bold">Phụ kiện Gaming</h6>
                             <ul class="list-unstyled">
                             <li><a href="#">Mousepad</a></li>
                             <li><a href="#">Bàn di</a></li>
                             <li><a href="#">Đèn LED RGB</a></li>
                             </ul>
                             </div>
                             </div>
                             '>
                            <a href="#" class="text-decoration-none text-dark flex-grow-1">
                                <i class="fas fa-gamepad me-2"></i> Gaming Gear
                            </a>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-8 col-lg-9">
                    <div class="content" data-aos="fade-left">
                        <div class="row">
                            <div class="product-grid">
                                <c:forEach var="product" items="${activeProducts}">
                                    <div class="product-card card h-100 mb-4" data-aos="zoom-in">
                                        <div class="image-container">
                                            <a href="#">
                                                <img src="${product.imageUrl}" class="product-img card-img-top" alt="${product.name}">
                                            </a>
                                        </div>
                                        <div class="card-body text-center">
                                            <h5 class="product-name card-title">${product.name}</h5>
                                            <p class="card-text text-danger fw-bold">
                                                <fmt:formatNumber value="${product.price}" type="number" pattern="#,###" currencySymbol="" groupingUsed="true" /> VNĐ
                                            </p>
                                            <div class="d-flex justify-content-center gap-2">
                                                <a href="<%= ProjectPaths.HREF_TO_PRODUCTPAGE%>&id=${product.productId}" class="btn btn-buy text-white">Xem chi tiết</a>
                                                <button class="btn btn-cart text-white add-to-cart" data-product-id="<c:out value='${product.productId}'/>">Thêm vào giỏ</button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
                    
        

        <!-- Footer -->
        <div class="mt-5">
            <jsp:include page="/WEB-INF/view/components/footer.jsp" />
        </div>

        <!-- Bootstrap Bundle JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

        <!-- Carousel Auto Init -->
        <script>
            const myCarousel = document.querySelector('#bannerCarousel');
            new bootstrap.Carousel(myCarousel, {
                interval: 3000,
                ride: 'carousel'
            });
        </script>


<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Handler cho nút "Thêm vào giỏ"
        document.querySelectorAll('.add-to-cart').forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.dataset.productId;
                const quantity = 1; // Mặc định thêm 1 sản phẩm

                // Disable button để tránh click nhiều lần
                this.disabled = true;
                const originalText = this.textContent;
                this.textContent = 'Đang thêm...';

                fetch('/Itel/cart/add', {
                    method: 'POST',
                    headers: { 
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: `productId=${productId}&quantity=${quantity}`
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        // Hiển thị thông báo thành công
                        showNotification('Đã thêm sản phẩm vào giỏ hàng!', 'success');

                        // Có thể cập nhật số lượng giỏ hàng ở header nếu có
                        updateCartCount();
                    } else {
                        showNotification('Lỗi: ' + data.message, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error adding to cart:', error);
                    showNotification('Có lỗi xảy ra khi thêm vào giỏ hàng. Vui lòng thử lại!', 'error');
                })
                .finally(() => {
                    // Enable lại button
                    this.disabled = false;
                    this.textContent = originalText;
                });
            });
        });
    });

    function showNotification(message, type) {
        if (type === undefined) {
            type = 'info';
        }
    
        const toast = document.createElement('div');
        toast.className = 'alert alert-' + (type === 'success' ? 'success' : 'danger') + ' alert-dismissible fade show position-fixed';
        toast.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        toast.innerHTML = message + '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>';

        document.body.appendChild(toast);

        setTimeout(function() {
            if (toast.parentNode) {
                toast.remove();
            }
        }, 3000);
    
    function updateCartCount() {
        const cartCountElement = document.querySelector('.cart-count');
        if (cartCountElement) {
            fetch('/Itel/cart/count', {
                method: 'GET',
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    cartCountElement.textContent = data.count;
                }
            })
            .catch(error => console.error('Error updating cart count:', error));
            }
        }
    </script>
        <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
        <script>
            AOS.init();
        </script>

        <!-- Mega menu -->
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const sidebar = document.querySelector('.sidebar');
                const megaMenu = document.querySelector('#mega-menu');
                const categoryItems = document.querySelectorAll('.category-item');

                if (sidebar && megaMenu) {
                    const sidebarHeight = sidebar.offsetHeight;
                    megaMenu.style.height = sidebarHeight + 'px';
                }

                let isMouseOverSidebar = false;
                let isMouseOverMegaMenu = false;
                let hideTimeout = null;

                function showMegaMenu(content) {
                    if (hideTimeout) {
                        clearTimeout(hideTimeout);
                        hideTimeout = null;
                    }
                    megaMenu.innerHTML = content;
                    megaMenu.style.display = 'block';
                }

                function tryHideMegaMenu() {
                    hideTimeout = setTimeout(() => {
                        if (!isMouseOverSidebar && !isMouseOverMegaMenu) {
                            megaMenu.style.display = 'none';
                            megaMenu.innerHTML = '';
                        }
                    }, 200);
                }

                categoryItems.forEach(item => {
                    item.addEventListener('mouseenter', function () {
                        const megaMenuContent = this.getAttribute('data-mega-menu');
                        if (megaMenuContent) {
                            showMegaMenu(megaMenuContent);
                        }
                        isMouseOverSidebar = true;
                    });

                    item.addEventListener('mouseleave', function () {
                        isMouseOverSidebar = false;
                        tryHideMegaMenu();
                    });
                });

                sidebar.addEventListener('mouseenter', () => {
                    isMouseOverSidebar = true;
                    if (hideTimeout) {
                        clearTimeout(hideTimeout);
                        hideTimeout = null;
                    }
                });

                sidebar.addEventListener('mouseleave', () => {
                    isMouseOverSidebar = false;
                    tryHideMegaMenu();
                });

                megaMenu.addEventListener('mouseenter', () => {
                    isMouseOverMegaMenu = true;
                    if (hideTimeout) {
                        clearTimeout(hideTimeout);
                        hideTimeout = null;
                    }
                });

                megaMenu.addEventListener('mouseleave', () => {
                    isMouseOverMegaMenu = false;
                    tryHideMegaMenu();
                });
            });
        </script>

        <!-- Add to Cart -->
        <script>
            document.querySelectorAll('.add-to-cart').forEach(button => {
                button.addEventListener('click', function () {
                    const productId = this.getAttribute('data-product-id');
                    
                    // Hiển thị thông báo đang xử lý
                    alert('Đang thêm sản phẩm vào giỏ hàng...');
                    
                    fetch('/Itel/cart/add', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: `productId=${productId}&quantity=1`
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert('Đã thêm sản phẩm vào giỏ hàng thành công!');
                            window.location.href = '<%= ProjectPaths.HREF_TO_CARTPAGE %>';
                        } else {
                            alert(data.message || 'Không thể thêm sản phẩm vào giỏ hàng');
                        }
                    })
                    .catch(error => {
                        console.error('Lỗi:', error);
                        alert('Đã xảy ra lỗi khi thêm vào giỏ hàng, vui lòng thử lại sau');
                    });
                });
            });
        </script>

        <style>
            .sidebar {
                background-color: #f0f0f0;
                position: relative;
                z-index: 1000;
            }

            .content {
                background-color: #e0e0e0;
            }

            body {
                background-color: #e1dbdb;
                margin: 0;
                font-family: 'Roboto', sans-serif;
            }

            .banner-wrapper {
                margin-top: 5px;
                padding-top: 20px;
            }

            .carousel-inner {
                width: 100%;
            }

            .carousel-item img {
                width: 100%;
                height: auto;
                max-height: 550px;
                object-fit: contain;
                display: block;
                margin: 0 auto;
            }

            .carousel-control-prev-icon,
            .carousel-control-next-icon {
                background-color: rgba(0, 0, 0, 0.6);
                border-radius: 50%;
                width: 40px;
                height: 40px;
                background-size: 60% 60%;
                background-position: center;
                background-repeat: no-repeat;
                padding: 10px;
            }

            .carousel-control-prev:hover .carousel-control-prev-icon,
            .carousel-control-next:hover .carousel-control-next-icon {
                background-color: rgba(0, 0, 0, 0.85);
                transition: 0.3s;
            }

            .list-group-item {
                background-color: #ffffff;
                color: #000000;
                transition: background-color 0.3s, color 0.3s;
                border-radius: 5px;
            }

            .list-group-item a {
                color: #000000;
                text-decoration: none;
                width: 100%;
            }

            .list-group-item i {
                color: #000000;
            }

            .list-group-item:hover {
                background-color: #66ccff;
                color: #000000;
            }

            .list-group-item:hover a, .list-group-item:hover i {
                color: #000000 !important;
            }

            .mega-menu {
                margin-left: 10px;
                display: none;
                position: absolute;
                top: 0;
                left: 100%;
                width: 980px;
                background: #fff;
                z-index: 2000;
                border: 1px solid #ddd;
                border-radius: 1%;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                padding: 20px;
            }

            .mega-menu-content {
                display: flex;
                justify-content: space-between;
                height: 100%;
            }

            .menu-column {
                flex: 1;
                margin-right: 20px;
            }

            .menu-column h6 {
                font-size: 14px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .menu-column ul {
                list-style: none;
                padding: 0;
            }

            .menu-column ul li a {
                display: block;
                padding: 5px 0;
                color: #000;
                text-decoration: none;
            }

            .menu-column ul li a:hover {
                color: #007bff;
            }

            .product-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 2rem;
            }

            .image-container {
                width: 100%;
                height: 260px;
                overflow: hidden;
                position: relative;
            }

            .product-img {
                width: 100%;
                height: 260px;
                object-fit: cover;
                transition: transform 0.3s ease;
            }

            .product-img:hover {
                transform: scale(1.05);
            }

            .btn-buy {
                background-color: #66c0ff;
                border: none;
                border-radius: 4px;
                padding: 6px 12px;
                transition: background-color 0.3s ease;
            }

            .btn-buy:hover,
            .btn-buy:focus {
                background-color: #4aa6f9;
                color: white;
                text-decoration: none;
                outline: none;
            }

            .btn-cart {
                background-color: #28a745;
                border: none;
                border-radius: 4px;
                padding: 6px 12px;
                transition: background-color 0.3s ease;
            }

            .btn-cart:hover,
            .btn-cart:focus {
                background-color: #218838;
                color: white;
                text-decoration: none;
                outline: none;
            }

            .product-card {
                position: relative;
                z-index: 1000;
            }

            .product-card .card-body {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                height: 150px;
                padding: 10px;
            }

            .product-name {
                height: 48px;
                line-height: 24px;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                margin-bottom: 10px;
            }

            .card-text {
                margin-bottom: 10px;
            }

            .d-flex.justify-content-center {
                margin-top: auto;
            }
        </style>
    </body>
</html>