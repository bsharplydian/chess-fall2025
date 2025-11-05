package model.results;

import model.SimpleGameData;

import java.util.Collection;

public record ListGamesResult(Collection<SimpleGameData> games) {
}
