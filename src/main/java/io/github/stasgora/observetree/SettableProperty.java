/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A wrapper class that encapsulates another object - {@link #modelValue} - and notifies it's listeners when it is set.
 * {@code equals()} method is used to determine if {@link #defaultValue} was changed.
 * It supports {@link #defaultValue} for the {@link #modelValue} that can be set and reset to.
 *
 * @param <T> the type of the actual observable data type
 *
 * @author Stanisław Góra
 * @see SettableObservable
 */
public class SettableProperty<T> extends Observable implements Serializable {

	/**
	 * Current value of the encapsulated object
	 */
	protected T modelValue;
	/**
	 * Default value of the encapsulated object
	 */
	protected T defaultValue;

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new, empty {@code SettableProperty} with {@code null} {@link #modelValue}
	 */
	public SettableProperty() {
	}

	/**
	 * Constructs a new {@code SettableProperty} with the specified {@link #modelValue}
	 * @param modelValue a value to be set
	 */
	public SettableProperty(T modelValue) {
		this.modelValue = modelValue;
	}

	/**
	 * Checks if the {@link #modelValue} is not {@code null}
	 * @return {@code false} if the {@link #modelValue} is {@code null}, {@code true} otherwise
	 */
	public boolean present() {
		return modelValue != null;
	}

	/**
	 * Returns the current {@link #modelValue} of this {@code SettableProperty}
	 * @return the current {@link #modelValue}
	 */
	public T get() {
		return modelValue;
	}

	/**
	 * Sets the {@link #modelValue} of this {@code SettableProperty}
	 * @param modelValue value to be set
	 */
	public void set(T modelValue) {
		if(Objects.equals(this.modelValue, modelValue)) {
			return;
		}
		this.modelValue = modelValue;
		onValueChanged();
	}

	/**
	 * Sets the {@link #modelValue} and calls {@link #notifyListeners()}
	 * @param modelValue value to be set
	 */
	public void setAndNotify(T modelValue) {
		set(modelValue);
		notifyListeners();
	}

	/**
	 * Modifies the {@link #modelValue} of this {@code SettableProperty} inline
	 * @param operator operation to be performed on {@link #modelValue}
	 */
	public void modify(UnaryOperator<T> operator) {
		set(operator.apply(modelValue));
	}

	/**
	 * Sets the {@link #modelValue} of this {@code SettableProperty} to the {@link #defaultValue}.
	 * If the {@link #defaultValue} is {@code null} no action will be performed.
	 */
	public void resetToDefaultValue() {
		if(defaultValue != null) {
			set(defaultValue);
		}
	}

	/**
	 * Returns the {@link #defaultValue} of this {@code SettableProperty}
	 * @return the {@link #defaultValue} of this object
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the {@link #defaultValue} of this {@code SettableProperty}
	 * @param defaultValue  value to be set
	 */
	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Sets the {@link #defaultValue} of this {@code SettableProperty} to be equal to the current {@link #modelValue}
	 */
	public void saveAsDefaultValue() {
		setDefaultValue(modelValue);
	}

}
