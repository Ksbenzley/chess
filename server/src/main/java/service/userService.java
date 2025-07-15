package service;
import dataaccess.*;

public class userService {


    public static String registerUser(String user, String pass, String email, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        //checks if username is already taken -else- registers the user
        if (checkUser(user, memoryUserDAO)){
            throw new AlreadyTakenException("username taken");
        }else{
            memoryUserDAO.addUser(user, pass, email);
            return memoryAuthDAO.loginUser(user);
        }
    }

    public static String loginUser(String username, String password, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO){
        if (checkUser(username, memoryUserDAO)){
            if(memoryUserDAO.checkPass(username, password)){
                return memoryAuthDAO.loginUser(username);
            }else{
                throw new PasswordsDontMatchException("password is incorrect");
            }
        }else{
            throw new UserDoesNotExistException("user does not exist");
        }
    }

    public static Boolean checkUser(String user, MemoryUserDAO memoryUserDAO){
        return memoryUserDAO.checkForUser(user);
    }
}
