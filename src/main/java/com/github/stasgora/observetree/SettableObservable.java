/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package com.github.stasgora.observetree;

import com.github.stasgora.observetree.enums.ListenerPriority;
import com.github.stasgora.observetree.listener.ChangeListener;
import com.github.stasgora.observetree.listener.ListenerEntry;

import java.util.HashSet;
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

	public SettableObservable() {
	}

	public SettableObservable(T modelValue) {
		super(modelValue);
	}

	public boolean addStaticListener(ChangeListener listener) {
		return add(staticListeners, listener);
	}

	public boolean addStaticListener(ChangeListener listener, ListenerPriority priority) {
		return add(staticListeners, listener, priority);
	}

	public boolean addStaticListener(ChangeListener listener, int priority) {
		return add(staticListeners, listener, priority);
	}

	public boolean removeStaticListener(ChangeListener listener) {
		return remove(staticListeners, listener);
	}

	@Override
	protected boolean addParent(Observable observable) {
		if(modelValue != null) {
			observable.addSubObservable(modelValue);
		}
		return super.addParent(observable);
	}

	@Override
	protected boolean removeParent(Observable observable) {
		if(modelValue != null) {
			observable.removeSubObservable(modelValue);
		}
		return super.removeParent(observable);
	}

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
