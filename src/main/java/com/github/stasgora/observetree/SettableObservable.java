package com.github.stasgora.observetree;

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
 * @see SettableObservable
 */
public class SettableObservable<T extends Observable> extends SettableProperty<T> {

	private transient Set<ListenerEntry> staticListeners = new TreeSet<>();
	private transient Set<Observable> staticParents = new HashSet<>();

	public SettableObservable() {
	}

	public SettableObservable(T modelValue) {
		super(modelValue);
	}

	public void addStaticListener(ChangeListener callback) {
		add(staticListeners, callback);
	}

	public void addStaticListener(ChangeListener callback, ListenerPriority priority) {
		add(staticListeners, callback, priority);
	}

	public void addStaticListener(ChangeListener callback, int priority) {
		add(staticListeners, callback, priority);
	}

	public void removeStaticListener(ChangeListener callback) {
		remove(staticListeners, callback);
	}

	@Override
	protected void addParent(Observable observable) {
		super.addParent(observable);
		staticParents.add(observable);
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
