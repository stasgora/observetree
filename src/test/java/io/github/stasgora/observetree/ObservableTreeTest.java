/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import io.github.stasgora.observetree.enums.ListenerNotification;
import org.junit.Assert;
import org.junit.Test;

public class ObservableTreeTest extends ObservableTestBase {
	private TestObservable parent;

	@Override
	public void prepareObjects() {
		super.prepareObjects();
		parent = new TestObservable();
		parent.addSubObservable(observable);
	}

	@Test
	public void whenManualObservableHasParent_itsListenerIsCalled() {
		parent.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		Assert.assertTrue(parent.isValueChanged());
		observable.notifyListeners();
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenAutomaticObservableHasParent_itsListenerIsCalled() {
		parent.addListener(listener);
		observable.notificationMethod = ListenerNotification.AUTOMATIC;

		observable.setValue(VALUE_TO_SET);
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenParentObservableIsRemoved_itIsUnregistered() {
		parent.addListener(listener);
		parent.removeSubObservable(observable);

		observable.setValue(VALUE_TO_SET);
		Assert.assertFalse(parent.isValueChanged());
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenObservableIsSetAsUnchanged_noChangedCallbackAreCalled() {
		parent.addListener(listener);
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.setUnchanged(true);
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}
}
