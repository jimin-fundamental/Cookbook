package cookbook.repository;

import cookbook.model.User;

public interface UserRepository {
    boolean insertUser(String name, String username, String password);
    User checkUser(String username, String password);
    // Other user-related methods
}

