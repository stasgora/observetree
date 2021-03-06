package dev.sgora.observetree.settable;

import dev.sgora.observetree.SettableProperty;
import dev.sgora.observetree.TestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettablePropertyTest extends TestBase {
	private SettableProperty<Integer> settableInt;

	@Before
	public void prepareObjects() {
		super.prepareObjects();
		settableInt = new SettableProperty<>(0);
	}

	@Test
	public void whenSettableObjectIsSet_theListenersGetCalled() {
		settableInt.addListener(listener);

		settableInt.set(VALUE_TO_SET);
		settableInt.notifyListeners();

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenSettableObjectIsSetButEqual_noListenersGetCalled() {
		settableInt.addListener(listener);

		settableInt.set(0);
		settableInt.notifyListeners();

		verifyListenerCalled(listener, 0);
	}

	@Test
	public void testValuePresentMethod() {
		assertTrue(settableInt.present());
		settableInt.set(null);
		assertFalse(settableInt.present());
	}

	@Test
	public void whenValueIsModified_itIsMarkedAsChanged() {
		settableInt.addListener(listener);

		settableInt.modify(val -> VALUE_TO_SET);

		assertEquals(VALUE_TO_SET, (int) settableInt.get());
		assertTrue(settableInt.isValueChanged());
	}

	@Test
	public void whenSettableObjectIsSetWithNotify_theListenersGetCalled() {
		settableInt.addListener(listener);

		settableInt.setAndNotify(VALUE_TO_SET);

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenSettingDefaultValue_itCanBeRestored() {
		settableInt.set(VALUE_TO_SET);
		settableInt.saveAsDefaultValue();
		settableInt.set(0);

		assertEquals(VALUE_TO_SET, (int) settableInt.getDefaultValue());
		settableInt.resetToDefaultValue();
		assertEquals(VALUE_TO_SET, (int) settableInt.get());
	}
}
