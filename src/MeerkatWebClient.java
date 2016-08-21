import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

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


    private boolean isDebugOn () {
        return prefs.getBooleanPreference ("DEBUG", false);
    }


    private void debugPrintln (String str) {
        if (isDebugOn ()) {
            System.out.println ("MeerkatWebClient: " + str);
        }
    }


    public void holeCards (Card c1, Card c2, int seat) {
        URL serverUrl;

		try {			
			HttpURLConnection connection;
			serverUrl = new URL (serverAddress + "holecards");
			connection = (HttpURLConnection) serverUrl.openConnection ();
			connection.setRequestMethod ("POST");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

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

    public void gameStartEvent (GameInfo gameInfonfo) { this.gameInfo = gameInfo; }

    public void gameStateChanged () {}

    public void showdownEvent (int seat, Card c1, Card c2) {}

    public void stageEvent (int stage) {}

    public void winEvent (int pos, double amount, String handName) {}

}
