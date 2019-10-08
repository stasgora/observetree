/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package dev.sgora.observetree.enums;

import dev.sgora.observetree.Observable;
import dev.sgora.observetree.listener.ChangeListener;

/**
 * Observable listener notification method
 *
 * @author Stanisław Góra
 * @see ChangeListener
 */
public enum ListenerNotification {
	/**
	 * Manually, through {@link Observable#notifyListeners()} (useful for complex multistage changes)
	 */
	MANUAL,
	/**
	 * Immediately after a {@link Observable#onValueChanged()} was called
	 */
	AUTOMATIC
}
