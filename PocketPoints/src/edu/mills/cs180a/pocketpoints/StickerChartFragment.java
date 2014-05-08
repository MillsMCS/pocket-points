package edu.mills.cs180a.pocketpoints;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An {@code Fragment} called by {@link EditStickerActivity} that displays a graphical
 * {@code GridView} representation of the number of stickers currently associated with a the
 * selected student.
 *
 * <P>
 * After clicking on the "empty sticker", the user is given the choice (via an alert dialog) of
 * adding or canceling the sticker. If added, the student's sticker count will increment by one, the
 * new sticker count is added to the database, and the result code {@link Activity#RESULT_OK} is
 * provided to the parent activity. Otherwise, the database is not modified, and the result code
 * {@link Activity#RESULT_CANCELED} is provided.
 *
 * <P>
 * Clicking the "Undo" button will, after confirming via an alert dialog, erase the most recently
 * added sticker.
 *
 * <P>
 * Clicking the "Clear All" button will, after confirming via an alert dialog, clear all stickers
 * associated with the student and will update the database accordingly.
 *
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 */
public class StickerChartFragment extends Fragment {
    private static final String TAG = "StickerChartFragment";
    private static final String KEY_CURRENTLY_DISPLAYED =
            "edu.mills.cs180a.pocketpoints.StickerChartFragment.being_displayed";
    private static final String KEY_STUDENT =
            "edu.mills.cs180a.pocketpoints.StickerChartFragment.displayed_student";

    private GridView mGridView;
    private GridViewCustomAdapter mAdapter;
    private LayoutInflater mInflater;
    private StudentManager mStudentManager;
    private Student mStudent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentManager = StudentManager.get(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sticker_chart_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_undo_sticker_addition:
            if (mStudent.getNumStickers() > 0) {
                mStudent.removeLastSticker();
                mStudentManager.updateStudent(mStudent);
                setStickersForStudent(mStudent.getId());
            }
            return true;
        case R.id.menu_item_clear_all_stickers:
            if (mStudent.getNumStickers() > 0) {
                mStudent.setNumStickers(0);
                mStudentManager.updateStudent(mStudent);
                setStickersForStudent(mStudent.getId());
            }
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_sticker_chart, container, false);

        // Set up the adapter.
        Activity activity = getActivity();
        mAdapter = new GridViewCustomAdapter(activity);
        mAdapter.setNotifyOnChange(true);
        mGridView = (GridView) view.findViewById(R.id.gridView1);
        mGridView.setAdapter(mAdapter);

        // Determine if this fragment should be displayed.
        boolean currentlyDisplayed = false; // By default, this fragment should be hidden.
        if (savedInstanceState != null) {
            currentlyDisplayed = savedInstanceState.getBoolean(KEY_CURRENTLY_DISPLAYED, false);
            if (currentlyDisplayed) {
                // Get the student currently being displayed (if any).
                long studentId = savedInstanceState.getLong(KEY_STUDENT, Student.INVALID_ID);
                if (studentId != Student.INVALID_ID) {
                    setStickersForStudent(studentId, view);
                }
            }
        }

        // Hide this fragment, if necessary.
        if (!currentlyDisplayed) {
            getFragmentManager().beginTransaction().hide(this).commit();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save whether or not this fragment is currently being displayed.
        boolean currentlyDisplayed = isVisible();
        savedInstanceState.putBoolean(KEY_CURRENTLY_DISPLAYED, currentlyDisplayed);

        if (currentlyDisplayed) {
            // Save the student being displayed.
            if (mStudent != null) {
                savedInstanceState.putLong(KEY_STUDENT, mStudent.getId());
            }
        }
    }

    /**
     * Sets the sticker count for the student whose chart is displayed in this instance of the
     * {@code StickerChartFragment}.
     *
     * <p>
     * We expect this to be called by the {@code OnSelectStudentListener}.
     * </p>
     *
     * @param personId the ID of the student whose stickers are being displayed
     */
    void setStickersForStudent(long studentId) {
        setStickersForStudent(studentId, getView());
    }

    private void setStickersForStudent(long studentId, View fragmentView) {
        mAdapter.clear();

        // Get the student (if any) associated with this ID.
        mStudent = null;
        if (studentId != Student.INVALID_ID) {
            mStudent = mStudentManager.getStudent(studentId);
        }

        if (mStudent == null) {
            Log.w(TAG, "invalid student ID");
        } else {
            mStudent = mStudentManager.getStudent(studentId);

            // Get sticker count.
            int stickerCount = mStudent.getNumStickers();

            //Display student's name:
            TextView displayName = (TextView) fragmentView.findViewById(R.id.nameHeaderSticker);
            displayName.setText(mStudent.getName() + " has " + stickerCount + " stickers!");

            // Add required number of stickers to mGridArray.
            for (int i = 0; i < stickerCount; i++) {
                mAdapter.add(R.drawable.ic_smile_sticker);
            }
        }
        mAdapter.add(R.drawable.ic_add_sticker);
    }

    private class GridViewCustomAdapter extends ArrayAdapter<Integer> {
        public GridViewCustomAdapter(Context context) {
            super(context, R.id.rowSmileImage);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fragment_sticker_chart_row, null);
            }
            int itemResId = getItem(position);
            ImageView sticker = (ImageView) convertView.findViewById(R.id.rowSmileImage);
            sticker.setImageResource(itemResId);

            // If the add smiley icon was just added, increment student's sticker count by 1.
            if (itemResId != R.drawable.ic_add_sticker) {
                sticker.setOnClickListener(null);
            } else {
                sticker.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mStudent.addSticker();
                        mStudentManager.updateStudent(mStudent);
                        setStickersForStudent(mStudent.getId());
                    }
                });
            }
            return convertView;
        }
    }
}
