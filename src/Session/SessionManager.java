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

