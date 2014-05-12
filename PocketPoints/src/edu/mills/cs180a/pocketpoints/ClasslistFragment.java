package edu.mills.cs180a.pocketpoints;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A {@link ListFragment} that displays the current list of students in the PocketPoints app.
 * {@code ClasslistFragment} is displayed as the main page when the application is first launched.
 * Clicking on a student from the list notifies an {@link OnStudentSelectedListener}. Clicking on
 * the 'Edit Student' button will direct to {@link EditClasslistFragment} and the 'Add New Student'
 * button will direct to {@link EditStudentFragment}.
 *
 * @author chingmyu@gmail.com (Ching Yu)
 * @author renee.johnston@gmail.com (Renee Johnston)
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class ClasslistFragment extends BitmapListFragment {
    private static final String KEY_CURRENTLY_DISPLAYED =
            "edu.mills.cs180a.pocketpoints.ClasslistFragment.being_displayed";

    private LayoutInflater mInflater;
    private ClasslistAdapter mAdapter;

    /**
     * Interface definition for the callback to be invoked when a {@link Student} in the class list
     * is selected.
     *
     * @author chingmyu@gmail.com (Ching Yu)
     */
    protected interface OnStudentSelectedListener {
        /**
         * Called when a student in the list is selected.
         *
         * @param student the selected student
         */
        public void onStudentSelected(Student student);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mAdapter.clear();
            mAdapter.addAll(StudentManager.get(getActivity()).getAllStudents());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.classlist_options, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        // Set up the adapter.
        mAdapter = new ClasslistAdapter(getActivity());
        setListAdapter(mAdapter);

        // Determine if this fragment should be displayed.
        boolean currentlyDisplayed = true; // By default, this fragment should be displayed.
        if (savedInstanceState != null) {
            currentlyDisplayed = savedInstanceState.getBoolean(KEY_CURRENTLY_DISPLAYED, true);
        }

        // Hide the fragment, if necessary.
        if (!currentlyDisplayed) {
            getFragmentManager().beginTransaction().hide(this).commit();
        }

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnStudentSelectedListener listener = (OnStudentSelectedListener) getActivity();
        Student selectedStudent = (Student) getListAdapter().getItem(position);
        listener.onStudentSelected(selectedStudent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save whether or not this fragment is currently being displayed.
        boolean currentlyDisplayed = isVisible();
        savedInstanceState.putBoolean(KEY_CURRENTLY_DISPLAYED, currentlyDisplayed);
    }

    private class ClasslistAdapter extends ArrayAdapter<Student> {
        private ClasslistAdapter(Context context) {
            super(context, R.layout.fragment_classlist_row, R.id.rowStudentName, StudentManager
                    .get(context)
                    .getAllStudents());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.fragment_classlist_row, null);
            }

            // Get the student for the current row.
            Student student = getItem(position);

            // Populate the fields with the student data.
            ImageView studentImageView = (ImageView) convertView
                    .findViewById(R.id.rowStudentPicture);
            String studentImgPath = student.getImgName();
            if (studentImgPath == null) {
                studentImageView.setImageBitmap(mDefaultProfileImg);
            } else {
                loadBitmap(studentImgPath, studentImageView);
            }

            // Set the name of the student in the row.
            TextView nameTextView = (TextView) convertView.findViewById(R.id.rowStudentName);
            nameTextView.setText(student.getName());

            // Set the sticker count of the student in the row.
            TextView stickerCountTextView = (TextView) convertView
                    .findViewById(R.id.rowStickerCount);
            stickerCountTextView.setText(String.valueOf(student.getNumStickers()));

            return convertView;
        }
    }
}
