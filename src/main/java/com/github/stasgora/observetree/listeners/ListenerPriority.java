package com.github.stasgora.observetree.listeners;

/**
 * A convenience enum defining five levels of {@link ChangeListener} priority
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
	public int value;

	ListenerPriority(int value) {
		this.value = value;
	}

}
