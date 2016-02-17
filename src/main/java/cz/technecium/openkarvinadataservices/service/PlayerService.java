package cz.technecium.openkarvinadataservices.service;

import java.util.List;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.RepositoryType;
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
    PlayerRepository czechRatingListRepository;

    @Autowired
    PlayerRepository fideRatingListRepository;


    public List<Player> findPlayersByName(final RepositoryType repository, String name) {
        if (repository == RepositoryType.CZE) {
            return czechRatingListRepository.findPlayersByName(name);
        } else {
            return fideRatingListRepository.findPlayersByName(name);
        }
    }

    public void refreshRepositories() {
        czechRatingListRepository.refreshRepository();
        fideRatingListRepository.refreshRepository();
    }
}
