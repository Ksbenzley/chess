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

    public static Boolean checkUser(String user){
        return memoryUserDAO.checkForUser(user);
    }
}
