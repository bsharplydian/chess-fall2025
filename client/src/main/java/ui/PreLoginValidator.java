package ui;

public class PreLoginValidator extends Validator{
    public void validateLogin(String[] params) {
        if(params.length != 3) {
            throw new SyntaxException("Usage: login <name> <pass>");
        }
    }

    public void validateRegister(String[] params) {
        if(params.length != 4) {
            throw new SyntaxException("Usage: register <name> <pass> <email>");
        }
    }
}
