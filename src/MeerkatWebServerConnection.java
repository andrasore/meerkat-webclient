import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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


    public MeerkatWebServerConnection (String serverAddress) {
        this.serverAddress = serverAddress;
        cachedActionType   = null;
        cachedRaiseAmount  = null;
    }


    private void postToServer(String urlPostfix, String data) {
        URL serverUrl;

        try {
            HttpURLConnection connection;
            serverUrl = new URL(serverAddress + urlPostfix);
            connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml");
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


    public void sendHoleCards(String c1, String c2, int seat) {
        Document document = newDocument();

        Element root = document.createElement("holecards");
        document.appendChild(root);

        Element cards = document.createElement("cards");
        root.appendChild(cards);

        Element card1 = document.createElement("card");
        card1.setTextContent(c1);
        cards.appendChild(card1);

        Element card2 = document.createElement("card");
        card2.setTextContent(c2);
        cards.appendChild(card2);

        Element playerSeat = document.createElement("seat");
        playerSeat.setTextContent(String.valueOf(seat));
        root.appendChild(playerSeat);

        postToServer(serverAddress + "holecards", getStringFromDocument(document));
    }


    private void getActionFromServer() {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(serverAddress + "getaction");

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
