package ui;

import serverfacade.HttpResponseException;

import java.util.Set;

import static ui.Validator.Command.*;

public class PostLoginValidator extends Validator {
    @Override
    Command validate(String[] params) throws SyntaxException {
        Command command = getCommand(params[0]);
        switch(command) {
            case LOGOUT -> validateLogout(params);
            case CREATE -> validateCreate(params);
            case LIST -> validateList(params);
            case JOIN -> validateJoin(params);
        }
        return command;
    }

    public void validateObserve(String[] params, int gameListSize) {
        if(params.length != 2) {
            throw new SyntaxException("Usage: observe [id]");
        }
        try{
            Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            throw new SyntaxException("Invalid game ID");
        }
        if(Integer.parseInt(params[1]) > gameListSize || Integer.parseInt(params[1]) <= 0) {
            throw new SyntaxException("Invalid game ID");
        }
    }

    public void validateJoin(String[] params) {

    }

    public void validateList(String[] params) {

    }

    public void validateCreate(String[] params) {

    }

    public void validateLogout(String[] params) {

    }

    @Override
    Set<Command> getLegalCommands() {
        return Set.of(HELP, LOGOUT, CREATE, LIST, JOIN, OBSERVE);
    }
}
