package cz.technecium.openkarvinadataservices.repository;

import java.util.List;

import cz.technecium.openkarvinadataservices.domain.Player;

/**
 *
 *
 * @author Martin.Sobek
 */
public interface PlayerRepository {

    Player findPlayerById(long id);

    List<Player> findPlayersByName(String name);

    void refreshRepository();
}
