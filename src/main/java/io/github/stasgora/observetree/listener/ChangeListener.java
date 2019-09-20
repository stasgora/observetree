/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.listener;

import io.github.stasgora.observetree.Observable;
import io.github.stasgora.observetree.enums.ListenerPriority;

/**
 * Represents a listener that subscribes to the changes of {@link Observable}
 * <p>This is a <i>functional interface</i> whose functional method is {@link #call()}
 *
 * @author Stanisław Góra
 * @see ListenerPriority
 */
@FunctionalInterface
public interface ChangeListener {
	/**
	 * Gets called whenever the subscribed {@link Observable} is changed.
	 */
	void call();
}
