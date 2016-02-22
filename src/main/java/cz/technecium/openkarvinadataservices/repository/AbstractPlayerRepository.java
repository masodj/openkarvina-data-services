package cz.technecium.openkarvinadataservices.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import cz.technecium.openkarvinadataservices.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author Martin.Sobek
 */
public abstract class AbstractPlayerRepository implements PlayerRepository {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractPlayerRepository.class);
    protected final List<Player> players = new ArrayList<>();
    protected final Resource resource;
    protected final Comparator<Player> byName = Comparator.comparing(Player::getName);
    protected final Object locker = new Object();

    public AbstractPlayerRepository(Resource resource) {
        this.resource = resource;
        readData();
    }

    private void readData() {
        try {

            logger.info(String.format("Reading players data from resource %s", resource.getFilename()));

            readResource();

            logger.info(String.format("Reading players data from resource %s finished. %d players successfully red.", resource.getFilename(), players.size()));
        } catch (Exception e){
            logger.error("Exception during reading resource", e);
        }
    }

    protected boolean startsWithIgnoreCase(String token, String name) {
        return name.toLowerCase().startsWith(token.toLowerCase());
    }

    @Override
    public abstract Player findPlayerById(final long id);

    @Override
    public List<Player> findPlayersByName(String name) {
        List<Player> filteredPlayers = players.stream()
                .filter(player -> startsWithIgnoreCase(name, player.getName()))
                .collect(Collectors.toList());
        filteredPlayers.sort(byName);
        return filteredPlayers;
    }

    @Override
    public  void refreshRepository() {
        synchronized(locker) {
            players.clear();
            readData();
        }
    }

    protected abstract void readResource() throws IOException, ParserConfigurationException, SAXException;
}
