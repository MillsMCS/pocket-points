package edu.mills.cs180a.pocketpoints;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * {@code EditStudentFragment} is displayed whenever the {@link ClassListFragment}
 *  "Add Student" button is pressed or when a student's row in the 
 *  {@link EditClassListFragment} is selected. 
 *
 *<p>
 * For an existing student, the fragment view displays an image of the selected student, 
 * the student's name as a TextView, a TextField to edit the student's name. Clicking on the student's 
 * picture will launch the device's embedded camera application, regardless of whether the picture 
 * has been set or not. 
 *</p>
 *
 *<p>
 * If the student has already been added, clicking on the Delete
 * button will delete the student from the database; otherwise, it will have no effect. Clicking
 * on the Save button will either update a previous database entry or add the Student to the database. 
 *</p>
 *
 * @author Renee Johnston (renee.johnston1149@gmail.com)
 */
public class EditStudentFragment extends Fragment {
	private static final String TAG = "EditStudentFragment";
	public static final String DEFAULT_NAME = "New Student";

	private EditText mNameField;
	private Student mStudent;
	private StudentManager mStudentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_student, container, false);
		mNameField = (EditText) view.findViewById(R.id.editStudentName);
		mStudentManager = StudentManager.get(getActivity());
		return view;
	}

	/**
	 * Sets the student whose information is displayed in this {@code EditStudentFragment}.
	 * 
	 * We expect this to be called by the {@code OnSelectStudentListener} which should
	 * supply a {@code studentId} argument of -1 in the event of creating a new student. 
	 *
	 * @param personId the ID of the recipient
	 */
	void setStudent(long studentId) {

		TextView displayName = (TextView) getView().findViewById(R.id.studentName);
		ImageButton imageButton = (ImageButton) getView().findViewById(R.id.studentImageButton);

		//If this is a new student display fields with defaults
		if (studentId == Student.INVALID_ID){
			mStudent = new Student();
			displayName.setText(DEFAULT_NAME);
			mNameField.setText("");
		} else {
			mStudent = mStudentManager.getStudent(studentId);
			// Display the Students name at the top of the screen (if it exists)
			String name = mStudent.getName();
			displayName.setText(name);

			//Show a picture of the student. (if it exists) 
			//icon.setImageResource(R.id.ic_launcher);
			//TODO get images working

			// Set the text of the name EditText to the value of the current name, if any.
			mNameField.setText(name);
		}


		// Add listeners.
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//take me to the camera!
			}
		});

		Button saveButton = (Button) getView().findViewById(R.id.studentSaveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {				
				saveCurrentStudent();
			}
		});

		Button deleteButton = (Button) getView().findViewById(R.id.studentDeleteButton);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deleteCurrentStudent();
				//createDeleteStudentDialog().show();

				Log.d(TAG, "In delete student: " + mStudentManager.getAllStudents().toString());
			}
		});

		Button cancelButton = (Button) getView().findViewById(R.id.studentCancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//Go back to either classList or editClassList
			}
		});

	}

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
				//do nothing
			}
		});
		return builder.create();
	}

	private void deleteCurrentStudent(){
		if(mStudent.getId() != Student.INVALID_ID){	
			boolean deleted = mStudentManager.deleteStudent(mStudent.getId());	
			if (deleted){
				Toast.makeText(getActivity(), "Student Deleted", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Deletion Failed", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(), "Student Deleted", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveCurrentStudent(){
		mStudent.setName(mNameField.getText().toString());
		//mStudent.setImgName(imgName);

		// Try to save the student in the database.
		boolean saved = false;
		if(mStudent.getId() == Student.INVALID_ID){
			saved = mStudentManager.createStudent(mStudent);
		} else {
			saved = mStudentManager.updateStudent(mStudent);
		}
		
		// Inform the user if the student was saved.
		if (saved){
			Toast.makeText(getActivity(), "Student Saved", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "Saving Failed", Toast.LENGTH_SHORT).show();
		}
	}
}





//public class CommentFragment extends Fragment {
//    private EditText mCommentField;
//    private Person mRecipient;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_comment, container, false);
//        mCommentField = (EditText) view.findViewById(R.id.commentEditText);
//        return view;
//    }
//
//    /**
//     * Sets the recipient whose comment is displayed in this {@code CommentFragment}.
//     *
//     * @param personId the ID of the recipient
//     */
//    void setRecipient(int personId) {
//        mRecipient = Person.everyone[personId];
//
//        // Show a picture of the recipient.
//        ImageView icon = (ImageView) getView().findViewById(R.id.commentImageView);
//        icon.setImageResource(mRecipient.getImageId());
//
//        // Set the text of the comment EditText to the value of the current comment, if any.
//        Comment comment = getCommentForRecipient(mRecipient.getEmail());
//        if (comment != null && comment.getContent() != null) {
//            mCommentField.setText(comment.getContent());
//        } else {
//            mCommentField.setText("");
//        }
//
//        // Add listeners.
//        Button saveButton = (Button) getView().findViewById(R.id.saveCommentButton);
//        saveButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveCurrentComment();
//            }
//        });
//        Button cancelButton = (Button) getView().findViewById(R.id.cancelCommentButton);
//        cancelButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String cancelMessage = getString(R.string.comment_canceled_toast, mRecipient);
//                ((MainActivity) getActivity()).hideCommentFragment(cancelMessage);
//            }
//        });
//        Button clearTextButton = (Button) getView().findViewById(R.id.clearTextButton);
//        clearTextButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                mCommentField.setText("");
//            }
//        });
//        Button deleteButton = (Button) getView().findViewById(R.id.deleteCommentButton);
//        OnClickListener deleteHandler = new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                deleteComment();
//            }
//        };
//        deleteButton.setOnClickListener(deleteHandler);
//        Button mailButton = (Button) getView().findViewById(R.id.mailCommentButton);
//        mailButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                sendEmailTo(mRecipient);
//                createDeleteCommentDialog().show();
//            }
//        });
//    }
//
//    private void saveCurrentComment() {
//        String recipientEmail = mRecipient.getEmail();
//        saveComment(recipientEmail, mCommentField.getText().toString());
//        String commentSavedMessage = getString(R.string.comment_altered_toast,
//                getString(R.string.added_text), mRecipient);
//        ((MainActivity) getActivity()).hideCommentFragment(commentSavedMessage);
//    }
//
//    private void deleteComment() {
//        MainActivity activity = (MainActivity) getActivity();
//        String email = mRecipient.getEmail();
//        Uri uri = Uri.parse(CommentContentProvider.CONTENT_URI + "/" + email);
//        activity.getContentResolver().delete(uri, null, null);
//        String commentDeletedMessage = getString(R.string.comment_altered_toast,
//                getString(R.string.deleted_text), mRecipient);
//        activity.hideCommentFragment(commentDeletedMessage);
//    }
//
//    private AlertDialog createDeleteCommentDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.verify_delete_comment_text).setTitle(R.string.delete_button);
//        builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Delete the comment that was just mailed.
//                deleteComment();
//            }
//        });
//        builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Save the comment that was just mailed.
//                saveCurrentComment();
//            }
//        });
//        return builder.create();
//    }
//
//    private void sendEmailTo(Person person) {
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", person
//                .getEmail(), null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello from " + getString(R.string.app_name));
//        emailIntent.putExtra(Intent.EXTRA_TEXT, mCommentField.getText());
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send Email"));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(getActivity(), "There are no email clients installed.",
//                    Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }
//
//    private Comment getCommentForRecipient(String recipient) {
//        Uri uri = Uri.parse(CommentContentProvider.CONTENT_URI + "/" + recipient);
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        Comment comment = null;
//        if (cursor.moveToFirst()) {
//            comment = new Comment(cursor.getLong(MySQLiteOpenHelper.COLUMN_ID_POS), cursor
//                    .getString(MySQLiteOpenHelper.COLUMN_RECIPIENT_POS), cursor
//                    .getString(MySQLiteOpenHelper.COLUMN_CONTENT_POS));
//            assert cursor.isLast();
//        }
//        cursor.close();
//        return comment;
//    }
//
//    private boolean saveComment(String recipient, String content) {
//        ContentResolver resolver = getActivity().getContentResolver();
//        Uri uri = Uri.parse(CommentContentProvider.CONTENT_URI + "/" + recipient);
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteOpenHelper.COLUMN_RECIPIENT, recipient);
//        values.put(MySQLiteOpenHelper.COLUMN_CONTENT, content);
//        Uri insertedUri = resolver.insert(uri, values);
//        if (insertedUri == null || !insertedUri.equals(uri)) {
//            // Comment already exists in the database and needs to be updated.
//            int updatedRows = resolver.update(uri, values, null, null);
//            return updatedRows > 0;
//        }
//        return true;
//    }
//}

