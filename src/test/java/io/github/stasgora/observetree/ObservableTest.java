/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import io.github.stasgora.observetree.enums.ListenerNotification;
import io.github.stasgora.observetree.enums.ListenerPriority;
import io.github.stasgora.observetree.listener.ChangeListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class ObservableTest {

	private static final int VALUE_TO_SET = 2;

	private TestObservable observable;
	private ChangeListener listener;

	@Before
	public void prepareObjects() {
		observable = new TestObservable();
		listener = mockListener(() -> Assert.assertEquals(observable.value, VALUE_TO_SET));
	}

	@Test
	public void whenManualObservableIsChanged_listenerIsCalledAndValueSet() {
		observable.addListener(listener);
		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenAutomaticObservableIsChanged_listenerIsCalledAndValueSet() {
		observable.notificationMethod = ListenerNotification.AUTOMATIC;
		observable.addListener(listener);
		observable.setValue(VALUE_TO_SET);
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenRemovingListener_itIsUnregistered() {
		observable.addListener(listener);
		observable.removeListener(listener);
		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenAddingHighPriorityListener_itIsCalledEarlier() {
		ChangeListener highPriorityListener = mockListener(() -> {});
		observable.addListener(listener);
		observable.addListener(highPriorityListener, ListenerPriority.HIGH);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		InOrder inOrder = Mockito.inOrder(listener, highPriorityListener);
		inOrder.verify(highPriorityListener, Mockito.times(1)).call();
		inOrder.verify(listener, Mockito.times(1)).call();
	}

	@Test
	public void whenThereIsParentObservable_itsListenerIsCalled() {
		TestObservable parent = new TestObservable();
		parent.addSubObservable(observable);
		parent.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenParentObservableIsRemoved_itIsUnregistered() {
		TestObservable parent = new TestObservable();
		parent.addSubObservable(observable);
		parent.addListener(listener);
		parent.removeSubObservable(observable);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}

	private ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

	private void verifyListenerCalled(ChangeListener listener, int times) {
		Mockito.verify(listener, Mockito.times(times)).call();
	}

	class TestObservable extends Observable {
		private int value;

		void setValue(int value) {
			this.value = value;
			onValueChanged();
		}
	}

}
