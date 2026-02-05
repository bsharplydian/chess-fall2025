package ui;

public class PostLoginValidator extends Validator {
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

    public void validateJoin(String[] params, int gameListSize) {
        if(params.length != 3) {
            throw new SyntaxException("Usage: join [id] [WHITE|BLACK]");
        }
        try{
            Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            throw new SyntaxException("Invalid game ID");
        }
        if(Integer.parseInt(params[1]) > gameListSize || Integer.parseInt(params[1]) <= 0) {
            throw new SyntaxException("Invalid game ID");
        }
        switch(params[2].toUpperCase()) {
            case "WHITE", "W", "BLACK", "B" -> {}
            default -> throw new SyntaxException("Usage: join [id] [WHITE|BLACK]");
        };
    }

    public void validateList(String[] params) {
        if(params.length != 1) {
            throw new SyntaxException("Usage: list");
        }
    }

    public void validateCreate(String[] params) {
        if(params.length != 2) {
            throw new SyntaxException("Usage: create [name]");
        }
        try{
            Integer.parseInt(params[1]);
            throw new SyntaxException("Game names cannot be numerals");
        } catch (NumberFormatException e) {
            // string was not a number
        }
    }

    public void validateLogout(String[] params) {
        if(params.length != 1) {
            throw new SyntaxException("Usage: logout");
        }
    }
}
