package cz.technecium.openkarvinadataservices.controller;

import java.util.List;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.RepositoryType;
import cz.technecium.openkarvinadataservices.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author Martin.Sobek
 */
@RestController
public class PlayerDataController {

    @Autowired
    PlayerService playerService;

    @RequestMapping("/players")
    public List<Player> findPlayersByName(@RequestParam(value = "repository") RepositoryType repository, @RequestParam(value = "name") String name) {
        return playerService.findPlayersByName(repository, name);
    }


    @RequestMapping("/refresh")
    public void refreshRepositories(){
        playerService.refreshRepositories();
    }
}
