package dataaccess;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public Boolean checkForUser(String username) throws DataAccessException;
    public void addUser(String username, String password, String email) throws DataAccessException;
    public Boolean checkPass(String username, String passowrd) throws DataAccessException;
}
