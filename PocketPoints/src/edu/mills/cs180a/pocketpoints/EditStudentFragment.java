package edu.mills.cs180a.pocketpoints;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
    private static final String KEY_STUDENT =
            "edu.mills.cs180a.pocketpoints.EditStudentFragment.displayed_student";
    private static final String KEY_NEW_PROFILE_PHOTO_PATH =
            "edu.mills.cs180a.pocketpoints.EditStudentFragment.new_profile_photo_path";
    private static final String DEFAULT_NAME = "New Student";
    private static final int REQUEST_TAKE_PHOTO = 1;

    private EditText mNameField;
    private Student mStudent;
    private StudentManager mStudentManager;
    private ImageButton mImageButton;

    private String mNewProfilePhotoPath;
    private String mPossibleNewProfilePhotoPath; // This is only used for telling the camera app
                                                 // where to save the photo (an action which may be
                                                 // cancelled).

    /**
     * Interface definition for a callback to be invoked when a {@link Student} is selected in the
     * list view.
     *
     * @author renee.johnston1149@gmail.com (Renee Johnston)
     */
    interface OnEditStudentButtonClickedListener {

        /**
         * Called when the save, delete, or cancel button is selected.
         *
         * @param buttonResId the resourceId of the selected button.
         */
        void onEditStudentButtonClicked(int buttonResId);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentManager = StudentManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_student, container, false);
        mNameField = (EditText) view.findViewById(R.id.editStudentName);
        mImageButton = (ImageButton) view.findViewById(R.id.studentImageButton);

        // Get the student currently being displayed, if any.
        if (savedInstanceState != null) {
            long studentId = savedInstanceState.getLong(KEY_STUDENT, Student.INVALID_ID);
            setStudent(studentId, view);

            CharSequence newProfilePhotoPath = savedInstanceState
                    .getCharSequence(KEY_NEW_PROFILE_PHOTO_PATH);
            if (newProfilePhotoPath != null) {
                mNewProfilePhotoPath = newProfilePhotoPath.toString();
                displayProfilePhoto(mNewProfilePhotoPath);
            }
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            switch (resultCode) {
            case Activity.RESULT_OK:
                mNewProfilePhotoPath = mPossibleNewProfilePhotoPath;
                displayProfilePhoto(mNewProfilePhotoPath); // Display the new profile photo.
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(getActivity(), R.string.no_photo_taken, Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.w(TAG, "Unhandled result code " + resultCode);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (mNewProfilePhotoPath != null) {
                // Then need to delete the image file located at mNewProfilePath (it is not being
                // used by the mStudent, and so should be removed from memory).
                deleteProfilePhoto(mNewProfilePhotoPath);
                Log.d(TAG, mNewProfilePhotoPath + " has been deleted, since it is not being used");
                mNewProfilePhotoPath = null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the student being displayed.
        if (mStudent != null) {
            savedInstanceState.putLong(KEY_STUDENT, mStudent.getId());
        }

        // Save the path to the new profile photo.
        if (mNewProfilePhotoPath != null) {
            savedInstanceState.putCharSequence(KEY_NEW_PROFILE_PHOTO_PATH, mNewProfilePhotoPath);
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
        setStudent(studentId, getView());
    }

    private void setStudent(long studentId, View fragmentView) {
        TextView displayName = (TextView) fragmentView.findViewById(R.id.studentName);

        // Get the student (if any) associated with the given ID.
        mStudent = null;
        if (studentId != Student.INVALID_ID) {
            mStudent = mStudentManager.getStudent(studentId);
        }

        // If this is a new student display fields with defaults.
        if (mStudent == null) {
            mStudent = new Student();
            displayName.setText(DEFAULT_NAME);
            mNameField.setText("");
            displayProfilePhoto(null); // Displays the default image.
        } else {
            String name = mStudent.getName();
            displayName.setText(name);
            mNameField.setText(name);
            displayProfilePhoto(mStudent.getImgName());
        }

        // Add listeners.
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        Button saveButton = (Button) fragmentView.findViewById(R.id.studentSaveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (saveCurrentStudent()) {
                    OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                    listener.onEditStudentButtonClicked(R.id.studentSaveButton);
                }
            }
        });

        Button deleteButton = (Button) fragmentView.findViewById(R.id.studentDeleteButton);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createDeleteStudentDialog().show();
            }
        });

        Button cancelButton = (Button) fragmentView.findViewById(R.id.studentCancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                listener.onEditStudentButtonClicked(R.id.studentCancelButton);
            }
        });

    }

    private boolean deleteCurrentStudent() {
        // Delete the student from the database, if necessary.
        if (mStudent.getId() != Student.INVALID_ID) {
            boolean deleted = mStudentManager.deleteStudent(mStudent.getId());
            if (deleted) {
                Toast.makeText(getActivity(), R.string.delete_success_toast, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getActivity(), R.string.delete_failure_toast, Toast.LENGTH_SHORT)
                        .show();
            }
            return deleted;
        }
        Toast.makeText(getActivity(), R.string.delete_success_toast, Toast.LENGTH_SHORT).show();
        return true;
    }

    private AlertDialog createDeleteStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.verify_delete_student_text).setTitle(R.string.delete_button);
        builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteCurrentStudent()) {
                    OnEditStudentButtonClickedListener listener = (OnEditStudentButtonClickedListener) getActivity();
                    listener.onEditStudentButtonClicked(R.id.studentDeleteButton);
                }
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

    private boolean saveCurrentStudent() {
        String newName = mNameField.getText().toString();
        if (newName.isEmpty()) {
            // Can't save a student with an empty name.
            Toast.makeText(getActivity(), R.string.save_failure_empty_name_toast,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        mStudent.setName(mNameField.getText().toString());

        // Update the student's profile photo, if necessary.
        if (mNewProfilePhotoPath != null) {
            String currentProfilePhotoPath = mStudent.getImgName();
            mStudent.setImgName(mNewProfilePhotoPath);
            if (currentProfilePhotoPath != null) {
                // Then need to delete the student's old profile photo from memory.
                deleteProfilePhoto(currentProfilePhotoPath);
                Log.d(TAG, "Deleted the old student profile photo at: " + currentProfilePhotoPath);
            }
            mNewProfilePhotoPath = null;
        }

        // Try to save the student in the database.
        boolean saved = false;
        if (mStudent.getId() == Student.INVALID_ID) {
            saved = mStudentManager.createStudent(mStudent);
        } else {
            saved = mStudentManager.updateStudent(mStudent);
        }

        // Inform the user if the student was saved.
        if (saved) {
            Toast.makeText(getActivity(), R.string.save_success_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.save_failure_toast, Toast.LENGTH_SHORT).show();
        }
        return saved;
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

        // Ensure that there's a camera activity to handle the intent.
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Unable to write create the file for saving photo.", ex);
                Toast.makeText(getActivity(), R.string.save_photo_failed,
                        Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mPossibleNewProfilePhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create the name of the image file.
        long timeStamp = new Date().getTime(); // Use current timestamp to make image names unique.
        String imageName = "student_profile_" + timeStamp;

        // We want to store the files in a directory which is private for our application.
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return imageFile;
    }

    private void displayProfilePhoto(String profilePhotoPath) {
        if (profilePhotoPath == null) {
            // Display the default (anonymous) profile photo.
            mImageButton.setImageResource(R.drawable.ic_take_picture);
        } else {
            Bitmap profilePhoto = ImageUtils.loadImage(getActivity(), profilePhotoPath,
                    R.drawable.ic_take_picture);
            mImageButton.setImageBitmap(profilePhoto);
        }
    }

    private void deleteProfilePhoto(String profilePhotoPath) {
        File currentProfilePhotoFile = new File(profilePhotoPath);
        if (!currentProfilePhotoFile.delete()) {
            Log.e(TAG, "Deletion of " + profilePhotoPath + " failed!");
        }
    }
}
