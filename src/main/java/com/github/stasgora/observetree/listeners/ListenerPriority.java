package com.github.stasgora.observetree.listeners;

/**
 *
 * @author Stanisław Góra
 * @see ChangeListener
 */
public enum ListenerPriority {

	VERY_HIGH(2),
	HIGH(1),
	NORMAL(0),
	LOW(-1),
	VERY_LOW(-2);

	public int value;

	ListenerPriority(int value) {
		this.value = value;
	}

}
