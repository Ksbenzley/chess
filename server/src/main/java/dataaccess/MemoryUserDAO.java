package dataaccess;
import model.*;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    static HashMap<String, UserData> userDB = new HashMap<>();

    public void clear(){
        userDB.clear();
    }

    public void addUser(String username, String password, String email){
        //adds the users info to the database
        userDB.put(username, new UserData(username, password, email));
    }

    public Boolean checkForUser(String username){
        if(userDB.containsKey(username)){
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
}
