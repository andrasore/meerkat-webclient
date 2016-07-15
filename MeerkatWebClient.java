import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biotools.meerkat.Action;
import com.biotools.meerkat.Card;
import com.biotools.meerkat.GameInfo;
import com.biotools.meerkat.util.Preferences;


public class MeerkatWebClient implements com.biotools.meerkat.Player {
    private int ourSeatNumber;
    private Card c1, c2;
    private GameInfo gameInfo;
    private Preferences prefs;


    public void holeCards (Card c1, Card c2, int seat) {
        this.c1 = c1;
        this.c2 = c2;
        this.ourSeatNumber = seat;
    }


    public JPanel getSettingsPanel () {
        JPanel panel = new JPanel ();

        panel.add (new JLabel ("Bot server address:"));

        String addressText = prefs.getPreference ("SERVER_ADDRESS", "http://localhost:9000/");

        final JTextField addressTextField = new JTextField (addressText);

        addressTextField.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                prefs.setPreference ("SERVER_ADDRESS", addressTextField.getText ());
            }
        });

        panel.add (addressTextField);

        return panel;
    }


    public boolean isDebugOn () {
        return prefs.getBooleanPreference ("DEBUG", false);
    }


    public void debugPrintln (String str) {
        if (isDebugOn ()) {
            System.out.println ("MeerkatWebClient: " + str);
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


    public void gameStartEvent (GameInfo gameInfonfo) {
        this.gameInfo = gameInfonfo;
    }


    public void stageEvent (int stage) {}

    public void showdownEvent (int seat, Card c1, Card c2) {}

    public void dealHoleCardsEvent () {}

    public void actionEvent (int pos, Action act) {}

    public void gameStateChanged () {}

    public void winEvent (int pos, double amount, String handName) {}

    public void gameOverEvent () {}

}
