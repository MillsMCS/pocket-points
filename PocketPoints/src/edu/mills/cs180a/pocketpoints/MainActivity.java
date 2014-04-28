package edu.mills.cs180a.pocketpoints;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity that decides which fragment to display.
 *
 * @author chingmyu@gmail.com (Ching Yu)
 */
public class MainActivity extends Activity
	implements ClassListFragment.OnStudentSelectedListener,
	EditClassListFragment.OnEditStudentSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.classlist_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_edit_students:
			// Display the EditClassListFragment.
			// TODO: Not yet implemented.
			return true;
		case R.id.menu_item_add_student:
			// Display the EditStudentActivity.
			// TODO: Not yet implemented.
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStudentSelected(Student student) {
		// Display the StudentStickerFragment.
		// TODO: Not yet implemented.
	}

	@Override
	public void onEditStudentSelected(Student student) {
		// Display the EditStudentFragment.
		// TODO: Not yet implemented.
	}
}
