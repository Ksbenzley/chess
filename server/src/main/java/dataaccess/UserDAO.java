package dataaccess;

public interface UserDAO {
    public Boolean checkForUser(String username);
    public void addUser(String username, String password, String email);
}
