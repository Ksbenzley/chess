package dataaccess;

public interface AuthDAO {
    public void clear();
    public void logout(String username);
    public String getUser(String authToken);
    public String createAuthToken();
    public String loginUser(String username);
    public Boolean checkUserAuth(String authToken);
}
