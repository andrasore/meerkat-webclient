import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biotools.meerkat.Action;
import com.biotools.meerkat.Card;
import com.biotools.meerkat.GameInfo;
import com.biotools.meerkat.util.Preferences;


public class MeerkatWebClient implements com.biotools.meerkat.Player {
    private GameInfo gameInfo;
    private Preferences prefs;
    private MeerkatWebServerConnection server;
    
    private int seat;


    public void init(Preferences playerPrefs) { //this is called by PokerAcademy
        this.prefs = playerPrefs;
        String serverAddress = prefs.getPreference("SERVER_ADDRESS", "http://localhost:9000/");
        server = new MeerkatWebServerConnection(serverAddress);
    }


    public JPanel getSettingsPanel() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Bot server address:"));

        String serverAddress = prefs.getPreference("SERVER_ADDRESS", "http://localhost:9000/");
        final JTextField addressTextField = new JTextField(serverAddress);

        addressTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prefs.setPreference("SERVER_ADDRESS", addressTextField.getText());
                server = new MeerkatWebServerConnection(addressTextField.getText());
            }
        });

        panel.add(addressTextField);

        return panel;
    }


    public static  int actionStringToInt (String actionString) {
        if(actionString.equals("fold")) {
         return Action.FOLD;
        } else if(actionString.equals("check")) {
         return Action.CHECK;
        } else if(actionString.equals("raise")) {
         return Action.RAISE;
        }
        return Action.INVALID;
    }


    public void holeCards(Card c1, Card c2, int seat) {
        server.sendHoleCards(c1.toString(), c2.toString(), seat);
        this.seat = seat;
    }


    public Action getAction() {
        int action = actionStringToInt(server.getActionTypeString());
        int raiseAmount = 0;

        if(action == Action.RAISE) {
            raiseAmount = Integer.parseInt(server.getRaiseAmountString());
        }

        return Action.getAction(action, gameInfo.getAmountToCall(seat), raiseAmount);
    }


    public void actionEvent(int pos, Action act) {

    }


    public void gameOverEvent() {
    }

    public void gameStartEvent(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void gameStateChanged() {}

    public void showdownEvent(int seat, Card c1, Card c2) {}

    public void stageEvent(int stage) {}

    public void winEvent(int pos, double amount, String handName) {}


}
