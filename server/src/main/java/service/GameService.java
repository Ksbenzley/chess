package service;
import dataaccess.*;

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

    public static CharSequence listGames(String authToken, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
        if(memoryAuthDAO.checkUserAuth(authToken)){
            return (CharSequence) memoryGameDAO.getList();
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }
}
