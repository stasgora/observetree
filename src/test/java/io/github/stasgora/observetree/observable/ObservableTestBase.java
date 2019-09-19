/*
 * Copyright 2019 Stanisław Góra
 *
 * This file is part of Observetree library which is released under MIT License.
 * See LICENSE file or go to http://www.opensource.org/licenses/mit-license.php for full license details.
 */

package io.github.stasgora.observetree.observable;

import io.github.stasgora.observetree.TestBase;
import io.github.stasgora.observetree.model.TestObservable;
import org.junit.Before;

public abstract class ObservableTestBase extends TestBase {
	protected TestObservable observable;

	@Before
	public void prepareObjects() {
		super.prepareObjects();
		observable = new TestObservable();
	}
}
