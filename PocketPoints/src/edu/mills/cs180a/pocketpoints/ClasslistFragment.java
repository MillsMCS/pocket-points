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
    private List<Student> mStudentList;
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
        mStudentList = mStudentManager.getAllStudents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_classlist, container, false);

        // Set up the adapter.
        Activity activity = getActivity();
        ArrayAdapter<Student> adapter = new StudentArrayAdapter(activity);
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        return view;
    }

    private class StudentArrayAdapter extends ArrayAdapter<Student> {
        StudentArrayAdapter(Context context) {
            super(context, R.layout.fragment_classlist_row, R.id.rowStudentName, mStudentList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.fragment_classlist_row, null);
            }
            Student student = getItem(position);
            ImageView picture = (ImageView) convertView.findViewById(R.id.rowStudentPicture);
            // TODO: Need to get an image resource ID
            // picture.setImageResource(student.getImgName());
            picture.setImageResource(R.drawable.ic_contact_picture);

            TextView name = (TextView) convertView.findViewById(R.id.rowStudentName);
            name.setText(student.getName());

            TextView stickerCount = (TextView) convertView.findViewById(R.id.rowStickerCount);
            stickerCount.setText(String.valueOf(student.getNumStickers()));

            return convertView;
        }
    }
}
