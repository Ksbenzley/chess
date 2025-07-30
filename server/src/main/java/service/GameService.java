package service;
import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import model.GameData;

import java.util.ArrayList;


public class GameService {

    public static void joinGame(String authToken, String playerColor, int gameID, AuthDAO memoryAuthDAO, GameDAO memoryGameDAO)
    throws DataAccessException {
        String userName = memoryAuthDAO.getUser(authToken);
        if(playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))){
            throw new BadRequestException("Error: bad request");
        }
        if(memoryAuthDAO.checkUserAuth(authToken)){
            if(memoryGameDAO.checkGameID(gameID)){
                if(memoryGameDAO.checkPlayerColor(playerColor, gameID)){
                    memoryGameDAO.setPlayerColor(playerColor, gameID, userName);
                }else{
                    throw new AlreadyTakenException(("Error: color already taken"));
                }
            }else{
                throw new BadRequestException("Error: bad request");
            }
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }

    public static Integer createGame(String authToken, String gameName, AuthDAO memoryAuthDAO, GameDAO memoryGameDAO) throws DataAccessException{
        if(gameName == null){
            throw new BadRequestException("Error: bad request");
        }
        if(memoryAuthDAO.checkUserAuth(authToken)){
            if(memoryGameDAO.gameExists(gameName)){
                throw new BadRequestException("Error: bad request");
            }else{
                return memoryGameDAO.addGame(memoryGameDAO.createGameID(), gameName);
            }
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }

    public static ArrayList<GameData> listGames(String authToken, AuthDAO memoryAuthDAO, GameDAO memoryGameDAO) throws DataAccessException{
        if(memoryAuthDAO.checkUserAuth(authToken)){
            return memoryGameDAO.getList();
        }else{
            throw new NotAuthorizedException("Error: unauthorized");
        }
    }
}
