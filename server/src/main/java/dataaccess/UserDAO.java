package dataaccess;

public interface UserDAO {
    public Boolean checkForUser(String username);
    public String addUser(String username, String password, String email);
}
