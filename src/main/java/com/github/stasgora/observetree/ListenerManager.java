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
import java.util.Optional;
import java.util.Set;

/**
 * Abstract base class providing add and remove methods for {@link ChangeListener} and {@link Observable} parents and children
 *
 * @author Stanisław Góra
 * @see Observable
 */
public abstract class ListenerManager {

	private transient Set<Observable> parents = new HashSet<>();
	private transient Set<Observable> children = new HashSet<>();

	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener) {
		return add(listenerSet, listener, ListenerPriority.NORMAL);
	}

	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener, ListenerPriority priority) {
		return add(listenerSet, listener, priority.value);
	}

	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener, int priority) {
		if(listenerSet.stream().noneMatch(entry -> entry.listener == listener)) {
			listenerSet.add(new ListenerEntry(listener, priority));
			return true;
		}
		return false;
	}

	protected boolean remove(Set<ListenerEntry> listenerSet, ChangeListener listener) {
		Optional<ListenerEntry> optional = listenerSet.stream().filter(entry -> entry.listener == listener).findFirst();
		optional.ifPresent(listenerSet::remove);
		return optional.isPresent();
	}

	protected boolean addParent(Observable observable) {
		return parents.add(observable);
	}

	protected boolean removeParent(Observable observable) {
		return parents.remove(observable);
	}

	protected boolean addChild(Observable observable) {
		return children.add(observable);
	}

	protected boolean removeChild(Observable observable) {
		return children.remove(observable);
	}

}
