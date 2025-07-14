package service;
import dataaccess.*;

public class userService {
    static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    public static void registerUser(String user, String pass, String email){
        if (checkUser(user) == true){
            //username already exists/taken
        }else{
            memoryUserDAO.addUser(user, pass, email);
        };
    }

    public static Boolean checkUser(String user){
        return memoryUserDAO.checkForUser(user);
    }
}
