/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.listener;

import java.util.Objects;

/**
 * Internal data class holding listener reference together with its priority. Overrides standard methods that ensure proper aggregation in collections.
 *
 * @author Stanisław Góra
 * @see ChangeListener
 */
public class ListenerEntry implements Comparable<ListenerEntry> {

	/**
	 * Listener callback method reference
	 */
	public ChangeListener listener;
	/**
	 * Listener priority
	 */
	public int priority;

	/**
	 * Constructs new listener entry object
	 * @param listener listener callback method reference
	 * @param priority listener priority
	 */
	public ListenerEntry(ChangeListener listener, int priority) {
		this.listener = listener;
		this.priority = priority;
	}

	/**
	 * Compares the specified object with this {@code ListenerEntry} for equality. Returns true if the given object is also a {@code ListenerEntry}
	 * and the two have the same {@link #listener} reference and {@link #priority}
	 * @param o object to be compared for equality with this {@code ListenerEntry}
	 * @return {@code true} if the specified object is equal to this {@code ListenerEntry}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ListenerEntry that = (ListenerEntry) o;
		return priority == that.priority && listener.equals(that.listener);
	}

	/**
	 * Returns the hash code value for this {@code ListenerEntry}. It is defined by the hashes of the {@link #priority} and {@link #listener}.
	 * @return the hash code value for this {@code ListenerEntry}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(listener, priority);
	}

	/**
	 * Compares the specified object with this {@code ListenerEntry}. It sorts the objects in a descending order according to their {@link #priority}.
	 * @param o {@code ListenerEntry} to which this {@code ListenerEntry} is to be compared
	 * @return -1 or 1 as this {@code ListenerEntry} should be placed before or behind {@code o} or 0 if the objects are equal.
	 */
	@Override
	public int compareTo(ListenerEntry o) {
		return this.equals(o) ? 0 : (priority >= o.priority ? -1 : 1);
	}
}
