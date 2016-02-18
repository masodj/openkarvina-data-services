package cz.technecium.openkarvinadataservices.repository;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.PlayerIdentifier;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author Martin.Sobek
 */
public class FideRatingListRepository extends AbstractPlayerRepository implements PlayerRepository {

    public FideRatingListRepository(Resource resource) {
        super(resource);
    }

    protected void readResource() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(resource.getInputStream());
        NodeList nodeList1 = doc.getElementsByTagName("playerslist");
        Node node = nodeList1.item(0);
        NodeList nodeList2 = node.getChildNodes();

        for (int i = 0; i < nodeList2.getLength(); i++) {
            Node playerNode = nodeList2.item(i);

            if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) playerNode;
                players.add(mapToPlayer(element));
            }
        }
    }

    private Player mapToPlayer(final Element element) {
        PlayerIdentifier identifier = new PlayerIdentifier();
        Player player = new Player();
        player.setPlayerIdentifier(identifier);

        identifier.setFideId(Long.parseLong(element.getElementsByTagName("fideid").item(0).getTextContent()));
        player.setName(element.getElementsByTagName("name").item(0).getTextContent());
        player.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
        player.setFideRating(Integer.parseInt(element.getElementsByTagName("rating").item(0).getTextContent()));
        player.setNationality(element.getElementsByTagName("country").item(0).getTextContent());
        return player;
    }
}
