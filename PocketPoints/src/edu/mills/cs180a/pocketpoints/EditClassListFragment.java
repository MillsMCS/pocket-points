package edu.mills.cs180a.pocketpoints;

import java.util.ArrayList;
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
import edu.mills.cs180a.pocketpoints.ClassListFragment.OnStudentSelectedListener;

/**
 * Fragment to display a list of students available for editing.  Clicking on a student
 * notifies an {@link OnStudentSelectedListener}. Clicking on the "Done" button will return
 * the user to the previously created {@link ClassListFragment}.
 *
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 */
public class EditClassListFragment extends Fragment {
	private LayoutInflater mInflater;
	private List<Student> mStudentList;
	private Student mMyStudent;
	/**
	 * Interface definition for the callback to be invoked
	 * when a student in the class list is selected.
	 */
	protected interface OnEditStudentSelectedListener {
		/**
		 * Called when a student in the list is selected.
		 *
		 * @param student	the selected student
		 */
		public void onEditStudentSelected(Student student);
	}

	/**
	 * Mocks inserting students into the database so they appear
	 * in the EditClassListFragment
	 *
	 * @param id
	 * @param imgName		student's picture
	 * @param name			student's name
	 * @param numSticks		initial number of stickers assigned to student
	 *
	 * @return a student object that has an id, image name, name, and sticker count
	 */
	public Student createStudent(long id, String imgName, String name, int numSticks) {
		Student createdStudent = mMyStudent;
		createdStudent = new Student()
		.setID(id)
		.setImgName(imgName)
		.setName(name)
		.setNumStickers(numSticks);
		return createdStudent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStudentList = new ArrayList<Student>();
		mStudentList.add(createStudent(1, "Ching.png", "Ching", 6));
		mStudentList.add(createStudent(2, "Renee.png", "Renee", 5));
		mStudentList.add(createStudent(3, "Ashley.png", "Ashley", 4));
		mStudentList.add(createStudent(4, "AJ.png", "AJ", 3));
		mStudentList.add(createStudent(5, "Kent.png", "Kent", 2));
		mStudentList.add(createStudent(6, "Zaida.png", "Zaida", 1));
		mStudentList.add(createStudent(7, "Courtney.png", "Courtney", 0));
		mStudentList.add(createStudent(8, "Jodessa.png", "Jodessa", 1));
		mStudentList.add(createStudent(9, "Honomi.png", "Honomi", 2));
		mStudentList.add(createStudent(10, "Kelly.png", "Kelly", 3));
		mStudentList.add(createStudent(11, "Jessica.png", "Jessica", 4));
		mStudentList.add(createStudent(12, "Michele.png", "Michele", 5));
		mStudentList.add(createStudent(13, "Goni.png", "Goni", 6));
		mStudentList.add(createStudent(14, "Valeria.png", "Valeria", 7));
		mStudentList.add(createStudent(15, "Angelica.png", "Angelica", 8));
		mStudentList.add(createStudent(16, "Gabriella.png", "Gabriella", 9));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.classlist_fragment, container, false);

		// Set up the adapter.
		Activity activity = getActivity();
		ArrayAdapter<Student> adapter = new EditStudentArrayAdapter(activity);
		ListView listview = (ListView) view.findViewById(R.id.listView1);
		listview.setAdapter(adapter);

		return view;
	}

	private class EditStudentArrayAdapter extends ArrayAdapter<Student> {
		EditStudentArrayAdapter(Context context) {
			// TODO: Need to call StudentManager.getAllStudents();
			super(context, R.layout.edit_classlist_row, R.id.rowStudentName, mStudentList);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.edit_classlist_row, null);
			}
			Student student = getItem(position);
			ImageView picture = (ImageView) convertView.findViewById(R.id.rowStudentPicture);
			// TODO: Need to get an image resource ID
			//picture.setImageResource(student.getName());
			picture.setImageResource(R.drawable.ic_contact_picture);

			TextView name = (TextView) convertView.findViewById(R.id.rowStudentName);
			name.setText(student.getName());

			TextView stickerCount = (TextView) convertView.findViewById(R.id.rowStickerCount);
			stickerCount.setText(String.valueOf(student.getNumStickers()));

			return convertView;
		}
	}
}
