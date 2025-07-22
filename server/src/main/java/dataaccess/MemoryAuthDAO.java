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
        for(AuthData data : authDB.values()){
            if (authToken.equals(data.authToken())){
                return data.username();
            }
        }
        return null;
    }

    public String loginUser(String username){
        String token = createAuthToken();
        authDB.put(token, new AuthData(username, token));
        return token;
    }

    public Boolean checkUserAuth(String authToken){
        for(AuthData data : authDB.values()){
            if(data.authToken().equals(authToken)){
                return true;
            }
        }
        return false;
    }

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }
}
