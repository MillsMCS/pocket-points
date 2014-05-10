package edu.mills.cs180a.pocketpoints;

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
 * Fragment to display a list of students available for editing. Clicking on a student notifies an
 * {@link OnEditStudentSelectedListener}. Clicking on the 'Done' button will return the user to the
 * previously created {@link ClasslistFragment}.
 * 
 * @author chingmyu@gmail.com (Ching Yu)
 * @author renee.johnston@gmail.com (Renee Johnston)
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class EditClasslistFragment extends BitmapListFragment {
    private static final String KEY_CURRENTLY_DISPLAYED =
            "edu.mills.cs180a.pocketpoints.EditClasslistFragment.being_displayed";

    private LayoutInflater mInflater;
    private EditClasslistAdapter mAdapter;

    /**
     * Interface definition for the callback to be invoked when a student in the edit class list is
     * selected.
     *
     * @author chingmyu@gmail.com (Ching Yu)
     */
    protected interface OnEditStudentSelectedListener {
        /**
         * Called when a student in the list is selected.
         *
         * @param student the selected student
         */
        public void onEditStudentSelected(Student selectedStudent);
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
        inflater.inflate(R.menu.edit_classlist_options, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = mInflater.inflate(R.layout.fragment_edit_classlist, container, false);

        // Set up the ListView adapter.
        mAdapter = new EditClasslistAdapter(getActivity());
        setListAdapter(mAdapter);

        // Determine if this fragment should be displayed.
        boolean currentlyDisplayed = false; // By default, this fragment should be hidden.
        if (savedInstanceState != null) {
            currentlyDisplayed = savedInstanceState.getBoolean(KEY_CURRENTLY_DISPLAYED, false);
        }

        // Hide the fragment, if necessary.
        if (!currentlyDisplayed) {
            getFragmentManager().beginTransaction().hide(this).commit();
        }

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnEditStudentSelectedListener listener = (OnEditStudentSelectedListener) getActivity();
        Student selectedStudent = (Student) getListAdapter().getItem(position);
        listener.onEditStudentSelected(selectedStudent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save whether or not this fragment is currently being displayed.
        boolean currentlyDisplayed = isVisible();
        savedInstanceState.putBoolean(KEY_CURRENTLY_DISPLAYED, currentlyDisplayed);
    }

    private class EditClasslistAdapter extends ArrayAdapter<Student> {
        private EditClasslistAdapter(Context context) {
            super(context, R.layout.fragment_edit_classlist_row, R.id.rowStudentName,
                    StudentManager.get(context).getAllStudents());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.fragment_edit_classlist_row, null);
            }

            // Get the student for the current row.
            Student student = getItem(position);

            // Set the picture of the student in the row.
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

            return convertView;
        }
    }
}
