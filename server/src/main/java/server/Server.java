package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //register
        Spark.post("/user", Handler::register);
        //clear
        Spark.delete("/db", Handler::clear);
//        //Login
        Spark.post("/session", Handler::login);
//        //Logout
        Spark.delete("/session", Handler::logout);
//        //List Games
        Spark.get("/game", Handler::listGames);
//        //Create Game
        Spark.post("/game", Handler::createGame);
//        //Join Game
        Spark.put("/game", Handler::joinGame);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
