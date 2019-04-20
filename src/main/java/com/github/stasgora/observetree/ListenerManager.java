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
 * Abstract base class providing add and remove methods for {@link ChangeListener} and {@link ListenerManager} parents and children
 *
 * @author Stanisław Góra
 * @see Observable
 */
public abstract class ListenerManager {

	private transient Set<ListenerManager> parents = new HashSet<>();
	private transient Set<ListenerManager> children = new HashSet<>();

	/**
	 * Utility method containing logic associated with adding a {@code listener} to a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be added
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener) {
		return add(listenerSet, listener, ListenerPriority.NORMAL);
	}

	/**
	 * Utility method containing logic associated with adding a {@code listener} to a specified {@code set}
	 * @param listenerSet set to operate onto
	 * @param listener element to be added
	 * @param priority priority of this listener
	 * @return {@code true} if the listener was successfully added. {@code false} if it was already present
	 */
	protected boolean add(Set<ListenerEntry> listenerSet, ChangeListener listener, ListenerPriority priority) {
		return add(listenerSet, listener, priority.value);
	}

	/**
	 * Utility method containing logic associated with adding a {@code listener} to a specified {@code set}
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
	 * Utility method containing logic associated with removing a {@code listener} from a specified {@code set}
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
	 * Sets the specified {@code ListenerManager} as a parent of this {@code ListenerManager}.
	 * @param observable parent to be added
	 * @return {@code true} if the {@code ListenerManager} was successfully added. {@code false} if it was already present
	 */
	protected boolean addParent(ListenerManager observable) {
		return parents.add(observable);
	}

	/**
	 * Removes the specified {@code ListenerManager} from parents of this {@code ListenerManager}.
	 * @param observable parent to be removed
	 * @return {@code true} if the {@code ListenerManager} was successfully removed. {@code false} if it was not found
	 */
	protected boolean removeParent(ListenerManager observable) {
		return parents.remove(observable);
	}


	/**
	 * Sets the specified {@code ListenerManager} as a child of this {@code ListenerManager}.
	 * @param observable child to be added
	 * @return {@code true} if the {@code ListenerManager} was successfully added. {@code false} if it was already present
	 */
	protected boolean addChild(ListenerManager observable) {
		return children.add(observable);
	}

	/**
	 * Removes the specified {@code ListenerManager} from children of this {@code ListenerManager}.
	 * @param observable child to be removed
	 * @return {@code true} if the {@code ListenerManager} was successfully removed. {@code false} if it was not found
	 */
	protected boolean removeChild(ListenerManager observable) {
		return children.remove(observable);
	}

}
