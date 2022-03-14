package ece651.riskgame.shared;

import java.util.Map;

public interface Actable {

    Board getBoard();

    Map<String, Clan> getClans();
}
