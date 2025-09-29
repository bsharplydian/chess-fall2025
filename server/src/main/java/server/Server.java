package server;

import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;
import service.handlers.*;

public class Server {
    UserService userService = new UserService();
    GameService gameService = new GameService();
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
        try {
            context.json(userHandler.register(context.body()));
        } catch (ForbiddenException e) {
            context.status(403);
            context.json(buildErrorMessage(e));
        } catch (BadRequestException e) {
            context.status(400);
            context.json(buildErrorMessage(e));
        }
    }
    private void login(Context context) {
        try {
            context.json(userHandler.login(context.body()));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        } catch (BadRequestException e) {
            context.status(400);
            context.json(buildErrorMessage(e));
        }
    }
    private void logout(Context context) {
        try {
            userHandler.logout(combineAuthAndBody(context));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        }
    }
    private void listGames(Context context) {

    }
    private void createGame(Context context) {

    }
    private void joinGame(Context context) {

    }
    private void clear(Context context) {
        userService.clear();
        gameService.clear();
    }

    private String combineAuthAndBody(Context context) {
        context.header("authorization");
        if(context.body().isEmpty()) {
            return String.format("""
                {"authToken": "%s"}
                """, context.header("authorization"));
        }
        return String.format("""
                {"authToken": "%s", %s}
                """, context.header("authorization"), context.body().substring(1, context.body().length()-2));
    }
    private String buildErrorMessage(Exception e) {
        return String.format("""
                    {"message": "%s"}""", e.getMessage());
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
