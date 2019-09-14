package io.github.stasgora.observetree;

import io.github.stasgora.observetree.listener.ChangeListener;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public abstract class TestBase {
	protected static final int VALUE_TO_SET = 2;

	protected ChangeListener listener;

	protected ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

	protected void verifyListenerCalled(ChangeListener listener, int times) {
		Mockito.verify(listener, Mockito.times(times)).call();
	}
}
