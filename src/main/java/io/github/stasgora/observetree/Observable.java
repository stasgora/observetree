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
import io.github.stasgora.observetree.listener.ListenerEntry;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base {@code Observable} class that holds a list of listeners and notifies them when it changes.
 * Use one of the {@link #addListener(ChangeListener)} methods to register a listener.
 *
 * <p>To signal that the {@code Observable} has changed call {@link #onValueChanged()} from the extending class.
 * When {@link #notificationMethod} is {@link ListenerNotification#MANUAL}, the changing entity should additionally call {@link #notifyListeners()} when it wants the callbacks to fire.
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
 * @see ChangeListener
 * @see ListenerPriority
 * @see ListenerNotification
 */
public abstract class Observable {

	/**
	 * Listeners notification method
	 */
	public transient ListenerNotification notificationMethod = ListenerNotification.MANUAL;
	private transient boolean valueChanged = false;

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
	 * Adds the specified {@code Observable} to the tree as a child of this {@code Observable}.
	 * This method is recommended for creating the {@code Observable} relations instead of plain {@link #addParent(Observable)} and {@link #addChild(Observable)}
	 * as it binds {@code Observables} both ways.
	 * @param observable element to be inserted into the tree as a child of this {@code Observable}
	 */
	protected void addSubObservable(Observable observable) {
		addChild(observable);
		observable.addParent(this);
		if(observable.isValueChanged())
			observable.onValueChanged();
	}

	/**
	 * Removes the specified {@code Observable} from the tree. This method is recommended for removing {@code Observable} relations
	 * instead of plain {@link #removeParent(Observable)} and {@link #removeChild(Observable)} as it unbinds {@code Observables} both ways.
	 * @param observable element to be removed from the tree
	 */
	protected void removeSubObservable(Observable observable) {
		removeChild(observable);
		observable.removeParent(this);
	}

	/**
	 * Marks the {@code Observable} and all its ancestors as changed. This method should be called inside a class extending {@code Observable} after a change was made.
	 */
	protected void onValueChanged() {
		parents.forEach(Observable::onValueChanged);
		valueChanged = true;
		if (notificationMethod == ListenerNotification.AUTOMATIC) {
			notifyListeners();
		}
	}

	private void collectListeners(TreeTraverseDirection direction, Set<ListenerEntry> treeListeners) {
		if(valueChanged) {
			valueChanged = false;
			treeListeners.addAll(listeners);
		}
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(observable -> observable.collectListeners(direction, treeListeners));
	}

	/**
	 * Invokes the listeners of all descendant and ancestor {@code Observables} that are currently marked as changed. Resets the changed flag.
	 * The listeners are called in order of their priority (globally) - first the listeners with the highest priority from all the {@code Observables} will be called, etc.
	 * The order at which the listeners with the same priority are called is undefined.
	 */
	public void notifyListeners() {
		Set<ListenerEntry> treeListeners = new TreeSet<>();
		collectListeners(TreeTraverseDirection.UP, treeListeners);
		collectListeners(TreeTraverseDirection.DOWN, treeListeners);
		treeListeners.forEach(entry -> entry.listener.call());
	}

	/**
	 * Copies all the listeners from this {@code Observable} to a specified {@code Observable}.
	 * @param observable element to copy the listeners to
	 */
	public void copyListeners(Observable observable) {
		listeners.forEach(listener -> observable.add(observable.listeners, listener.listener, listener.priority));
	}

	/**
	 * Removes all the listeners from this {@code Observable}
	 */
	public void clearListeners() {
		listeners.clear();
	}


	/**
	 * Resets the changed flag on this object and - if the {@code traverseTree} flag is set to {@code true} -
	 * all descendant and ancestor {@code Observables} that are currently marked as changed.
	 * @param traverseTree whether to set all descendant and ancestor as unchanged
	 */
	public void setUnchanged(boolean traverseTree) {
		if(!traverseTree) {
			valueChanged = false;
			return;
		}
		setUnchanged(TreeTraverseDirection.UP);
		setUnchanged(TreeTraverseDirection.DOWN);
	}


	/**
	 * Returns whether this {@code Observable} was changed and it's listeners not yet called
	 * @return {@code true} if the {@code Observable} was changed. {@code false} if not
	 */
	public boolean isValueChanged() {
		return valueChanged;
	}

	private void setUnchanged(TreeTraverseDirection direction) {
		valueChanged = false;
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(relative -> relative.setUnchanged(direction));
	}

	/**
	 * Utility method used internally that contains logic associated with adding a {@code listener} to a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be added
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener) {
		return add(listenerSet, listener, ListenerPriority.NORMAL);
	}

	/**
	 * Utility method used internally that contains logic associated with adding a {@code listener} to a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener, ListenerPriority priority) {
		return add(listenerSet, listener, priority.value);
	}

	/**
	 * Utility method used internally that contains logic associated with adding a {@code listener} to a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener, int priority) {
		if(listenerSet.stream().noneMatch(entry -> entry.listener == listener)) {
			listenerSet.add(new ListenerEntry(listener, priority));
			return true;
		}
		return false;
	}

	/**
	 * Utility method used internally that contains logic associated with removing a {@code listener} from a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be removed
	 * @return {@code true} if the listener was successfully removed. {@code false} if it was not found
	 */
	protected boolean remove(Set<ListenerEntry> listenerSet, ChangeListener listener) {
		Optional<ListenerEntry> optional = listenerSet.stream().filter(entry -> entry.listener == listener).findFirst();
		optional.ifPresent(listenerSet::remove);
		return optional.isPresent();
	}

	/**
	 * Sets the specified {@code Observable} as a parent of this {@code Observable}. <i>Note: this creates only one way binding.</i>
	 * {@link #addSubObservable(Observable)} method is recommended for setting up {@code Observable} relations.
	 * @param observable parent to be added
	 * @return {@code true} if the {@code Observable} was successfully added. {@code false} if it was already present
	 */
	protected boolean addParent(Observable observable) {
		return parents.add(observable);
	}

	/**
	 * Removes the specified {@code Observable} from parents of this {@code Observable}. <i>Note: this removed one way binding only.</i>
	 * {@link #removeSubObservable(Observable)} method is recommended for removing {@code Observable} relations.
	 * @param observable parent to be removed
	 * @return {@code true} if the {@code Observable} was successfully removed. {@code false} if it was not found
	 */
	protected boolean removeParent(Observable observable) {
		return parents.remove(observable);
	}


	/**
	 * Sets the specified {@code Observable} as a child of this {@code Observable}. <i>Note: this creates only one way binding.</i>
	 * {@link #addSubObservable(Observable)} method is recommended for setting up {@code Observable} relations.
	 * @param observable child to be added
	 * @return {@code true} if the {@code Observable} was successfully added. {@code false} if it was already present
	 */
	protected boolean addChild(Observable observable) {
		return children.add(observable);
	}

	/**
	 * Removes the specified {@code Observable} from children of this {@code Observable}. <i>Note: this removed one way binding only.</i>
	 * {@link #removeSubObservable(Observable)} method is recommended for removing {@code Observable} relations.
	 * @param observable child to be removed
	 * @return {@code true} if the {@code Observable} was successfully removed. {@code false} if it was not found
	 */
	protected boolean removeChild(Observable observable) {
		return children.remove(observable);
	}

	/**
	 * Returns parents of this {@code Observable}
	 * @return a set containing all the parents of this object
	 */
	public Set<Observable> getParents() {
		return parents;
	}

	/**
	 * Returns children of this {@code Observable}
	 * @return a set containing all the children of this object
	 */
	public Set<Observable> getChildren() {
		return children;
	}

	private enum TreeTraverseDirection {
		UP, DOWN
	}

}
