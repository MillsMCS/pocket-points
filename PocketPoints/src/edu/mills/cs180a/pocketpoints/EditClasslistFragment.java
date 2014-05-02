package edu.mills.cs180a.pocketpoints;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        setListAdapter(new EditClasslistAdapter(getActivity())); // Set up the ListView adapter.
        return view;
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
            return mInflater.inflate(R.layout.fragment_edit_classlist_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Get the student for the current row.
            Student student = getCursor().getStudent();

            // Populate the row's fields with the student data.
            ImageView studentImageView = (ImageView) view.findViewById(R.id.rowStudentPicture);
            String imgName = student.getImgName();
            if (imgName == null) {
                studentImageView.setImageResource(R.drawable.ic_contact_picture);
            } else {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inPurgeable = true;

                Bitmap studentProfilePhoto = BitmapFactory.decodeFile(imgName, bmOptions);
                studentImageView.setImageBitmap(studentProfilePhoto);
            }

            TextView name = (TextView) view.findViewById(R.id.rowStudentName);
            name.setText(student.getName());
        }
    }
}
