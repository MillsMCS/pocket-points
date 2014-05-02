package edu.mills.cs180a.pocketpoints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * {@code EditStudentFragment} is displayed whenever the {@link ClassListFragment} "Add Student"
 * button is pressed or when a student's row in the {@link EditClassListFragment} is selected.
 *
 * <p>
 * For an existing student, the fragment view displays an image of the selected student, the
 * student's name, and a {@link TextField} to edit the student's name. Clicking on the student's
 * picture will launch the device's embedded camera application, regardless of whether the picture
 * has been set or not.
 * </p>
 *
 * <p>
 * If the student has already been added, clicking on the Delete button will delete the student from
 * the database; otherwise, it will have no effect. Clicking on the Save button will either update a
 * previous database entry or add the student to the database.
 * </p>
 *
 * @author Renee Johnston (renee.johnston1149@gmail.com)
 */
public class EditStudentFragment extends Fragment {
    private static final String TAG = "EditStudentFragment";
    private static final String DEFAULT_NAME = "New Student";
    private static final int REQUEST_TAKE_PHOTO = 1;

    private EditText mNameField;
    private Student mStudent;
    private StudentManager mStudentManager;
    private ImageButton mImageButton;

    /**
     * Interface definition for a callback to be invoked when a {@link Student} is selected in the
     * list view.
     *
     * @author renee.johnston1149@gmail.com (Renee Johnston)
     */
    interface OnEditStudentButtonClickedListener {

        /**
         * Called when a {@code Student} is selected.
         *
         * @param personId the ID of the selected person
         */
        void onEditStudentButtonClicked(int buttonResId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_student, container, false);
        mNameField = (EditText) view.findViewById(R.id.editStudentName);
        mImageButton = (ImageButton) view.findViewById(R.id.studentImageButton);
        mStudentManager = StudentManager.get(getActivity());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult photo taken!");
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    mImageButton.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    /**
     * Sets the student whose information is displayed in this {@code EditStudentFragment}.
     *
     * <p>
     * We expect this to be called by the {@code OnSelectStudentListener} which should supply a
     * {@code studentId} argument of -1 in the event of creating a new student.
     * </p>
     *
     * @param personId the ID of the recipient
     */
    void setStudent(long studentId) {
        TextView displayName = (TextView) getView().findViewById(R.id.studentName);

        // If this is a new student display fields with defaults.
        if (studentId == Student.INVALID_ID) {
            mStudent = new Student();
            displayName.setText(DEFAULT_NAME);
            mNameField.setText("");
        } else {
            mStudent = mStudentManager.getStudent(studentId);

            // Display the Students name at the top of the screen (if it
            // exists).
            String name = mStudent.getName();
            displayName.setText(name);

            // Show a picture of the student. (if it exists).
            // icon.setImageResource(R.id.ic_launcher);
            // TODO get images working

            // Set the text of the name EditText to the value of the current
            // name, if any.
            mNameField.setText(name);
        }

        // Add listeners.
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        Button saveButton = (Button) getView().findViewById(R.id.studentSaveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "Save button clicked");
                saveCurrentStudent();
                OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                listener.onEditStudentButtonClicked(R.id.studentSaveButton);
            }
        });

        Button deleteButton = (Button) getView().findViewById(R.id.studentDeleteButton);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteCurrentStudent();
                // TODO: implement delete dialog.
                OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                listener.onEditStudentButtonClicked(R.id.studentDeleteButton);
            }
        });

        Button cancelButton = (Button) getView().findViewById(R.id.studentCancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                listener.onEditStudentButtonClicked(R.id.studentCancelButton);
            }
        });

    }

    // TODO: implement this within the deleteButton listener.
    private AlertDialog createDeleteStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.verify_delete_comment_text).setTitle(R.string.delete_button);
        builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCurrentStudent();
            }
        });
        builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing.
            }
        });
        return builder.create();
    }

    private void deleteCurrentStudent() {

        // If the student has a valid Id.
        if (mStudent.getId() != Student.INVALID_ID) {
            boolean deleted = mStudentManager.deleteStudent(mStudent.getId());
            if (deleted) {
                Toast.makeText(getActivity(), R.string.delete_success_toast, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getActivity(), R.string.delete_failure_toast, Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.delete_success_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCurrentStudent() {
        Log.d(TAG, "saveCurrentStudent() method called");
        mStudent.setName(mNameField.getText().toString());
        // mStudent.setImgName(imgName);

        // Try to save the student in the database.
        boolean saved = false;
        if (mStudent.getId() == Student.INVALID_ID) {
            Log.d(TAG, "New Student to be saved ");
            saved = mStudentManager.createStudent(mStudent);
            Log.d(TAG, "New Student should be saved: "
                    + mStudentManager.getStudent(mStudent.getId()).toString());
        } else {
            saved = mStudentManager.updateStudent(mStudent);
        }

        // Inform the user if the student was saved.
        if (saved) {
            Toast.makeText(getActivity(), R.string.saved_success_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.saved_failure_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void takePicture() {
        PackageManager pm = getActivity().getPackageManager();
        if (pm != null && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Then take the picture.
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(getActivity(), R.string.no_camera_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
}