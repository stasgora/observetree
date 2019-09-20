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

import java.util.Collections;

public class ObservableTreeTest extends ObservableTestBase {
	private TestObservable parent;
	private ChangeListener parentListener = mockListener(() -> {});

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
		observable.setNotificationMethod(ListenerNotification.AUTOMATIC);

		observable.setValue(VALUE_TO_SET);
		verifyListenerCalled(listener, 1);
	}

	@Test
	public void whenAddingParent_itIsRegistered() {
		Assert.assertEquals(Collections.singleton(parent), observable.getParents());
		Assert.assertEquals(Collections.singleton(observable), parent.getChildren());
	}

	@Test
	public void whenParentObservableIsRemoved_itIsUnregistered() {
		parent.removeSubObservable(observable);

		Assert.assertEquals(Collections.emptySet(), observable.getParents());
		Assert.assertEquals(Collections.emptySet(), parent.getChildren());
	}

	@Test
	public void whenParentObservableIsRemoved_itsListenerIsNotCalled() {
		parent.addListener(listener);
		parent.removeSubObservable(observable);

		observable.setValue(VALUE_TO_SET);
		Assert.assertFalse(parent.isValueChanged());
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenParentObservableListenerHasHigherPriority_itIsCalledEarlier() {
		ChangeListener highPriorityListener = mockListener(() -> {});
		parent.addListener(highPriorityListener, ListenerPriority.HIGH);
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.notifyListeners();

		InOrder inOrder = Mockito.inOrder(listener, highPriorityListener);
		inOrder.verify(highPriorityListener, Mockito.times(1)).call();
		inOrder.verify(listener, Mockito.times(1)).call();
	}

	@Test
	public void whenObservableIsSetAsUnchangedTraversingTree_noChangedCallbackAreCalled() {
		parent.addListener(listener);
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.setUnchanged(true);
		observable.notifyListeners();
		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenObservableIsSetAsUnchangedWithoutTraversingTree_onlyParentListenerIsCalled() {
		ChangeListener differentListener = mockListener(() -> {});
		parent.addListener(differentListener);
		observable.addListener(listener);

		observable.setValue(VALUE_TO_SET);
		observable.setUnchanged(false);
		observable.notifyListeners();
		verifyListenerCalled(differentListener, 1);
		verifyListenerCalled(listener, 0);
	}

	@Test
	public void whenChangedChildIsAdded_parentIsSetAsChangedAsWell() {
		parent.removeSubObservable(observable);
		observable.setValue(VALUE_TO_SET);

		Assert.assertFalse(parent.isValueChanged());
		parent.addSubObservable(observable);
		Assert.assertTrue(parent.isValueChanged());
	}

	@Test
	public void onlyChangedChildrenListeners_getCalled() {
		TestObservable childObservable = new TestObservable();
		ChangeListener childListener = mockListener(() -> {});
		childObservable.addListener(childListener);
		observable.addListener(listener);
		parent.addListener(parentListener);
		observable.addSubObservable(childObservable);

		childObservable.setValue(VALUE_TO_SET);
		observable.setUnchanged(false);
		childObservable.notifyListeners();

		verifyListenerCalled(parentListener, 1);
		verifyListenerCalled(listener, 0);
		verifyListenerCalled(childListener, 1);
	}
}
