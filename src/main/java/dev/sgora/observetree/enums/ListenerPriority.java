/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package dev.sgora.observetree.enums;

import dev.sgora.observetree.listener.ChangeListener;

/**
 * A utility enum defining five levels of {@link ChangeListener} priority
 *
 * @author Stanisław Góra
 * @see ChangeListener
 */
public enum ListenerPriority {

	/**
	 * Very high priority: 2
	 */
	VERY_HIGH(2),
	/**
	 * High priority: 1
	 */
	HIGH(1),
	/**
	 * Normal priority: 0
	 */
	NORMAL(0),
	/**
	 * Low priority: -1
	 */
	LOW(-1),
	/**
	 * Very low priority: -2
	 */
	VERY_LOW(-2);

	/**
	 * Numeric priority of the enum value
	 */
	public final int value;

	ListenerPriority(int value) {
		this.value = value;
	}

}
