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
 * ClassListFragment is displayed as the main page when the application is first launched.
 *
 * The fragment will display a populated list view of student's picture and name. Selecting a
 * student from the list will direct to {@link StudentStickerFragment}. It will also contain two
 * buttons on the upper righthand corner of the screen: 'Edit Student' and 'Add New Student'.
 * Clicking on the 'Edit Student' button will direct to {@link EditClasslistFragment} and the 'Add
 * New Student' button will direct to {@link EditStudentFragment}.
 *
 * @author chingmyu@gmail.com (Ching Yu)
 */
public class ClasslistFragment extends ListFragment {
    private LayoutInflater mInflater;
    private StudentManager mStudentManager;
    private ClasslistAdapter mAdapter;

    /**
     * Interface definition for the callback to be invoked when a student in the class list is
     * selected.
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
            // StudentCursor studentCursor =
            // StudentManager.get(getActivity()).getAllStudentsCursor();
            // mAdapter.changeCursor(studentCursor); // closes old cursor
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentManager = StudentManager.get(getActivity());
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

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnStudentSelectedListener listener = (OnStudentSelectedListener) getActivity();
        Student selectedStudent = (Student) getListAdapter().getItem(position);
        listener.onStudentSelected(selectedStudent);
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
                studentImageView.setImageResource(R.drawable.ic_contact_picture);
            } else {
                Bitmap studentProfilePhoto = ImageUtils.loadImage(getActivity(), studentImgPath,
                        R.drawable.ic_contact_picture);
                studentImageView.setImageBitmap(studentProfilePhoto);
            }

            // Set the name of the student in the row.
            TextView name = (TextView) convertView.findViewById(R.id.rowStudentName);
            name.setText(student.getName());

            // Set the sticker count of the student in the row.
            TextView stickerCount = (TextView) convertView.findViewById(R.id.rowStickerCount);
            stickerCount.setText(String.valueOf(student.getNumStickers()));

            return convertView;
        }
    }
}
