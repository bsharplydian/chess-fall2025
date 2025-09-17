package service.results;

import java.util.Collection;

public record ListGamesResult(Collection<chess.ChessGame> games) {
}
