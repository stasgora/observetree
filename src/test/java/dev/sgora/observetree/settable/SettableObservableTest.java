/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package dev.sgora.observetree.settable;

import dev.sgora.observetree.TestBase;
import dev.sgora.observetree.model.SettableTestObservable;
import dev.sgora.observetree.model.TestObservable;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class SettableObservableTest extends TestBase {
	private TestObservable modelValue;
	private SettableTestObservable<TestObservable> settableObservable;
	private SettableTestObservable<TestObservable> settableParent;

	@Before
	public void prepareObjects() {
		super.prepareObjects();
		modelValue = new TestObservable();
		settableObservable = new SettableTestObservable<>(modelValue);
		settableParent = new SettableTestObservable<>();
		settableParent.addSubObservable(settableObservable);
	}

	@Test
	public void testParentAssignment() {
		assertEquals(Collections.singleton(settableParent), settableObservable.getParents());
		assertEquals(Collections.singleton(settableParent), modelValue.getParents());
	}

	@Test
	public void whenParentIsRemoved_itIsUnregistered() {
		settableParent.removeSubObservable(settableObservable);

		assertEquals(Collections.emptySet(), settableObservable.getParents());
		assertEquals(Collections.emptySet(), modelValue.getParents());
	}

	@Test
	public void whenValueIsSet_parentsGetCopied() {
		modelValue = new TestObservable();
		settableObservable.set(modelValue);

		assertEquals(Collections.singleton(settableParent), modelValue.getParents());
	}

	@Test
	public void testStaticListenersAssignment() {
		settableObservable.addStaticListener(listener);

		settableObservable.get().setValue(VALUE_TO_SET);
		settableObservable.get().notifyListeners();

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenStaticListenersIsRemoved_itIsUnregistered() {
		settableObservable.addStaticListener(listener);
		settableObservable.removeStaticListener(listener);

		settableObservable.get().setValue(VALUE_TO_SET);
		settableObservable.get().notifyListeners();

		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenSettableObjectIsSetButEqual_noListenersGetCalled() {
		settableObservable.addListener(listener);

		settableObservable.set(modelValue);
		settableObservable.notifyListeners();

		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenValueIsSet_staticListenersGetCopied() {
		settableObservable.addStaticListener(listener);

		settableObservable.set(new TestObservable());
		settableObservable.get().setValue(VALUE_TO_SET);
		settableObservable.get().notifyListeners();

		verifyListenerCalled(listener, 1);
	}
}
