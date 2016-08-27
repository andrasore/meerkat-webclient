import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biotools.meerkat.Action;
import com.biotools.meerkat.Card;
import com.biotools.meerkat.GameInfo;
import com.biotools.meerkat.PlayerInfo;
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


    public void holeCards(Card c1, Card c2, int seat) {
        server.sendHoleCards(c1.toString(), c2.toString(), seat);
        this.seat = seat;
    }


    public Action getAction() {
        int action = ActionConverter.stringToInt(server.getActionTypeString());
        int raiseAmount = 0;

        if(action == Action.RAISE) {
            raiseAmount = Integer.parseInt(server.getRaiseAmountString());
        }

        return Action.getAction(action, gameInfo.getAmountToCall(seat), raiseAmount);
    }


    public void actionEvent(int pos, Action action) {
        double amount = 0;
        int index = action.getActionIndex(); //this will only leave fold/call/raise events,
                                             //and convert everything else to invalid
        if (index != Action.INVALID) {
        	
        	if (index == Action.RAISE) {
                amount = action.getAmount();
            }
        	
        	String actionString = ActionConverter.intToString(index);
        	server.sendActionEvent (pos, actionString, amount);
        }
    }


    public void gameOverEvent() {

    }

    public void gameStartEvent(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        ArrayList<MeerkatWebServerConnection.Player> players 
        	= new ArrayList<MeerkatWebServerConnection.Player>();

        for(int i = 0; i < 10; i++) {
        	if (!gameInfo.inGame(i)) {
        		continue;
        	}
        	
        	PlayerInfo player  = gameInfo.getPlayer(i);
        	
        	String playerName  = player.getName(); 
        	double playerStack = player.getBankRollAtStartOfHand();
        	
            players.add(new MeerkatWebServerConnection.Player(playerName, playerStack, i));
        }
        
        server.sendPlayerInfo(players);
    }

    public void gameStateChanged() {

    }

    public void showdownEvent(int seat, Card c1, Card c2) {

    }

    public void stageEvent(int stage) {

    }


    public void winEvent(int pos, double amount, String handName) {

    }


    public void dealHoleCardsEvent() {

    }


}
