package User;


import Mapper.UserMapper;

public abstract class UserService {

    private final UserRepository userRepository = new UserRepository();

    public <T extends User> T userLogin(String tableName, String loginField, String loginValue, String password, UserMapper<T> mapper) {
        return userRepository.userLogin(tableName, loginField, loginValue, password, mapper);
    }

     public abstract User userLogin(String loginValue, String password);
}
