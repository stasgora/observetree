package com.github.stasgora.observetree;

import java.util.TreeSet;

public class ListenerSet extends TreeSet<ListenerEntry> {

	public void add(ChangeListener callback) {
		add(callback, ListenerPriority.NORMAL);
	}

	public void add(ChangeListener callback, ListenerPriority priority) {
		add(callback, priority.value);
	}

	public void add(ChangeListener callback, int priority) {
		if(stream().noneMatch(listener -> listener.listener == callback)) {
			super.add(new ListenerEntry(callback, priority));
		}
	}

	public void remove(ChangeListener callback) {
		stream().filter(listener -> listener.listener == callback).findFirst().ifPresent(super::remove);
	}

}
