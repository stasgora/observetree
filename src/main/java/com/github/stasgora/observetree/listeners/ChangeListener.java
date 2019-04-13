package com.github.stasgora.observetree.listeners;

/**
 * Represents a listener that subscribes to the changes of {@link com.github.stasgora.observetree.Observable}
 *
 * @author Stanisław Góra
 * @see ListenerEntry
 */
@FunctionalInterface
public interface ChangeListener {
	void call();
}
