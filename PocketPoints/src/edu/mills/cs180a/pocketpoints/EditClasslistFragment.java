package edu.mills.cs180a.pocketpoints;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment to display a list of students available for editing. Clicking on a
 * student notifies an {@link OnEditStudentSelectedListener}. Clicking on the
 * 'Done' button will return the user to the previously created
 * {@link ClasslistFragment}.
 * 
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 * @author chingmyu@gmail.com (Ching Yu)
 */
public class EditClasslistFragment extends Fragment {
    private LayoutInflater mInflater;
    private List<Student> mStudentList;
    private StudentManager mStudentManager;

    /**
     * Interface definition for the callback to be invoked when a student in the
     * edit class list is selected.
     * 
     * @author chingmyu@gmail.com (Ching Yu)
     */
    protected interface OnEditStudentSelectedListener {
        /**
         * Called when a student in the list is selected.
         * 
         * @param student
         *            the selected student
         */
        public void onEditStudentSelected(int studentId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentManager = StudentManager.get(getActivity());
        mStudentList = mStudentManager.getAllStudents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_classlist, container,
                false);

        // Set up the adapter.
        Activity activity = getActivity();
        ArrayAdapter<Student> adapter = new EditStudentArrayAdapter(activity);
        ListView listview = (ListView) view.findViewById(R.id.listView1);
        listview.setAdapter(adapter);

        return view;
    }

    private class EditStudentArrayAdapter extends ArrayAdapter<Student> {
        EditStudentArrayAdapter(Context context) {
            super(context, R.layout.fragment_edit_classlist_row,
                    R.id.rowStudentName, mStudentList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = mInflater.inflate(
                        R.layout.fragment_edit_classlist_row, null);
            }

            Student student = getItem(position);
            ImageView picture = (ImageView) convertView
                    .findViewById(R.id.rowStudentPicture);
            // TODO: Need to get an image resource ID
            // picture.setImageResource(student.getName());
            picture.setImageResource(R.drawable.ic_contact_picture);

            TextView name = (TextView) convertView
                    .findViewById(R.id.rowStudentName);
            name.setText(student.getName());

            TextView stickerCount = (TextView) convertView
                    .findViewById(R.id.rowStickerCount);
            stickerCount.setText(String.valueOf(student.getNumStickers()));

            return convertView;
        }
    }
}
