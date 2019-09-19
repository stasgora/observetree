/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.model;

import io.github.stasgora.observetree.Observable;
import io.github.stasgora.observetree.SettableObservable;

public class SettableTestObservable<T extends Observable> extends SettableObservable<T> {
	public SettableTestObservable() {}

	public SettableTestObservable(T modelValue) {
		super(modelValue);
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
