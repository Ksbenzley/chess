package service;

import dataaccess.*;

public class DatabaseService {

    public static void clear(UserDAO UserDAO, AuthDAO AuthDAO, GameDAO GameDAO) throws DataAccessException{
        //clear user data
        UserDAO.clear();
        //clear auth data
        AuthDAO.clear();
        //clear game data
        GameDAO.clear();
    }

    public static void logout(String authToken, AuthDAO memoryAuthDAO) throws DataAccessException, NotAuthorizedException{
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
