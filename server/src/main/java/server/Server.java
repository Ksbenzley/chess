package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //register
        Spark.post("/user", handler::register);
        //clear
        Spark.delete("/db", handler::clear);
//        //Login
        Spark.post("/session", handler::login);
//        //Logout
        Spark.delete("/session", handler::logout);
//        //List Games
        Spark.get("/game", handler::listGames);
//        //Create Game
        Spark.post("/game", handler::createGame);
//        //Join Game
        Spark.put("/game", handler::joinGame);


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
