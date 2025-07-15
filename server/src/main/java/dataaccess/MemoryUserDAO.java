package dataaccess;
import model.*;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO{

    static HashMap<String, userData> userDB = new HashMap<>();
    static HashMap<String, AuthData> authDB = new HashMap<>();

    public String addUser(String username, String password, String email){
        //adds the users info to the database
        userDB.put(username, new userData(username, password, email));
        //applies an authToken to the new user
        authDB.put(username, new AuthData(username, createAuthToken()));
        return authDB.get(username).toString();
    }

    public String loginUser(String username){
        authDB.put(username, new AuthData(username, createAuthToken()));
        return authDB.get(username).toString();
    }

    public Boolean checkForUser(String username){
        if(userDB.containsKey(username)){
            return true;
        }else{
            return false;
        }
    }

    public Boolean checkAuthToken(String username){
        if(authDB.containsKey(username)){
            return true;
        }else{
            return false;
        }
    }

    public Boolean checkPass(String username, String password){
        if(userDB.get(username).password().equals(password)){
            return true;
        }else{
            return false;
        }
    }

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }
}
