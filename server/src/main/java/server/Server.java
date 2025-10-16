package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;
import service.handlers.*;

public class Server {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    UserService userService = new UserService(authDAO, userDAO);
    GameService gameService = new GameService(authDAO, gameDAO);
    UserJsonHandler userHandler = new UserJsonHandler(userService);
    GameJsonHandler gameHandler = new GameJsonHandler(gameService);
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
            context.json(userHandler.register(context));
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
            context.json(userHandler.login(context));
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
            userHandler.logout(context);
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        }
    }
    private void listGames(Context context) {
        try {
            context.json(gameHandler.listGames(context));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        }
    }
    private void createGame(Context context) {
        try {
            context.json(gameHandler.createGame(context));
        } catch (BadRequestException e) {
            context.status(400);
            context.json(buildErrorMessage(e));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        }
    }
    private void joinGame(Context context) {
        try {
            gameHandler.joinGame(context);
        } catch(BadRequestException e) {
            context.status(400);
            context.json(buildErrorMessage(e));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        } catch (ForbiddenException e) {
            context.status(403);
            context.json(buildErrorMessage(e));
        }
    }
    private void clear(Context context) {
        userService.clear();
        gameService.clear();
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
