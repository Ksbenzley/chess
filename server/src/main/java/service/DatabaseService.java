package service;

import dataaccess.*;

public class DatabaseService {

    public static void clear(UserDAO memoryUserDAO, AuthDAO memoryAuthDAO, GameDAO memoryGameDAO) throws DataAccessException{
        //clear user data
        memoryUserDAO.clear();
        //clear auth data
        memoryAuthDAO.clear();
        //clear game data
        memoryGameDAO.clear();
    }

    public static void logout(String authToken, AuthDAO memoryAuthDAO) throws DataAccessException{
        if(checkAuthToken(authToken, memoryAuthDAO)){
            memoryAuthDAO.logout(authToken);
            return;
        }
        throw new NotAuthorizedException("Error: unauthorized");
    }

    public static Boolean checkAuthToken(String authToken, AuthDAO memoryAuthDAO) throws DataAccessException{
        if(memoryAuthDAO.checkUserAuth(authToken)){
            return true;
        }
        return false;
    }
}
