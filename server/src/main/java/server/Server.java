package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;
import service.handlers.*;

public class Server {
    UserService userService = new UserService();
    UserJsonHandler userHandler = new UserJsonHandler(userService);
    GameJsonHandler gameHandler = new GameJsonHandler();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clear);
    }

    private void register(Context context) {
        context.json(userHandler.register(context.body()));
    }
    private void login(Context context) {

    }
    private void logout(Context context) {

    }
    private void listGames(Context context) {

    }
    private void createGame(Context context) {

    }
    private void joinGame(Context context) {

    }
    private void clear(Context context) {

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
