package ui;

import java.util.Set;

public abstract class Validator {
    public enum Command {
        REGISTER,
        LOGIN,
        LOGOUT,
        CREATE,
        LIST,
        JOIN,
        OBSERVE,
        DRAW,
        LEAVE,
        MOVE,
        RESIGN,
        HIGHLIGHT,
        HELP,
        QUIT
    }
    Command getCommand(String command) throws SyntaxException{
        return switch(command) {
            case "register" -> Command.REGISTER;
            case "login" -> Command.LOGIN;
            case "logout" -> Command.LOGOUT;
            case "create" -> Command.CREATE;
            case "list" -> Command.LIST;
            case "join" -> Command.JOIN;
            case "observe" -> Command.OBSERVE;
            case "draw" -> Command.DRAW;
            case "leave" -> Command.LEAVE;
            case "move" -> Command.MOVE;
            case "resign" -> Command.RESIGN;
            case "hl" -> Command.HIGHLIGHT;
            case "help" -> Command.HELP;
            case "quit" -> Command.QUIT;

            default -> throw new SyntaxException("Unexpected value: " + command); // add an actual syntax exception
        };
    }
    abstract Command validate(String[] params) throws Exception;
    abstract Set<Command> getLegalCommands();
}
