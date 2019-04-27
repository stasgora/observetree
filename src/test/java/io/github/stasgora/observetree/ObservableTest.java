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

	private TestObservable observable;
	private static final int VALUE_TO_SET = 2;

	@Before
	public void prepareObservables() {
		observable = new TestObservable();
	}

	@Test
	public void whenManualObservableIsChanged_listenerIsCalledAndValueSet() {
		ChangeListener listener = mockListener(() -> Assert.assertEquals(observable.value, VALUE_TO_SET));
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		Mockito.verify(listener, Mockito.times(1)).call();
	}

	@Test
	public void whenAutomaticObservableIsChanged_listenerIsCalledAndValueSet() {
		observable.notificationMethod = ListenerNotification.AUTOMATIC;
		ChangeListener listener = mockListener(() -> Assert.assertEquals(observable.value, VALUE_TO_SET));
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		Mockito.verify(listener, Mockito.times(1)).call();
	}

	@Test
	public void whenAddingHighPriorityListener_itIsCalledEarlier() {
		ChangeListener normalPriorityListener = mockListener(() -> {});
		ChangeListener highPriorityListener = mockListener(() -> {});
		observable.addListener(normalPriorityListener);
		observable.addListener(highPriorityListener, ListenerPriority.HIGH);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();
		InOrder inOrder = Mockito.inOrder(normalPriorityListener, highPriorityListener);
		inOrder.verify(highPriorityListener, Mockito.times(1)).call();
		inOrder.verify(normalPriorityListener, Mockito.times(1)).call();
	}

	private ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

	class TestObservable extends Observable {
		private int value;

		void setValue(int var) {
			this.value = var;
			onValueChanged();
		}
	}

}
