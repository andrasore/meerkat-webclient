import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class MeerkatWebServerConnection {

    private String serverAddress;

    private String cachedActionType; //these are read once before set to null
    private String cachedRaiseAmount;

    private ArrayList<PlayerCards> shownCards;
    private ArrayList<Winning>     playersWon; //these are sent all at once

    public static class Player {
        public String name;
        public double stack;
        public int    seat;

        public Player (String name, double stack, int seat) {
            this.name = name;
            this.stack = stack;
            this.seat = seat;
        }
    }

    private class PlayerCards {
        public String card1;
        public String card2;
        public int seat;

        public PlayerCards(String card1, String card2, int seat) {
            this.card1 = card1;
            this.card2 = card2;
            this.seat = seat;
        }
    }

    private class Winning {
        public int seat;
        public double amount;

        public Winning(int seat, double amount) {
            this.seat = seat;
            this.amount = amount;
        }
    }


    public MeerkatWebServerConnection (String serverAddress) {
        this.serverAddress = serverAddress;
        cachedActionType   = null;
        cachedRaiseAmount  = null;

        shownCards = new ArrayList<PlayerCards> ();
        playersWon = new ArrayList<Winning> ();
    }


    private void postToServer(String urlPostfix, String data) {
        URL serverUrl;

        try {
            HttpURLConnection connection;
            serverUrl = new URL(serverAddress + urlPostfix);
            connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setDoOutput(true);

            OutputStream stream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(stream);
            writer.write(data);
            writer.close();

            connection.getResponseCode();
        } catch(MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "URL is invalid!");
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void sendNewGame (List<Player> players, int buttonSeat) {
        Document document = newDocument();

        Element rootElem = document.createElement("newgame");
        document.appendChild(rootElem);

        Element buttonSeatElem = document.createElement("buttonseat");
        buttonSeatElem.setTextContent(String.valueOf(buttonSeat));
        rootElem.appendChild(buttonSeatElem);

        Element playersElem = document.createElement("players");
        rootElem.appendChild(playersElem);

        for (Player p : players) {
            Element playerElem = document.createElement("player");
            playersElem.appendChild(playerElem);

            Element nameElem = document.createElement("name");
            nameElem.setTextContent(String.valueOf(p.name));
            playerElem.appendChild(nameElem);

            Element stackElem = document.createElement("stack");
            stackElem.setTextContent(String.valueOf(p.stack));
            playerElem.appendChild(stackElem);

            Element seatElem = document.createElement("seat");
            seatElem.setTextContent(String.valueOf(p.seat));
            playerElem.appendChild(seatElem);
        }

        postToServer("newgame", getStringFromDocument(document));
    }



    public void sendHoleCards(String card1, String card2, int seat) {
        Document document = newDocument();

        Element rootElem = document.createElement("holecards");
        document.appendChild(rootElem);

        Element cardsElem = document.createElement("cards");
        rootElem.appendChild(cardsElem);

        Element card1Elem = document.createElement("card");
        card1Elem.setTextContent(card1);
        cardsElem.appendChild(card1Elem);

        Element card2elem = document.createElement("card");
        card2elem.setTextContent(card2);
        cardsElem.appendChild(card2elem);

        Element seatElem = document.createElement("seat");
        seatElem.setTextContent(String.valueOf(seat));
        rootElem.appendChild(seatElem);

        postToServer("holecards", getStringFromDocument(document));
    }


    public void sendActionEvent (int seat, String action, double amount) {
        Document document = newDocument();

        Element rootElem = document.createElement("action");
        document.appendChild(rootElem);

        Element seatElem = document.createElement("seat");
        seatElem.setTextContent(String.valueOf(seat));
        rootElem.appendChild(seatElem);

        Element actionElem = document.createElement("type");
        actionElem.setTextContent(action);
        rootElem.appendChild(actionElem);

        if (amount > 0) {
            Element raiseElem = document.createElement("amount");
            raiseElem.setTextContent(String.valueOf(amount));
            rootElem.appendChild(raiseElem);
        }

        postToServer("action", getStringFromDocument(document));
    }


    private Document getGameoverDocument () {
        Document document = newDocument();

        Element rootElem = document.createElement("gameover");
        document.appendChild(rootElem);

        for (Winning w : playersWon) {
            Element winningElem = document.createElement("winning");
            rootElem.appendChild(winningElem);

            Element amountElem = document.createElement("amount");
            amountElem.setTextContent(String.valueOf(w.amount));
            winningElem.appendChild(amountElem);

            Element seatElem = document.createElement("seat");
            seatElem.setTextContent(String.valueOf(w.seat));
            winningElem.appendChild(seatElem);
        }

        return document;
    }

    private Document getShowdownDocument () {
        Document document = newDocument();

        Element rootElem = document.createElement("showdown");
        document.appendChild(rootElem);

        for (PlayerCards pc : shownCards) {
            Element cardsElem = document.createElement("cards");
            rootElem.appendChild(cardsElem);

            Element card1Elem = document.createElement("card");
            card1Elem.setTextContent(pc.card1);
            cardsElem.appendChild(card1Elem);

            Element card2elem = document.createElement("card");
            card2elem.setTextContent(pc.card2);
            cardsElem.appendChild(card2elem);

            Element seatElem = document.createElement("seat");
            seatElem.setTextContent(String.valueOf(pc.seat));
            cardsElem.appendChild(seatElem);
        }

        return document;
    }


    public void sendGameOver () {
        if (!shownCards.isEmpty()) {
            Document showdownDocument = getShowdownDocument ();
            postToServer("showdown", getStringFromDocument(showdownDocument));
            shownCards.clear();
        }

        if (!playersWon.isEmpty()) {
            Document gameoverDocument = getGameoverDocument ();
            postToServer("gameover", getStringFromDocument(gameoverDocument));
            playersWon.clear();
        }
    }


    public void sendWinEvent(int pos, double amount) {
        playersWon.add(new Winning (pos, amount));
    }


    public void sendShowdownEvent (String card1, String card2, int seat) {
        shownCards.add(new PlayerCards(card1, card2, seat));
    }


    public void sendBoardCards(String stage, ArrayList<String> boardCards) {
        Document document = newDocument();

        Element rootElem = document.createElement("board");
        document.appendChild(rootElem);

        Element cardsElem = document.createElement("cards");
        rootElem.appendChild(cardsElem);

        for (String c : boardCards) {
            Element cardElem = document.createElement("card");
            cardElem.setTextContent(c);
            cardsElem.appendChild(cardElem);
        }

        Element stageElem = document.createElement("stage");
        stageElem.setTextContent(stage);
        rootElem.appendChild(stageElem);

        postToServer("board", getStringFromDocument(document));
    }


    private void getActionFromServer() {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(serverAddress + "action");

            cachedActionType = document.getDocumentElement()
                .getElementsByTagName("type")
                .item(0)
                .getTextContent();

            if (cachedActionType.equals("raise")) {
                cachedRaiseAmount = document.getDocumentElement()
                    .getElementsByTagName("amount")
                    .item(0)
                    .getTextContent();
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String getActionTypeString() {
        if (cachedActionType == null) {
            getActionFromServer ();
        }
        String returnString = cachedActionType;
        cachedActionType = null;
        return returnString;
    }


    public String getRaiseAmountString() {
        if (cachedRaiseAmount == null) {
            getActionFromServer ();
        }
        String returnString = cachedRaiseAmount;
        cachedRaiseAmount = null;
        return returnString;
    }


    public static String getStringFromDocument(Document doc) {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
        } catch(TransformerException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    public static Document newDocument() {
        try {
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }


}
