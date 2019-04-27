/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import io.github.stasgora.observetree.listener.ChangeListener;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public class ObservableTest {

	class TestObservable extends Observable {
		private int value;

		void setValue(int var) {
			this.value = var;
			onValueChanged();
		}
	}

	@Test
	public void whenObservableIsChanged_listenerIsCalledAndValueSet() {
		TestObservable observable = new TestObservable();
		int valueToSet = 2;
		ChangeListener listener = mockListener(() -> Assert.assertEquals(observable.value, valueToSet));
		observable.addListener(listener);

		observable.setValue(valueToSet);
		observable.notifyListeners();
		Mockito.verify(listener, Mockito.times(1)).call();
	}

	private ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

}
