package edu.mills.cs180a.pocketpoints;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ClasslistFragment extends Fragment {
    private LayoutInflater mInflater;
    private StudentManager mStudentManager;

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
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(new ClasslistAdapter(getActivity()));

        return view;
    }

    private class ClasslistAdapter extends StudentCursorAdapter {
        ClasslistAdapter(Context context) {
            super(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.fragment_classlist_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Get the student for the current row.
            Student student = getCursor().getStudent();

            // Populate the fields with the student data.
            ImageView picture = (ImageView) view.findViewById(R.id.rowStudentPicture);
            // TODO: Need to get an image resource ID
            // picture.setImageResource(student.getImgName());
            picture.setImageResource(R.drawable.ic_contact_picture);

            TextView name = (TextView) view.findViewById(R.id.rowStudentName);
            name.setText(student.getName());

            TextView stickerCount = (TextView) view.findViewById(R.id.rowStickerCount);
            stickerCount.setText(String.valueOf(student.getNumStickers()));
        }
    }
}
