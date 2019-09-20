/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.observable;

import io.github.stasgora.observetree.model.TestObservable;
import io.github.stasgora.observetree.enums.ListenerNotification;
import io.github.stasgora.observetree.enums.ListenerPriority;
import io.github.stasgora.observetree.listener.ChangeListener;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObservableTest extends ObservableTestBase {

	@Test
	public void whenManualObservableIsChanged_listenerIsCalledAndValueSet() {
		observable.addListener(listener);
		observable.setValue(VALUE_TO_SET);
		Assert.assertTrue(observable.isValueChanged());
		observable.notifyListeners();
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenAutomaticObservableIsChanged_listenerIsCalledAndValueSet() {
		observable.setNotificationMethod(ListenerNotification.AUTOMATIC);
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
	public void clearListeners_removesAllListeners() {
		observable.addListener(listener);

		observable.clearListeners();
		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();

		verifyListenerCalled(listener, 0);
	}

	@Test
	public void testCopyingListeners() {
		TestObservable otherObservable = new TestObservable();
		observable.addListener(listener);

		observable.copyListeners(otherObservable);
		otherObservable.setValue(VALUE_TO_SET);
		otherObservable.notifyListeners();

		verifyListenerCalled(listener, 1);
	}

	@Test
	public void testAddingTheSameListenerTwice() {
		assertTrue(observable.addListener(listener));
		assertFalse(observable.addListener(listener));
	}

}
