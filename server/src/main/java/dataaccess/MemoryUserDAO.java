package dataaccess;
import model.userData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    static HashMap<String, userData> userDB = new HashMap<>();

    public void addUser(String username, String password, String email){
        userDB.put(username, new userData(username, password, email));
        createAuthToken(username);
    }

    public Boolean checkForUser(String username){
        if(userDB.containsKey(username)){
            return true;
        }else{
            return false;
        }
    }

    public void createAuthToken(String user){

    }
}
