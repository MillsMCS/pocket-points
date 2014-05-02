package edu.mills.cs180a.pocketpoints;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

/**
 * Activity that decides which fragment to display.
 *
 * @author chingmyu@gmail.com (Ching Yu)
 */
public class MainActivity extends Activity implements ClasslistFragment.OnStudentSelectedListener,
        EditClasslistFragment.OnEditStudentSelectedListener,
        EditStudentFragment.OnEditStudentButtonClickedListener {
    private static final String TAG = "MainActivity";

    private FragmentManager mFragmentManager;
    private StickerChartFragment mStickerChartFragment;
    private EditStudentFragment mEditStudentFragment;
    private ClasslistFragment mClasslistFragment;
    private EditClasslistFragment mEditClasslistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to fragment manager and fragments.
        mFragmentManager = getFragmentManager();
        mEditStudentFragment = (EditStudentFragment) mFragmentManager
                .findFragmentById(R.id.editStudentFragment);
        mClasslistFragment = (ClasslistFragment) mFragmentManager
                .findFragmentById(R.id.classlistFragment);
        mEditClasslistFragment = (EditClasslistFragment) mFragmentManager
                .findFragmentById(R.id.editClasslistFragment);
        mStickerChartFragment = (StickerChartFragment) mFragmentManager
                .findFragmentById(R.id.stickerChartFragment);

        mFragmentManager
                .beginTransaction()
                .hide(mEditStudentFragment)
                .hide(mEditClasslistFragment)
                .hide(mStickerChartFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_edit_students:
            mFragmentManager.beginTransaction()
                    .hide(mClasslistFragment)
                    .show(mEditClasslistFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        case R.id.menu_item_add_student:
            mFragmentManager.beginTransaction()
                    .hide(mClasslistFragment)
                    .show(mEditStudentFragment)
                    .addToBackStack(null)
                    .commit();

            // Tell the EditStudentFragment that it is creating a new student.
            mEditStudentFragment.setStudent(Student.INVALID_ID);
            return true;
        case R.id.menu_item_done_edit:
            mFragmentManager.popBackStack();
            return true;
        case R.id.menu_item_clear_all_stickers:
            // TODO: call method here to remove all stickers
            return true;
        case R.id.menu_item_undo_sticker_addition:
            // TODO: call method here to remove one sticker
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStudentSelected(Student selectedStudent) {
        long selectedPersonId = selectedStudent.getId();
        Log.d(TAG, "Student id is " + selectedPersonId);

        // Display the EditStudentFragment.
        mFragmentManager
                .beginTransaction()
                .hide(mClasslistFragment)
                .show(mStickerChartFragment)
                .addToBackStack(null)
                .commit();

        // Show the current person.
        mStickerChartFragment.setStickersForStudent(selectedPersonId);
    }

    @Override
    public void onEditStudentSelected(Student selectedStudent) {
        long selectedPersonId = selectedStudent.getId();

        // Display the EditStudentFragment.
        mFragmentManager
                .beginTransaction()
                .hide(mEditClasslistFragment)
                .show(mEditStudentFragment)
                .addToBackStack(null)
                .commit();

        // Show the current person.
        mEditStudentFragment.setStudent(selectedPersonId);
    }

    @Override
    public void onEditStudentButtonClicked(int buttonResId) {
        // Return to the fragment we just came from.
        mFragmentManager.popBackStack();
    }
}
