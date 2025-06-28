package com.tourismapp.controller.redirectController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.tourismapp.common.Status;
import com.tourismapp.config.ProjectPaths;
import com.tourismapp.controller.mainController.MainControllerServlet;
import com.tourismapp.dao.DBConnection;
import com.tourismapp.model.OrderDetail;
import com.tourismapp.model.Product;
import com.tourismapp.model.Orders;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "CartPageServlet", urlPatterns = {MainControllerServlet.CARTPAGE_SERVLET})
public class CartPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin người dùng từ session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId != null) {
            try {
                // Tìm giỏ hàng hiện tại của người dùng
                Orders cartOrder = getCartOrder(userId);
                
                if (cartOrder != null) {
                    // Lấy các mặt hàng trong giỏ hàng
                    List<OrderDetail> cartItems = getCartItems(cartOrder.getOrderId());
                    request.setAttribute("cartItems", cartItems);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        request.getRequestDispatcher(ProjectPaths.JSP_CARTPAGE_PATH).forward(request, response);
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
    
    private List<OrderDetail> getCartItems(int orderId) throws SQLException {
        List<OrderDetail> cartItems = new ArrayList<>();
        String sql = "SELECT od.*, p.* FROM Order_Detail od " +
                     "JOIN Product p ON od.product_id = p.product_id " +
                     "WHERE od.order_id = ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail item = new OrderDetail();
                    item.setOrderDetailId(rs.getInt("order_detail_id"));
                    
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setStockQuantity(rs.getInt("stock"));
                    
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    
                    cartItems.add(item);
                }
            }
        }
        return cartItems;
    }

    // <editor-fold defaultstate="collapsed" desc=" functional ... ">
    
    // </editor-fold>
}
