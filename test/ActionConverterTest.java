import static org.junit.Assert.*;

import org.junit.Test;

import com.biotools.meerkat.Action;

public class ActionConverterTest {

	@Test
	public void testToStringAction1() {
		Action action = Action.checkAction();
		assertEquals("check", ActionConverter.toString(action));
	}

	@Test
	public void testToStringAction2() {
		Action action = Action.raiseAction(1, 1);
		assertEquals("raise", ActionConverter.toString(action));
	}

}
