import static org.junit.Assert.*;

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
		assertEquals("0", server.getRaiseAmountString());
	}
}
