package com.tourismapp.config;

/**
 * Defines all paths for JSP pages and URL endpoints in the application.
 */
public class ProjectPaths {

    // Prefix for main controller endpoints (used for dashboard and user views)
    public static final String PREFIX_WEB_PATH = "/Itel";
    public static final String HREF_TO_MAINCONTROLLER = PREFIX_WEB_PATH + "/main?action=";

    // Dashboard endpoints (via MainControllerServlet)
    public static final String HREF_TO_DASHBOARDPAGE = HREF_TO_MAINCONTROLLER + "dashboardPage";
    public static final String HREF_TO_USERMANAGEMENT = HREF_TO_MAINCONTROLLER + "userManagement";
    public static final String HREF_TO_BRANDMANAGEMENT = HREF_TO_MAINCONTROLLER + "brandManagement";
    public static final String HREF_TO_CATEGORYMANAGEMENT = HREF_TO_MAINCONTROLLER + "categoryManagement";
    public static final String HREF_TO_PRODUCTMANAGEMENT = HREF_TO_MAINCONTROLLER + "productManagement";
    public static final String HREF_TO_ORDERMANAGEMENT = HREF_TO_MAINCONTROLLER + "orderManagement";

    // User view endpoints (via MainControllerServlet)
    public static final String HREF_TO_LOGINPAGE = HREF_TO_MAINCONTROLLER + "loginPage";
    public static final String HREF_TO_LOGOUTPAGE = HREF_TO_MAINCONTROLLER + "logoutPage";
    public static final String HREF_TO_PROFILEPAGE = HREF_TO_MAINCONTROLLER + "profilePage";
    public static final String HREF_TO_PRODUCTPAGE = HREF_TO_MAINCONTROLLER + "productPage";
    public static final String HREF_TO_HOMEPAGE = HREF_TO_MAINCONTROLLER + "homePage";

    // Direct endpoints (via CartServlet, MomoServlet, VNPayServlet)
    public static final String HREF_TO_CARTPAGE = "/cart";
    public static final String HREF_TO_ORDERPAGE = "/orders";
    public static final String HREF_TO_MOMO_PAYMENT = "/payment/momo";
    public static final String HREF_TO_VNPAY_PAYMENT = "/payment/vnpay";

    // JSP paths for view rendering
    public static final String JSP_PATH_VIEW = "/WEB-INF/view/pages/";
    public static final String JSP_PATH_DASHBOARD = "/WEB-INF/view/dashboard/";

    // Dashboard JSPs
    public static final String JSP_DASHBOARDPAGE_PATH = JSP_PATH_DASHBOARD + "dashboard.jsp";
    public static final String JSP_USERMANAGEMENT_PATH = JSP_PATH_DASHBOARD + "userManagement/userManagement.jsp";
    public static final String JSP_BRANDMANAGEMENT_PATH = JSP_PATH_DASHBOARD + "brandManagement/brandManagement.jsp";
    public static final String JSP_MANAGEBRAND_PATH = JSP_PATH_DASHBOARD + "brandManagement/manageBrand.jsp";
    public static final String JSP_CREATEBRAND_PATH = JSP_PATH_DASHBOARD + "brandManagement/createBrand.jsp";
    public static final String JSP_UPDATEBRAND_PATH = JSP_PATH_DASHBOARD + "brandManagement/updateBrand.jsp";
    public static final String JSP_CATEGORYMANAGEMENT_PATH = JSP_PATH_DASHBOARD + "categoryManagement/categoryManagement.jsp";
    public static final String JSP_CREATE_CATEGORY_PATH = JSP_PATH_DASHBOARD + "categoryManagement/createCategory.jsp";
    public static final String JSP_EDIT_CATEGORY_PATH = JSP_PATH_DASHBOARD + "categoryManagement/editCategory.jsp";
    public static final String JSP_MANAGE_CATEGORY_PATH = JSP_PATH_DASHBOARD + "categoryManagement/manageCategory.jsp";
    public static final String JSP_PRODUCTMANAGEMENT_PATH = JSP_PATH_DASHBOARD + "productManagement/productManagement.jsp";
    public static final String JSP_ORDERMANAGEMENT_PATH = JSP_PATH_DASHBOARD + "orderManagement/orderManagement.jsp";

    // User view JSPs
    public static final String JSP_LOGINPAGE_PATH = JSP_PATH_VIEW + "loginPage/loginPage.jsp";
    public static final String JSP_PROFILEPAGE_PATH = JSP_PATH_VIEW + "profilePage/profilePage.jsp";
    public static final String JSP_HOMEPAGE_PATH = JSP_PATH_VIEW + "homePage/homePage.jsp";
    public static final String JSP_PRODUCTPAGE_PATH = JSP_PATH_VIEW + "productPage/productPage.jsp";
    public static final String JSP_PRODUCTDETAILPAGE_PATH = JSP_PATH_VIEW + "productPage/productDetail.jsp";
    public static final String JSP_CARTPAGE_PATH = JSP_PATH_VIEW + "cartPage/cartPage.jsp";
    public static final String JSP_ORDERPAGE_PATH = JSP_PATH_VIEW + "orderPage/orderPage.jsp";
}