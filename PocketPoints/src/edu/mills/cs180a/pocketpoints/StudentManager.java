package edu.mills.cs180a.pocketpoints;

import java.util.List;

import android.content.Context;

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
// Modeled after the RunManager class from the RunTracker app in "Android Programming: The Big Nerd
// Ranch Guide".
public class StudentManager {
    private static StudentManager sStudentManager;

    private Context mAppContext;
    private StudentSQLiteOpenHelper mHelper;

    /**
     * Get the singleton instance of {@code StudentManager}. If no instance currently exists, create
     * an instance using the singleton application context provided by the given context.
     *
     * @param context the context to use
     * @return the current instance of {@StudentManager}
     */
    public static synchronized StudentManager get(Context context) {
        // TODO: Implement.
        return null;
    }

    private StudentManager(Context appContext) {
        mAppContext = appContext;
        mHelper = new StudentSQLiteOpenHelper(appContext);
    }

    /**
     * Retrieves all of the students from the database.
     *
     * @return a list of all students in the database
     */
    public List<Student> getAllStudents() {
        // TODO: Implement.
        return null;
    }

    /**
     * Retrieves the student with the given ID from the database.
     *
     * @param id the ID of the student to get
     * @return the student with the given ID, or {@code null} if there is no student with the given
     *         ID in the database
     */
    public Student getStudent(long id) {
        // TODO: Implement.
        return null;
    }

    /**
     * Adds the given student to the database.
     *
     * @param student the student to add
     */
    public void createStudent(Student student) {
        // TODO: Implement.
    }

    /**
     * Updates the values associated with the given student in the database.
     *
     * @param student the student to update
     */
    public void updateStudent(Student student) {
        // TODO: Implement.
    }

    /**
     * Deletes the student with the given ID from the database.
     *
     * @param id the ID of the student to delete
     */
    public void deleteStudent(long id) {
        // TODO: Implement.
    }
}
