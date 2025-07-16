package service;
import model.*;
import dataaccess.*;
import java.util.ArrayList;

public class GameService {

    public static Integer createGame(String authToken, String gameName, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        if(memoryAuthDAO.checkUserAuth(authToken)){
            if(memoryGameDAO.gameExists(gameName)){
                throw new BadRequestException("Error: bad request, game name taken");
            }else{
                return memoryGameDAO.addGame(memoryGameDAO.createGameID(), gameName);
            }
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }

    public static ArrayList<GameData> listGames(String authToken, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        if(memoryAuthDAO.checkUserAuth(authToken)){
            return memoryGameDAO.getList();
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }
}
