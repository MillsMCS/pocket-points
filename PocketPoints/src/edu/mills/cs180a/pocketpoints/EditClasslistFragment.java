package edu.mills.cs180a.pocketpoints;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
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
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 * @author chingmyu@gmail.com (Ching Yu)
 */
public class EditClasslistFragment extends ListFragment {
    private static final String TAG = "EditClasslistFragment";

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
            // StudentCursor studentCursor =
            // StudentManager.get(getActivity()).getAllStudentsCursor();
            // mAdapter.changeCursor(studentCursor); // closes old cursor
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

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnEditStudentSelectedListener listener = (OnEditStudentSelectedListener) getActivity();
        Student selectedStudent = (Student) getListAdapter().getItem(position);
        listener.onEditStudentSelected(selectedStudent);
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
                studentImageView.setImageResource(R.drawable.ic_contact_picture);
            } else {
                Bitmap studentProfilePhoto = ImageUtils.loadImage(getActivity(), studentImgPath,
                        R.drawable.ic_contact_picture);
                studentImageView.setImageBitmap(studentProfilePhoto);
            }

            // Set the name of the student in the row.
            TextView nameTextView = (TextView) convertView.findViewById(R.id.rowStudentName);
            nameTextView.setText(student.getName());

            return convertView;
        }
    }
}
