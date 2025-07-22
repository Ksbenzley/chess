package dataaccess;

public interface AuthDAO {
    public void clear() throws DataAccessException;
    public void logout(String username) throws DataAccessException;
    public String getUser(String authToken) throws DataAccessException;
    public String createAuthToken();
    public String loginUser(String username) throws DataAccessException;
    public Boolean checkUserAuth(String authToken) throws DataAccessException;
}
