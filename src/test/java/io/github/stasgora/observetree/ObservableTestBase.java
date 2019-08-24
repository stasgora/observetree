/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import io.github.stasgora.observetree.listener.ChangeListener;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public abstract class ObservableTestBase {

	protected static final int VALUE_TO_SET = 2;

	protected TestObservable observable;
	protected ChangeListener listener;

	@Before
	public void prepareObjects() {
		observable = new TestObservable();
		listener = mockListener(() -> {});
	}

	protected ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

	protected void verifyListenerCalled(ChangeListener listener, int times) {
		Mockito.verify(listener, Mockito.times(times)).call();
	}

	protected static class TestObservable extends Observable {
		private int value;

		void setValue(int value) {
			this.value = value;
			onValueChanged();
		}
	}
}
