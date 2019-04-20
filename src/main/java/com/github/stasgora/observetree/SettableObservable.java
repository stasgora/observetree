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
 * Maintains a set of observable listeners and parents for settable property
 *
 * @param <T> the type of the actual observable data type. Must inherit from {@link Observable}
 *
 * @author Stanisław Góra
 * @see SettableProperty
 */
public class SettableObservable<T extends Observable> extends SettableProperty<T> {

	private transient Set<ListenerEntry> staticListeners = new TreeSet<>();
	private transient Set<Observable> staticParents = new HashSet<>();

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
		staticParents.add(observable);
		return super.addParent(observable);
	}

	@Override
	public void set(T modelValue) {
		if(this.modelValue == modelValue) {
			return;
		}
		if(modelValue != null) {
			staticListeners.forEach(listener -> modelValue.addListener(listener.listener, listener.priority));
			staticParents.forEach(parent -> parent.addSubObservable(modelValue));
		}
		if(this.modelValue != null) {
			staticParents.forEach(parent -> parent.removeSubObservable(this.modelValue));
		}
		super.set(modelValue);
	}

}
