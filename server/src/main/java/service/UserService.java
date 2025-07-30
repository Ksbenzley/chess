package service;
import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;

public class UserService {


    public static String registerUser(String user, String pass, String email, UserDAO memoryUserDAO, AuthDAO memoryAuthDAO)
    throws DataAccessException {
        //checks if username is already taken -else- registers the user
        if (user == null || pass == null){
            throw new BadRequestException("Error: bad request");
        }
        if (checkUser(user, memoryUserDAO)){
            throw new AlreadyTakenException("Error: username already taken");
        }else{
            memoryUserDAO.addUser(user, pass, email);
            return memoryAuthDAO.loginUser(user);
        }
    }

    public static String loginUser(String username, String password, UserDAO memoryUserDAO, AuthDAO memoryAuthDAO)
    throws DataAccessException{
        if(username == null || password == null){
            throw new BadRequestException("Error: bad request");
        }
        if (checkUser(username, memoryUserDAO)){
            if(memoryUserDAO.checkPass(username, password)){
                return memoryAuthDAO.loginUser(username);
            }else{
                throw new NotAuthorizedException("Error: unauthorized");
            }
        }else{
            throw new NotAuthorizedException("Error: unauthorized");

        }
    }

    public static Boolean checkUser(String user, UserDAO memoryUserDAO) throws DataAccessException{
        return memoryUserDAO.checkForUser(user);
    }
}
