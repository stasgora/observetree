/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.observable;

import io.github.stasgora.observetree.Observable;
import io.github.stasgora.observetree.TestBase;
import io.github.stasgora.observetree.listener.ChangeListener;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public abstract class ObservableTestBase extends TestBase {
	protected TestObservable observable;

	@Before
	public void prepareObjects() {
		observable = new TestObservable();
		listener = mockListener(() -> {});
	}

	public static class TestObservable extends Observable {
		private int value;

		public void setValue(int value) {
			this.value = value;
			onValueChanged();
		}
	}
}
