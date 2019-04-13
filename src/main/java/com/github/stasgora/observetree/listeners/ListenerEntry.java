package com.github.stasgora.observetree.listeners;

import java.util.Objects;

/**
 *
 * @author Stanisław Góra
 * @see ChangeListener
 * @see ListenerPriority
 */
public class ListenerEntry implements Comparable<ListenerEntry> {

	public ChangeListener listener;
	public int priority;

	public ListenerEntry(ChangeListener listener, int priority) {
		this.listener = listener;
		this.priority = priority;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ListenerEntry that = (ListenerEntry) o;
		return priority == that.priority && listener.equals(that.listener);
	}

	@Override
	public int hashCode() {
		return Objects.hash(listener, priority);
	}

	@Override
	public int compareTo(ListenerEntry o) {
		return this.equals(o) ? 0 : (priority >= o.priority ? -1 : 1);
	}
}
