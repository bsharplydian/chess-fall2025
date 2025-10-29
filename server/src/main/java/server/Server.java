package server;

import dataaccess.*;
import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;
import service.handlers.*;

public class Server {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    UserService userService;
    GameService gameService;
    UserJsonHandler userHandler;
    GameJsonHandler gameHandler;
    private final Javalin javalin;

    public Server() {
        try {
            this.authDAO = new SQLAuthDAO();
            this.userDAO = new SQLUserDAO();
            this.gameDAO = new SQLGameDAO();
            this.userService = new UserService(authDAO, userDAO);
            this.gameService = new GameService(authDAO, gameDAO);
            this.userHandler = new UserJsonHandler(userService);
            this.gameHandler = new GameJsonHandler(gameService);
        } catch (DataAccessException e) {
            throw new RuntimeException(String.format("Error: unable to start server: %s%n", e.getMessage()));
        }

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
        } catch (DataAccessException e) {
            context.status(500);
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
        } catch (DataAccessException e) {
            context.status(500);
            context.json(buildErrorMessage(e));
        }
    }
    private void logout(Context context) {
        try {
            userHandler.logout(context);
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        } catch (DataAccessException e) {
            context.status(500);
            context.json(buildErrorMessage(e));
        }
    }
    private void listGames(Context context) {
        try {
            context.json(gameHandler.listGames(context));
        } catch (UnauthorizedException e) {
            context.status(401);
            context.json(buildErrorMessage(e));
        } catch (DataAccessException e) {
            context.status(500);
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
        } catch (DataAccessException e) {
            context.status(500);
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
        } catch (DataAccessException e) {
            context.status(500);
            context.json(buildErrorMessage(e));
        }
    }
    private void clear(Context context) {
        try {
            userService.clear();
            gameService.clear();
        } catch (DataAccessException e) {
            context.status(500);
            context.json(buildErrorMessage(e));
        }
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
