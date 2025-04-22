package Session;

public class SessionManager {

    private static SessionManager instance;

    private int loggedInUserId = -1;
    private String loggedInUserRole = null;
    private String loggedInUserName = "";

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId, String role) {
        this.loggedInUserId = userId;
        this.loggedInUserRole = role;
    }

    public void logout() {
        this.loggedInUserId = -1;
        this.loggedInUserRole = null;
        this.loggedInUserName = "";
    }

    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public String getLoggedInUserRole() {
        return loggedInUserRole;
    }

    public boolean isLoggedIn() {
        return loggedInUserId > 0 && loggedInUserRole != null;
    }

    public boolean isCustomerLoggedIn() {
        return "customer".equalsIgnoreCase(loggedInUserRole);
    }

    public boolean isAdminLoggedIn() {
        return "admin".equalsIgnoreCase(loggedInUserRole);
    }
}


//package Session;
//
//public class SessionManager {
//
//    private static SessionManager instance;
//
//    private int loggedInCustomerId = -1;
//    private int loggedInAdminId = -1;
//    private String loggedInCustomerName = "";
//
//
//
//    private SessionManager() {}
//
//    public static SessionManager getInstance() {
//        if (instance == null) {
//            instance = new SessionManager();
//        }
//        return instance;
//    }
//
//    public void loginCustomer(int customerId) {
//        loggedInCustomerId = customerId;
//    }
//
//    public void loginCustomer(int customerId, String name) {
//        loggedInCustomerId = customerId;
//        loggedInCustomerName = name;  // Sätt kundens namn
//    }
//
//    public void loginAdmin(int adminId) {
//        loggedInAdminId = adminId;
//    }
//
//    public void logoutCustomer() {
//        loggedInCustomerId = -1;
//    }
//
//    public void logoutAdmin() {
//        loggedInAdminId = -1;
//    }
//
//    public int getLoggedInCustomerId() {
//        return loggedInCustomerId;
//    }
//
//    public int getLoggedInAdminId() {
//        return loggedInAdminId;
//    }
//
//    public boolean isCustomerLoggedIn() {
//        return loggedInCustomerId > 0;
//    }
//
//    public boolean isAdminLoggedIn() {
//        return loggedInAdminId > 0;
//    }
//    public String getLoggedInCustomerName() {
//        return loggedInCustomerName;
//    }
//
//    public void setLoggedInCustomerName(String loggedInCustomerName) {
//        this.loggedInCustomerName = loggedInCustomerName;
//    }
//}
