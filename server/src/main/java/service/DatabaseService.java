package service;

import dataaccess.*;

public class DatabaseService {

    public static void clear(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException{
        //clear user data
        userDAO.clear();
        //clear auth data
        authDAO.clear();
        //clear game data
        gameDAO.clear();
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
