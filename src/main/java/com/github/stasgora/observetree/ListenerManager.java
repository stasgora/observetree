package com.github.stasgora.observetree;

import java.util.HashSet;
import java.util.Set;

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
