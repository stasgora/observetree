package com.github.stasgora.observetree;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base {@code Observable} class that holds a list of listeners and notifies them when it changes.
 * Use one of the {@link #addListener(ChangeListener)} methods to register a listener.
 *
 * <p>To signal that the {@code Observable} has changed call {@link #onValueChanged()} from the extending class.
 * When {@link #notifyManually} is {@code true}, the changing entity should additionally call {@link #notifyListeners()} when it wants the callbacks to fire.
 *
 * <p>Use {@link #notifyManually} flag to configure whether {@link #notifyListeners()} should be called:
 * <ul>
 *     <li>automatically, immediately after a {@link #onValueChanged()} was called</li>
 *     <li>manually (useful for complex multistage changes)</li>
 * </ul>
 *
 * <p>{@code Observables} can create a tree structure.
 * For creating the relations use {@link #addSubObservable(Observable)}<br>
 * <b>Cycles are not supported.</b>
 * <ul>
 *     <li>Calling {@link #onValueChanged()} marks the {@code Observable} and all its ancestors as changed</li>
 *     <li>
 *          Calling {@link #notifyListeners()}:
 *          <ul>
 *              <li>resets the {@code Observable} changed flag</li>
 *              <li>invokes the listeners of all descendant and ancestor {@code Observables} that are currently marked as changed</li>
 *          </ul>
 *          The listeners are called in order of their priority (globally) - first the listeners with the highest priority from all the {@code Observables} will be called, etc.
 *          The order at which the listeners with the same priority are called is undefined.
 *     </li>
 * </ul>
 *
 * <p><i>Example use:</i><br>
 * With several child {@code Observables} grouped under a single parent, multiple independent modifications can be made to the children.
 * Then the {@link #notifyListeners()} can be called only once on the parent object to notify all the listeners including the children's.
 *
 * @author Stanisław Góra
 * @see SettableProperty
 * @see SettableObservable
 * @see ListenerPriority
 */
public abstract class Observable {

	public transient boolean notifyManually = true;
	private transient boolean wasValueChanged = false;

	private transient Set<ListenerEntry> listeners = new TreeSet<>();
	private transient Set<Observable> parents = new HashSet<>();
	private transient Set<Observable> children = new HashSet<>();

	public void addListener(ChangeListener callback) {
		addListener(callback, ListenerPriority.NORMAL);
	}

	public void addListener(ChangeListener callback, ListenerPriority priority) {
		addListener(callback, priority.value);
	}

	public void addListener(ChangeListener callback, int priority) {
		if(listeners.stream().noneMatch(listener -> listener.listener == callback)) {
			listeners.add(new ListenerEntry(callback, priority));
		}
	}

	public void removeListener(ChangeListener callback) {
		listeners.stream().filter(listener -> listener.listener == callback).findFirst().ifPresent(found -> listeners.remove(found));
	}

	protected void addParent(Observable observable) {
		parents.add(observable);
	}

	protected void addSubObservable(Observable observable) {
		initSubObservable(observable);
		if(observable instanceof SettableObservable) {
			SettableObservable settableObservable = (SettableObservable) observable;
			if(settableObservable.present()) {
				initSubObservable((Observable) settableObservable.modelValue);
			}
		}
	}

	protected void initSubObservable(Observable model) {
		model.addParent(this);
		children.add(model);
		model.setUnchanged();
	}

	protected void removeSubObservable(Observable model) {
		model.parents.remove(this);
		children.remove(model);
	}

	protected void onValueChanged() {
		parents.forEach(Observable::onValueChanged);
		wasValueChanged = true;
		if (!notifyManually) {
			listeners.forEach(entry -> entry.listener.call());
		}
	}

	private void collectListeners(TreeTraverseDirection direction, Set<ListenerEntry> treeListeners) {
		if(wasValueChanged && notifyManually) {
			wasValueChanged = false;
			treeListeners.addAll(listeners);
		}
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(observable -> observable.collectListeners(direction, treeListeners));
	}

	public void notifyListeners() {
		Set<ListenerEntry> treeListeners = new TreeSet<>();
		collectListeners(TreeTraverseDirection.UP, treeListeners);
		collectListeners(TreeTraverseDirection.DOWN, treeListeners);
		treeListeners.forEach(entry -> entry.listener.call());
	}

	public void copyListeners(Observable observable) {
		listeners.forEach(listener -> observable.addListener(listener.listener, listener.priority));
	}

	public void clearListeners() {
		listeners.clear();
	}

	public void setUnchanged() {
		setUnchanged(TreeTraverseDirection.UP);
		setUnchanged(TreeTraverseDirection.DOWN);
	}

	private void setUnchanged(TreeTraverseDirection direction) {
		wasValueChanged = false;
		Set<Observable> relatives = direction == TreeTraverseDirection.UP ? parents : children;
		relatives.forEach(relative -> relative.setUnchanged(direction));
	}

	private enum TreeTraverseDirection {
		UP, DOWN
	}

}
