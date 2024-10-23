package server;

import server.handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.post("/session", new LoginHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.get("/game", new ListGamesHandler());
        Spark.delete("/session", new LogoutHandler());
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
