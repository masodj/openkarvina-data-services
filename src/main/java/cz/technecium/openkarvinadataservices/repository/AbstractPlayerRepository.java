package cz.technecium.openkarvinadataservices.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import cz.technecium.openkarvinadataservices.domain.Player;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author Martin.Sobek
 */
public abstract class AbstractPlayerRepository implements PlayerRepository {

    protected final List<Player> players = new ArrayList<>();
    protected final Resource resource;
    protected final Comparator<Player> byName = (p1, p2) -> p1.getName().compareTo(p2.getName());

    public AbstractPlayerRepository(Resource resource) {
        this.resource = resource;
        try {
            readResource();
        } catch (Exception e){
        }
    }

    protected boolean startsWithIgnoreCase(String token, String name) {
        return name.toLowerCase().startsWith(token.toLowerCase());
    }

    @Override
    public Player findPlayerById(final long id) {
        return players.stream()
                .filter(player -> player.getPlayerIdentifier().getCrId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Player> findPlayersByName(String name) {
        List<Player> filteredPlayers = players.stream()
                .filter(player -> startsWithIgnoreCase(name, player.getName()))
                .collect(Collectors.toList());
        filteredPlayers.sort(byName);
        return filteredPlayers;
    }

    @Override
    public void refreshRepository() {
        try {
            readResource();
        } catch (Exception e) {}
    }

    protected abstract void readResource() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException;
}