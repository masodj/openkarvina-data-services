package cz.technecium.openkarvinadataservices.repository;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cz.technecium.openkarvinadataservices.domain.Player;
import cz.technecium.openkarvinadataservices.domain.PlayerIdentifier;
import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 *
 * @author Martin.Sobek
 */
public class SaxFideRatingListRepository extends AbstractPlayerRepository implements PlayerRepository {

    private Player currentPlayer;
    ;
    private PlayerIdentifier currPlayerIdentifier;

    public SaxFideRatingListRepository(final Resource resource) {
        super(resource);
    }

    @Override
    protected void readResource() throws IOException, ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            boolean fideid = false;
            boolean name = false;
            boolean title = false;
            boolean rating = false;
            boolean country = false;

            public void startElement(String uri, String localName, String qName,
                    Attributes attributes) throws SAXException {


                if (qName.equalsIgnoreCase("player")) {
                    currentPlayer = new Player();
                    currPlayerIdentifier = new PlayerIdentifier();
                    currentPlayer.setPlayerIdentifier(currPlayerIdentifier);
                }

                if (qName.equalsIgnoreCase("fideid")) {
                    fideid = true;
                }

                if (qName.equalsIgnoreCase("name")) {
                    name = true;
                }

                if (qName.equalsIgnoreCase("title")) {
                    title = true;
                }

                if (qName.equalsIgnoreCase("rating")) {
                    rating = true;
                }

                if (qName.equalsIgnoreCase("country")) {
                    country = true;
                }

            }

            public void endElement(String uri, String localName,
                    String qName) throws SAXException {
                if (qName.equalsIgnoreCase("player")) {
                    players.add(currentPlayer);
                }
            }

            public void characters(char ch[], int start, int length) throws SAXException {
                String parsed = new String(ch, start, length).trim();

                if (parsed.isEmpty()) {
                    return;
                }

                if (fideid) {
                    currPlayerIdentifier.setFideId(Long.parseLong(parsed));
                    fideid = false;
                }

                if (name) {
                    currentPlayer.setName(parsed);
                    name = false;
                }

                if (title) {
                    currentPlayer.setTitle(parsed);
                    title = false;
                }

                if (rating) {
                    currentPlayer.setFideRating(Integer.parseInt(parsed));
                    rating = false;
                }

                if (country) {
                    currentPlayer.setNationality(parsed);
                    country = false;
                }

            }

        };
        saxParser.parse(resource.getInputStream(), handler);
    }
}

