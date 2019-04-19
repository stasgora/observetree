/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package com.github.stasgora.observetree;

import com.github.stasgora.observetree.enums.ListenerPriority;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class providing add and remove methods for {@link ChangeListener} and {@link Observable} parents and children
 *
 * @author Stanisław Góra
 * @see Observable
 */
abstract class ListenerManager {

	private transient Set<Observable> parents = new HashSet<>();
	private transient Set<Observable> children = new HashSet<>();

	public void add(Set<ListenerEntry> listenerSet, ChangeListener callback) {
		add(listenerSet, callback, ListenerPriority.NORMAL);
	}

	public void add(Set<ListenerEntry> listenerSet, ChangeListener callback, ListenerPriority priority) {
		add(listenerSet, callback, priority.value);
	}

	public void add(Set<ListenerEntry> listenerSet, ChangeListener callback, int priority) {
		if(listenerSet.stream().noneMatch(listener -> listener.listener == callback)) {
			listenerSet.add(new ListenerEntry(callback, priority));
		}
	}

	public void remove(Set<ListenerEntry> listenerSet, ChangeListener callback) {
		listenerSet.stream().filter(listener -> listener.listener == callback).findFirst().ifPresent(listenerSet::remove);
	}

	protected void addParent(Observable observable) {
		parents.add(observable);
	}

	protected void removeParent(Observable observable) {
		parents.remove(observable);
	}

	protected void addChild(Observable observable) {
		children.add(observable);
	}

	protected void removeChild(Observable observable) {
		children.remove(observable);
	}

}
