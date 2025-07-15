package dataaccess;

public interface UserDAO {
    public void clear();
    public Boolean checkForUser(String username);
    public void addUser(String username, String password, String email);
    public Boolean checkPass(String username, String passowrd);
}
