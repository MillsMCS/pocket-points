package edu.mills.cs180a.pocketpoints;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import edu.mills.cs180a.pocketpoints.StudentSQLiteOpenHelper.StudentCursor;

/**
 * Class that manages all interactions with the {@code students} database in the PocketPoints app.
 * This class abstracts the front-end of the application from the implementation of the back end, so
 * that if the database's implementation was changed, only this class would have to be altered.
 *
 * <p>
 * All of the activities and fragments in the PocketPoints app should access the database through
 * this class. They should not (repeat: *NOT*) access the database implementation directly.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
// Modeled after the RunManager class from the RunTracker app in "Android
// Programming: The Big Nerd Ranch Guide".
public class StudentManager {
    private static StudentManager sStudentManager;

    private StudentSQLiteOpenHelper mHelper;

    /**
     * Gets the singleton instance of {@code StudentManager}. If no instance currently exists,
     * create an instance using the singleton application context provided by the given context.
     * 
     * @param context the context to use
     * @return the singleton instance of {@code StudentManager}
     */
    public static synchronized StudentManager get(Context context) {
        if (sStudentManager == null) {
            // Use the singleton application context to avoid leaking
            // activities.
            return new StudentManager(context.getApplicationContext());
        }
        return sStudentManager;
    }

    /**
     * Creates an instance of {@code StudentManager} for the given test context.
     *
     * <p>
     * <b><i> PLEASE NOTE </i></b> that this method exists only for testing the
     * {@code StudentManager}. Please use it for NO other purpose.
     *
     * @param context the context of the test for which to create this {@code StudentManager}
     *        instance
     * @return the {@code StudentManager} test instance for the given context
     */
    public static StudentManager getTestInstance(Context context) {
        return new StudentManager(context);
    }

    private StudentManager(Context appContext) {
        mHelper = new StudentSQLiteOpenHelper(appContext);
    }

    /**
     * Adds the given student to the database. If the student was successfully added, changes the ID
     * of the student to reflect the student's unique ID in the database. If the student was not
     * added, sets the ID of the student to an invalid value.
     *
     * @param student the student to add
     * @return {@code true} if the student was successfully created in the database; otherwise
     *         {@code false}
     */
    public boolean createStudent(Student student) {
        return mHelper.insertStudent(student) > -1;
    }

    /**
     * Retrieves all of the students from the database.
     *
     * @return a list of all students in the database
     */
    public List<Student> getAllStudents() {
        // Query the database for all students.
        SQLiteDatabase db = mHelper.getReadableDatabase();
        StudentCursor studentCursor = new StudentCursor(db.query(
                StudentSQLiteOpenHelper.TABLE_STUDENTS, null, // All columns.
                null, // No where (selection) clause.
                null, // No selection args.
                null, // No grouping constraint.
                null, // No 'having' constraint.
                StudentSQLiteOpenHelper.COLUMN_NAME + " asc")); // Order by ascending student names.

        // Compile a list of all the students in the database.
        List<Student> students = new ArrayList<Student>(studentCursor.getCount());
        while (studentCursor.moveToNext()) {
            students.add(studentCursor.getStudent());
        }

        // Close the cursor and the connection to the database.
        studentCursor.close();
        db.close();

        return students;
    }

    /**
     * Retrieves the student with the given ID from the database.
     *
     * @param id the ID of the student to get
     * @return the student with the given ID, or {@code null} if there is no student with the given
     *         ID in the database
     */
    public Student getStudent(long id) {
        // Query the database for the desired student.
        SQLiteDatabase db = mHelper.getReadableDatabase();
        StudentCursor studentCursor = new StudentCursor(db.query(
                StudentSQLiteOpenHelper.TABLE_STUDENTS, null, // All columns.
                StudentSQLiteOpenHelper.COLUMN_ID + " = ?", // Look for a run ID.
                new String[] { String.valueOf(id) }, // with this value.
                null, // No grouping constraint.
                null, // No 'having' constraint.
                null, // No ordering constraint.
                "1")); // Limit the result to 1 row (there should only be 1).

        // Get the desired student, if there is any.
        Student student = null;
        if (studentCursor.moveToFirst()) {
            student = studentCursor.getStudent();
        }

        // Close the cursor and the connection to the database.
        studentCursor.close();
        db.close();

        return student;
    }

    /**
     * Updates the values associated with the given student in the database.
     *
     * @param student the student to update
     * @return {@code true} if the student was successfully updated in the database; otherwise
     *         {@code false}
     */
    public boolean updateStudent(Student student) {
        return mHelper.updateStudent(student) != 0;
    }

    /**
     * Deletes the student with the given ID from the database.
     *
     * @param id the ID of the student to delete
     * @return {@code true} if the student was successfully deleted from the database; otherwise
     *         {@code false}
     */
    public boolean deleteStudent(long id) {
        return mHelper.deleteStudent(id) != 0;
    }
}
