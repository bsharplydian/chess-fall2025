package ui;

import java.util.Set;

public class ValidatorPreLogin extends Validator{
    @Override
    public Command validate(String[] params) throws SyntaxException {
        Command command = getCommand(params[0]);
        switch(command) {
            case REGISTER -> validateRegister(params);
            case LOGIN -> validateLogin(params);
        }
       return command;
    }

    private void validateLogin(String[] params) {
        if(params.length != 3) {
            throw new SyntaxException("Usage: login <name> <pass>");
        }
    }

    private void validateRegister(String[] params) {
        if(params.length != 4) {
            throw new SyntaxException("Usage: register <name> <pass> <email>");
        }
    }

    @Override
    Set<Command> getLegalCommands() {
        return Set.of(Command.LOGIN, Command.REGISTER);
    }
}
