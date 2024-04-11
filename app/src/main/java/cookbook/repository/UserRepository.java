package cookbook.repository;

public interface UserRepository {
    boolean insertUser(String name, String username, String password);
    boolean checkUser(String username, String password);
    // Other user-related methods
}

