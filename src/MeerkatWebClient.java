import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

import com.biotools.meerkat.Action;
import com.biotools.meerkat.Card;
import com.biotools.meerkat.GameInfo;
import com.biotools.meerkat.util.Preferences;


public class MeerkatWebClient implements com.biotools.meerkat.Player {
    private GameInfo gameInfo;
    private Preferences prefs;

    private String serverAddress;

    public MeerkatWebClient () {
        serverAddress = prefs.getPreference ("SERVER_ADDRESS", "http://localhost:9000/");
    }


    public JPanel getSettingsPanel () {
        JPanel panel = new JPanel ();

        panel.add (new JLabel ("Bot server address:"));

        final JTextField addressTextField = new JTextField (serverAddress);

        addressTextField.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                prefs.setPreference ("SERVER_ADDRESS", addressTextField.getText ());
            }
        });

        panel.add (addressTextField);

        return panel;
    }


    private void postToServer (String urlPostfix, String data) {
        URL serverUrl;

		try {			
			HttpURLConnection connection;
			serverUrl = new URL (serverAddress + urlPostfix);
			connection = (HttpURLConnection) serverUrl.openConnection ();
			connection.setRequestMethod ("POST");

			OutputStream stream = connection.getOutputStream();
			PrintWriter writer = new PrintWriter (stream);
			writer.write (data);
			stream.close();
            
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}   
    }
    
    
    private static String getStringFromDocument(Document doc) {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
        
        return writer.toString();
    }
    
    private static Document newDocument () {
		try {
			DocumentBuilder builder;
			builder = DocumentBuilderFactory.newInstance ().newDocumentBuilder ();
	        return builder.newDocument ();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    
    public void holeCards (Card c1, Card c2, int seat) {
        Document document = newDocument ();
        
        Element root = document.createElement("holecards");
        document.appendChild(root);
        
        Element cards = document.createElement("cards");
        root.appendChild(cards);
        
        Element card1 = document.createElement("card");
        card1.setTextContent(c1.toString());
        cards.appendChild(card1);
        
        Element card2 = document.createElement("card");
        card1.setTextContent(c2.toString());
        cards.appendChild(card2);
        
        Element playerSeat = document.createElement("seat");
        playerSeat.setTextContent(String.valueOf(seat));
        root.appendChild(playerSeat);
        
        postToServer ("holecards", getStringFromDocument (document));
    }


    public Action getAction () {
        return new Action (Action.FOLD, 0, 0);
    }


    public Preferences getPreferences () {
        return prefs;
    }


    public void init (Preferences playerPrefs) {
        this.prefs = playerPrefs;
    }


    public void actionEvent (int pos, Action act) {

    }

    public void dealHoleCardsEvent () {}

    public void gameOverEvent () {}

    public void gameStartEvent (GameInfo gameInfo) {
    	this.gameInfo = gameInfo;
    }

    public void gameStateChanged () {}

    public void showdownEvent (int seat, Card c1, Card c2) {}

    public void stageEvent (int stage) {}

    public void winEvent (int pos, double amount, String handName) {}

}
