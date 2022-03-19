package ece651.riskgame.shared;

import java.util.Map;

/**
 * Implemented by World and GameInfo
 */
public interface Actable {

    /**
     * @return Board topology relationship between territories
     */
    Board getBoard();

    /**
     * @return Map from color to Clan
     */
    Map<String, Clan> getClans();

    /**
     * @param name of territory
     * @return Clan who occupies the territory (color)
     */
    String getTerritoryOwnership(String name);
}
