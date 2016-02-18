package cz.technecium.openkarvinadataservices.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author Martin.Sobek
 */
@Service
public class PlayerService {

    @Autowired
    Map<String, PlayerRepository> repos = new HashMap<>();

    public List<Player> findPlayersByName(final String repository, String name) {
        return repos.get(repository).findPlayersByName(name);
    }

    public Player findPlayersById(final String repository, final long id) {
        return repos.get(repository).findPlayerById(id);
    }

    public void refreshRepositories() {
        repos.values().forEach(PlayerRepository::refreshRepository);
    }
}
