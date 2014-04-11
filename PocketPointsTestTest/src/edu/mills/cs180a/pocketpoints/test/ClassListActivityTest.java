package edu.mills.cs180a.pocketpoints.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.mills.cs180a.pocketpoints.ClassListActivity;

public class ClassListActivityTest extends ActivityInstrumentationTestCase2<ClassListActivity> {

	public ClassListActivityTest() {
        super(ClassListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// Note that our tear down code must go before the call to super.tearDown(),
		// which apparently nulls out our instance variables.
		super.tearDown();
	}
}
