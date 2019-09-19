/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import io.github.stasgora.observetree.enums.ListenerPriority;
import io.github.stasgora.observetree.listener.ChangeListener;
import io.github.stasgora.observetree.listener.ListenerEntry;

import java.util.Set;
import java.util.TreeSet;

/**
 * Extends the functionality of the {@link SettableProperty} for {@link #modelValue} extending {@link Observable}.
 * Maintains a persistent set of {@link #modelValue} listeners that are copied over whenever a new {@link #modelValue} is set.
 * Parents of this {@code SettableProperty} automatically become parents of it's {@link #modelValue}.
 *
 * @param <T> the type of the actual observable data type. Must extend from {@link Observable}
 *
 * @author Stanisław Góra
 * @see SettableProperty
 */
public class SettableObservable<T extends Observable> extends SettableProperty<T> {

	private transient Set<ListenerEntry> staticListeners = new TreeSet<>();

	/**
	 * Constructs a new, empty {@code SettableObservable} with {@code null} {@link #modelValue}
	 */
	public SettableObservable() {
	}

	/**
	 * Constructs a new {@code SettableObservable} with the specified {@link #modelValue}
	 * @param modelValue a value to be set
	 */
	public SettableObservable(T modelValue) {
		super(modelValue);
	}

	/**
	 * Adds the specified listener to the list of static listeners with the default priority {@link ListenerPriority#NORMAL} (0).
	 * @param listener element to be added
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addStaticListener(ChangeListener listener) {
		return addStaticListener(listener, ListenerPriority.NORMAL);
	}

	/**
	 * Adds the specified listener to the list of static listeners with the specified priority.
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addStaticListener(ChangeListener listener, ListenerPriority priority) {
		return addStaticListener(listener, priority.value);
	}

	/**
	 * Adds the specified listener to the list of static listeners with the specified priority.
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	public boolean addStaticListener(ChangeListener listener, int priority) {
		boolean added = add(staticListeners, listener, priority);
		if(added && modelValue != null)
			modelValue.addListener(listener, priority);
		return added;
	}

	/**
	 * Removes the specified static listener from the list of listeners.
	 * @param listener element to be removed
	 * @return {@code true} if the listener was successfully removed. {@code false} if it was not found
	 */
	public boolean removeStaticListener(ChangeListener listener) {
		boolean removed = remove(staticListeners, listener);
		if(removed && modelValue != null)
			modelValue.removeListener(listener);
		return removed;
	}

	/**
	 * Sets the specified {@code Observable} as a parent of this {@code SettableObservable}. <i>Note: this creates only one way binding.</i>
	 * {@link #addSubObservable(Observable)} method is recommended for setting up {@code Observable} relations. Method overridden to provide additional functionality.
	 * @param observable parent to be added
	 * @return {@code true} if the {@code Observable} was successfully added. {@code false} if it was already present
	 */
	@Override
	protected boolean addParent(Observable observable) {
		if(modelValue != null) {
			observable.addSubObservable(modelValue);
		}
		return super.addParent(observable);
	}

	/**
	 * Removes the specified {@code Observable} from parents of this {@code SettableObservable}. <i>Note: this removed one way binding only.</i>
	 * {@link #removeSubObservable(Observable)} method is recommended for removing {@code Observable} relations. Method overridden to provide additional functionality.
	 * @param observable parent to be removed
	 * @return {@code true} if the {@code Observable} was successfully removed. {@code false} if it was not found
	 */
	@Override
	protected boolean removeParent(Observable observable) {
		if(modelValue != null) {
			observable.removeSubObservable(modelValue);
		}
		return super.removeParent(observable);
	}

	/**
	 * Sets the {@link #modelValue} of this {@code SettableObservable}. Method overridden to provide additional functionality.
	 * @param modelValue value to be set
	 */
	@Override
	public void set(T modelValue) {
		if(this.modelValue == modelValue) {
			return;
		}
		if(this.modelValue != null) {
			getParents().forEach(parent -> parent.removeSubObservable(this.modelValue));
		}
		if(modelValue != null) {
			staticListeners.forEach(listener -> modelValue.addListener(listener.listener, listener.priority));
			getParents().forEach(parent -> parent.addSubObservable(modelValue));
		}
		super.set(modelValue);
	}

}
