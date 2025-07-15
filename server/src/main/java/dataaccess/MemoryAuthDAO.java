package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    static HashMap<String, AuthData> authDB = new HashMap<>();

    public void clear(){
        authDB.clear();
    }

    public void logout(String username){
        authDB.remove(username);
    }

    public String getUser(String authToken){
        return authDB.get(authToken).username();
    }

    public String loginUser(String username){
        authDB.put(username, new AuthData(username, createAuthToken()));
        return authDB.get(username).toString();
    }

    public Boolean checkUserAuth(String authToken){
        if(authDB.containsValue(authToken)){
            return true;
        }
        return false;
    }

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }
}
