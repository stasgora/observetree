/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.model;

import io.github.stasgora.observetree.Observable;

public class TestObservable extends Observable {
	private int value;

	public void setValue(int value) {
		this.value = value;
		onValueChanged();
	}

	@Override
	public void addSubObservable(Observable observable) {
		super.addSubObservable(observable);
	}

	@Override
	public void removeSubObservable(Observable observable) {
		super.removeSubObservable(observable);
	}
}
