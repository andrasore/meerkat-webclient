import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class MeerkatWebServerConnectionTest {
	
	private MeerkatWebServerConnection server;
	
	@Before
	public void setUp() {
	    server = new MeerkatWebServerConnection ("http://localhost:5000/");
	}
	
	@Test
	public void testSendHoleCards() {
		server.sendHoleCards("AB", "CD", 1);
	}

	@Test
	public void testGetActionTypeString() {
		assertEquals("fold", server.getActionTypeString());
	}

	@Test
	public void testGetRaiseAmountString() {
		assertEquals("0", server.getRaiseAmountString()); //there is no way to test this nicely yet :c
	}
	
	@Test
	public void testSendActionEvent() {
		server.sendActionEvent(3, "Test", 3.0);
	}
	
	@Test
	public void testSendShowdownEvent() {
		server.sendShowdownEvent("AB", "CD", 1);
	}
	
	@Test
	public void testSendBoardCards() {
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("AB");
		cards.add("CD");
		cards.add("EF");
		
		server.sendBoardCards(cards);
	}
}
