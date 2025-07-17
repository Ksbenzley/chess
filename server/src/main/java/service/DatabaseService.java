package service;

import dataaccess.*;

public class DatabaseService {

    public static void clear(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        //clear user data
        memoryUserDAO.clear();
        //clear auth data
        memoryAuthDAO.clear();
        //clear game data
        memoryGameDAO.clear();
    }

    public static void logout(String authToken, MemoryAuthDAO memoryAuthDAO){
        if(checkAuthToken(authToken, memoryAuthDAO)){
            memoryAuthDAO.logout(authToken);
            return;
        }
        throw new NotAuthorizedException("Error: unauthorized");
    }

    public static Boolean checkAuthToken(String authToken, MemoryAuthDAO memoryAuthDAO){
        if(memoryAuthDAO.checkUserAuth(authToken)){
            return true;
        }
        return false;
    }
}
