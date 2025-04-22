package Admin;

import User.User;
import User.UserService;


public class AdminService extends UserService {

    private AdminRepository adminRepository = new AdminRepository();

    AdminMapper adminMapper = new AdminMapper();


    @Override
    public User userLogin( String loginValue, String password) {
        return super.userLogin("admins", "username", loginValue, password, adminMapper); // Skicka med loginField
    }
}


