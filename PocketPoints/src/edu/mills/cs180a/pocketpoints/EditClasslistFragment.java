package edu.mills.cs180a.pocketpoints;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private LayoutInflater mInflater;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        // Set up the adapter.
        setListAdapter(new EditClasslistAdapter(getActivity()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnEditStudentSelectedListener listener = (OnEditStudentSelectedListener) getActivity();
        Student selectedStudent = (Student) getListAdapter().getItem(position);
        listener.onEditStudentSelected(selectedStudent);
    }

    private class EditClasslistAdapter extends StudentCursorAdapter {
        private EditClasslistAdapter(Context context) {
            super(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.fragment_edit_classlist_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Get the student for the current row.
            Student student = getCursor().getStudent();

            // Populate the row's fields with the student data.
            ImageView picture = (ImageView) view.findViewById(R.id.rowStudentPicture);
            // TODO: Need to get an image resource ID
            // picture.setImageResource(student.getName());
            picture.setImageResource(R.drawable.ic_contact_picture);

            TextView name = (TextView) view.findViewById(R.id.rowStudentName);
            name.setText(student.getName());

            TextView stickerCount = (TextView) view.findViewById(R.id.rowStickerCount);
            stickerCount.setText(String.valueOf(student.getNumStickers()));
        }

    }
}
