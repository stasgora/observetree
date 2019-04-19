/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package com.github.stasgora.observetree;

/**
 * Represents a listener that subscribes to the changes of {@link com.github.stasgora.observetree.Observable}
 * <p>This is a <i>functional interface</i> whose functional method is {@link #call()}</p>
 *
 * @author Stanisław Góra
 * @see ListenerPriority
 */
@FunctionalInterface
public interface ChangeListener {
	/**
	 * Gets called whenever the subscribed {@link com.github.stasgora.observetree.Observable} is changed.
	 */
	void call();
}
