import static org.junit.Assert.*;

import org.junit.Test;

public class MeerkatWebClientTest {

	@Test
	public void testPostToServer() {
		MeerkatWebClient.postToServer("http://localhost:5000/holecards", "asdf");
	}

	@Test
	public void testGetStringFromDocument() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetActionStringFromServer () {
		String data = MeerkatWebClient.getActionStringFromServer("http://localhost:5000/getaction");
		assertEquals(data, "FOLD");
	}
}
