package io.github.stasgora.observetree.settable;

import io.github.stasgora.observetree.SettableProperty;
import io.github.stasgora.observetree.TestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SettablePropertyTest extends TestBase {
	private SettableProperty<Integer> settableInt;

	@Before
	public void prepareObjects() {
		super.prepareObjects();
		settableInt = new SettableProperty<>(0);
	}

	@Test
	public void whenSettableObjectIsSet_theListenersGetsCalled() {
		settableInt.addListener(listener);

		settableInt.set(VALUE_TO_SET);
		settableInt.notifyListeners();

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenValueIsModified_itIsMarkedAsChanged() {
		settableInt.addListener(listener);

		settableInt.modify(val -> VALUE_TO_SET);

		assertEquals((int) settableInt.get(), VALUE_TO_SET);
		assertTrue(settableInt.isValueChanged());
	}

	@Test
	public void whenSettableObjectIsSetWithNotify_theListenersGetsCalled() {
		settableInt.addListener(listener);

		settableInt.setAndNotify(VALUE_TO_SET);

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenSettingDefaultValue_itCanBeRestored() {
		settableInt.set(VALUE_TO_SET);
		settableInt.saveAsDefaultValue();
		settableInt.set(0);

		assertEquals((int) settableInt.getDefaultValue(), VALUE_TO_SET);
		settableInt.resetToDefaultValue();
		assertEquals((int) settableInt.get(), VALUE_TO_SET);
	}
}
