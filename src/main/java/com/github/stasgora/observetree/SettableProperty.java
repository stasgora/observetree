package com.github.stasgora.observetree;

import java.io.Serializable;
import java.util.function.UnaryOperator;

/**
 * Notifies listeners when model value is set
 *
 * @param <T> the type of the actual observable data type
 *
 * @author Stanisław Góra
 * @see SettableProperty
 * @see SettableObservable
 */
public class SettableProperty<T> extends Observable implements Serializable {

	protected T modelValue;
	protected T defaultValue;

	private static final long serialVersionUID = 1L;

	public SettableProperty() {
	}

	public SettableProperty(T modelValue) {
		this.modelValue = modelValue;
	}

	public boolean present() {
		return modelValue != null;
	}

	public T get() {
		return modelValue;
	}

	public void set(T modelValue) {
		if(this.modelValue == modelValue) {
			return;
		}
		this.modelValue = modelValue;
		onValueChanged();
	}

	public void setAndNotify(T modelValue) {
		set(modelValue);
		notifyListeners();
	}

	public void modify(UnaryOperator<T> operator) {
		set(operator.apply(modelValue));
	}

	public void resetToDefaultValue() {
		if(defaultValue != null) {
			set(defaultValue);
		}
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void saveAsDefaultValue() {
		this.defaultValue = modelValue;
	}

}
