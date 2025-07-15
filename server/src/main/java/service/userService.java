package service;
import dataaccess.*;

public class userService {
    static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    public static String registerUser(String user, String pass, String email){
        //checks if username is already taken -else- registers the user
        if (checkUser(user)){
            throw new AlreadyTakenException("username taken");
        }else{
            return memoryUserDAO.addUser(user, pass, email);
        }
    }

    public static String loginUser(String username, String password, String authToken){
        if (checkUser(username)){
            if(memoryUserDAO.checkPass(username, password)){
                if(memoryUserDAO.checkAuthToken(username, authToken)){
                    return memoryUserDAO.loginUser(username);
                }else{
                    throw new NotAuthorizedException("user is unauthorized");
                }
            }else{
                throw new PasswordsDontMatchException("password is incorrect");
            }
        }else{
            throw new UserDoesNotExistException("user does not exist");
        }
    }

    public static Boolean checkUser(String user){
        return memoryUserDAO.checkForUser(user);
    }
}
