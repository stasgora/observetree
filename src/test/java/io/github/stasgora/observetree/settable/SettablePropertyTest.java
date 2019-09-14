package io.github.stasgora.observetree.settable;

import io.github.stasgora.observetree.SettableProperty;
import io.github.stasgora.observetree.TestBase;
import org.junit.Before;
import org.junit.Test;

public class SettablePropertyTest extends TestBase {
	private SettableProperty<Integer> settableInt;

	@Before
	public void prepareObjects() {
		settableInt = new SettableProperty<>(0);
		listener = mockListener(() -> {});
	}

	@Test
	public void whenSettableObjectIsSet_theListenersGetsCalled() {
		settableInt.addListener(listener);

		settableInt.set(VALUE_TO_SET);
		settableInt.notifyListeners();

		verifyListenerCalled(listener, 1);
	}
}
