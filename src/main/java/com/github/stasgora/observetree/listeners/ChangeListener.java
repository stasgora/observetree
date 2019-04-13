package com.github.stasgora.observetree.listeners;

/**
 * Represents a listener that subscribes to the changes of {@link com.github.stasgora.observetree.Observable}
 * <p>This is a <i>functional interface</i> whose functional method is {@link #call()}</p>
 *
 * @author Stanisław Góra
 * @see ListenerEntry
 */
@FunctionalInterface
public interface ChangeListener {
	/**
	 * Gets called whenever the subscribed {@link com.github.stasgora.observetree.Observable} is changed.
	 */
	void call();
}
