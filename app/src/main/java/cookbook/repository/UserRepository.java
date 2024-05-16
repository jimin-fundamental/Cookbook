package cookbook.repository;

import java.util.List;

import cookbook.model.User;

public interface UserRepository {
    boolean insertUser(String name, String username, String password);
    User checkUser(String username, String password);
    List<User> getAllUser();
    void editUser(User user);
    void deleteUser(User user);
    // Other user-related methods
}

