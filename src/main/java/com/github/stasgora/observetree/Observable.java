/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package com.github.stasgora.observetree;

import com.github.stasgora.observetree.enums.ListenerNotification;
import com.github.stasgora.observetree.enums.ListenerPriority;
import com.github.stasgora.observetree.listener.ChangeListener;
import com.github.stasgora.observetree.listener.ListenerEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base {@code Observable} class that holds a list of listeners and notifies them when it changes.
 * Use one of the {@link #add(Set, ChangeListener)} methods to register a listener.
 *
 * <p>To signal that the {@code Observable} has changed call {@link #onValueChanged()} from the extending class.
 * When {@link #notificationMethod} is {@code true}, the changing entity should additionally call {@link #notifyListeners()} when it wants the callbacks to fire.
 *
 * <p>Use {@link #notificationMethod} flag to configure whether {@link #notifyListeners()} should be called:
 * <ul>
 *     <li>automatically, immediately after a {@link #onValueChanged()} was called</li>
 *     <li>manually, through {@link #notifyListeners()} (useful for complex multistage changes)</li>
 * </ul>
 *
 * <p>{@code Observables} can create a tree structure.
 * For creating the relations use {@link #addSubObservable(Observable)}<br>
 * <b>Cycles are not supported.</b>
 * <ul>
 *     <li>Calling {@link #onValueChanged()} marks the {@code Observable} and all its ancestors as changed</li>
 *     <li>
 *          Calling {@link #notifyListeners()}:
 *          <ul>
 *              <li>resets the {@code Observable} changed flag</li>
 *              <li>invokes the listeners of all descendant and ancestor {@code Observables} that are currently marked as changed</li>
 *          </ul>
 *          The listeners are called in order of their priority (globally) - first the listeners with the highest priority from all the {@code Observables} will be called, etc.
 *          The order at which the listeners with the same priority are called is undefined.
 *     </li>
 * </ul>
 *
 * <p><i>Example use:</i><br>
 * With several child {@code Observables} grouped under a single parent, multiple independent modifications can be made to the children.
 * Then the {@link #notifyListeners()} can be called only once on the parent object to notify all the listeners including the children's.
 *
 * @author Stanisław Góra
 * @see SettableProperty
 * @see SettableObservable
 * @see ListenerPriority
 */
public abstract class Observable extends ListenerManager {

	/**
	 * Listeners notification method
	 */
	public transient ListenerNotification notificationMethod = ListenerNotification.MANUAL;
	private transient boolean wasValueChanged = false;

	private transient Set<ListenerEntry> listeners = new TreeSet<>();
	private transient Set<Observable> parents = new HashSet<>();
	private transient Set<Observable> children = new HashSet<>();

	/**
	 * Adds the specified listener to the list of listeners with the default priority {@link ListenerPriority#NORMAL} (0).
	 * @param listener element to be added
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addListener(ChangeListener listener) {
		return add(listeners, listener);
	}

	/**
	 * Adds the specified listener to the list of listeners with the specified priority.
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addListener(ChangeListener listener, ListenerPriority priority) {
		return add(listeners, listener, priority);
	}

	/**
	 * Adds the specified listener to the list of listeners with the specified priority.
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addListener(ChangeListener listener, int priority) {
		return add(listeners, listener, priority);
	}

	/**
	 * Removes the specified listener from the list of listeners.
	 * @param listener element to be removed
	 * @return {@code true} if the listener was successfully removed. {@code false} if it was not found
	 */
	public boolean removeListener(ChangeListener listener) {
		return remove(listeners, listener);
	}

	/**
	 * @param observable
	 */
	protected void addSubObservable(Observable observable) {
		initSubObservable(observable);
		if(observable instanceof SettableObservable) {
			SettableObservable settableObservable = (SettableObservable) observable;
			if(settableObservable.present()) {
				initSubObservable((Observable) settableObservable.modelValue);
			}
		}
	}

	/**
	 * @param model
	 */
	protected void initSubObservable(Observable model) {
		model.addParent(this);
		children.add(model);
		model.setUnchanged();
	}

	/**
	 * @param model
	 */
	protected void removeSubObservable(Observable model) {
		model.parents.remove(this);
		children.remove(model);
	}

	/**
	 *
	 */
	protected void onValueChanged() {
		parents.forEach(Observable::onValueChanged);
		wasValueChanged = true;
		if (notificationMethod == ListenerNotification.AUTOMATIC) {
			listeners.forEach(entry -> entry.listener.call());
		}
	}

	private void collectListeners(TreeTraverseDirection direction, Set<ListenerEntry> treeListeners) {
		if(wasValueChanged && notificationMethod == ListenerNotification.MANUAL) {
			wasValueChanged = false;
			treeListeners.addAll(listeners);
		}
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(observable -> observable.collectListeners(direction, treeListeners));
	}

	/**
	 *
	 */
	public void notifyListeners() {
		Set<ListenerEntry> treeListeners = new TreeSet<>();
		collectListeners(TreeTraverseDirection.UP, treeListeners);
		collectListeners(TreeTraverseDirection.DOWN, treeListeners);
		treeListeners.forEach(entry -> entry.listener.call());
	}

	/**
	 * @param observable
	 */
	public void copyListeners(Observable observable) {
		listeners.forEach(listener -> observable.add(listeners, listener.listener, listener.priority));
	}

	/**
	 *
	 */
	public void clearListeners() {
		listeners.clear();
	}

	/**
	 *
	 */
	public void setUnchanged() {
		setUnchanged(TreeTraverseDirection.UP);
		setUnchanged(TreeTraverseDirection.DOWN);
	}

	private void setUnchanged(TreeTraverseDirection direction) {
		wasValueChanged = false;
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(relative -> relative.setUnchanged(direction));
	}

	private enum TreeTraverseDirection {
		UP, DOWN
	}

}
