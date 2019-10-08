package dev.sgora.observetree;

import dev.sgora.observetree.listener.ChangeListener;
import org.junit.Before;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public abstract class TestBase {
	protected static final int VALUE_TO_SET = 2;

	protected ChangeListener listener;

	@Before
	public void prepareObjects() {
		listener = mockListener(() -> {});
	}

	protected ChangeListener mockListener(ChangeListener listener) {
		return Mockito.mock(ChangeListener.class, AdditionalAnswers.delegatesTo(listener));
	}

	protected void verifyListenerCalled(ChangeListener listener, int times) {
		Mockito.verify(listener, Mockito.times(times)).call();
	}
}
