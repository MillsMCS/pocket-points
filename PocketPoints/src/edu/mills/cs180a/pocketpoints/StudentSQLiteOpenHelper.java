package edu.mills.cs180a.pocketpoints;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Creates and upgrades a database for storing student data.
 * 
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "StudentSQLiteOpenHelper";
    private static final String DATABASE_NAME = "class_info.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * The name of the table storing student information.
     */
    public static final String TABLE_STUDENTS = "students";

    /**
     * The title of the column containing the unique ID of a student.
     */
    public static final String COLUMN_ID = "_id";

    /**
     * The title of the column containing the name of a student.
     */
    public static final String COLUMN_NAME = "name";

    /**
     * The title of the column containing the name of the image associated with
     * a student (if any).
     */
    public static final String COLUMN_IMAGE_NAME = "image_name";

    /**
     * The title of the column containing the number of stickers associated with
     * a student.
     */
    public static final String COLUMN_NUM_STICKERS = "num_stickers";

    /**
     * Creates a {@code StudentSQLiteOpenHelper} for the 'students' database
     * with the given context.
     * 
     * @param context
     *            the context in which to create this
     *            {@code StudentSQLiteOpenHelper}
     */
    public StudentSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STUDENTS + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_NAME
                + " varchar(255) not null " + "check (" + COLUMN_NAME
                + " != ''), " + COLUMN_IMAGE_NAME + " varchar(255), "
                + COLUMN_NUM_STICKERS + " integer not null default 0 "
                + "check (" + COLUMN_NUM_STICKERS + ">-1)" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add code when upgrading the database.
    }

    /**
     * Inserts the given student into the {@code students} table of the
     * database. If the student was successfully added, changes the ID of the
     * student to reflect the student's unique ID in the database. If the
     * student was not added, sets the ID of the student to an invalid value.
     * 
     * @param student
     *            the student to insert
     * @return the row at which the student was inserted, or {@code -1} if an
     *         error occurred
     */
    public long insertStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_IMAGE_NAME, student.getImgName());
        values.put(COLUMN_NUM_STICKERS, student.getNumStickers());
        long studentId = getWritableDatabase().insert(TABLE_STUDENTS, null,
                values);
        student.setID(studentId);
        return studentId;
    }

    /**
     * Gets a cursor of all the students in the database.
     * 
     * @return the cursor for all students in the database
     */
    public StudentCursor queryStudents() {
        // Equivalent to "SELECT * FROM students ORDER BY name ASC"
        Cursor wrappedCursor = getReadableDatabase().query(TABLE_STUDENTS,
                null, // All columns.
                null, // No where (selection) clause.
                null, // No selection args.
                null, // No grouping constraint.
                null, // No 'having' constraint.
                COLUMN_NAME + " asc"); // Order by ascending student names.
        return new StudentCursor(wrappedCursor);
    }

    /**
     * Gets a cursor for the student with the given ID.
     * 
     * @param id
     *            the ID of the student to query
     * @return the cursor for the student with the given ID
     */
    public StudentCursor queryStudent(long id) {
        // Equivalent to "SELECT * FROM students WHERE _id = {id} LIMIT 1"
        Cursor wrappedCursor = getReadableDatabase().query(TABLE_STUDENTS,
                null, // All columns.
                COLUMN_ID + " = ?", // Look for a run ID.
                new String[] { String.valueOf(id) }, // with this value.
                null, // No grouping constraint.
                null, // No 'having' constraint.
                null, // No ordering constraint.
                "1"); // Limit the result to 1 row (there should only be 1).
        return new StudentCursor(wrappedCursor);
    }

    /**
     * Updates the given student in the database.
     * 
     * @param student
     *            the student to update
     * @return the number of students in the database that were updated
     */
    public int updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_IMAGE_NAME, student.getImgName());
        values.put(COLUMN_NUM_STICKERS, student.getNumStickers());
        int numStudentsUpdated = getWritableDatabase().update(TABLE_STUDENTS,
                values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(student.getId()) });
        if (numStudentsUpdated > 1) {
            Log.e(TAG,
                    "When attempted to update student with ID = "
                            + student.getId() + ", " + numStudentsUpdated
                            + " students were updated!");
        }
        return numStudentsUpdated;
    }

    /**
     * Deletes the student with the given ID from the database.
     * 
     * @param id
     *            the ID of the student to delete
     * @return the number of students in the database that were deleted
     */
    public int deleteStudent(long id) {
        int numStudentsDeleted = getWritableDatabase().delete(TABLE_STUDENTS,
                COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        if (numStudentsDeleted > 1) {
            Log.e(TAG, "When attempted to delete student with ID = " + id
                    + ", " + numStudentsDeleted + " students were deleted!");
        }
        return numStudentsDeleted;
    }

    /**
     * A convenience class that wraps a cursor that returns rows from the
     * {@code students} table.
     * 
     * @author ajkwak@users.noreply.github.com (AJ Parmidge)
     */
    public static class StudentCursor extends CursorWrapper {

        /**
         * Creates a {@code StudentCursor} that wraps the given cursor.
         * 
         * @param cursor
         *            the cursor to wrap
         */
        public StudentCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         * Creates an instance of {@link Student} that represents the current
         * row in the {@code students} table.
         * 
         * @return the {@link Student} representation of this row in the
         *         {@code students} table
         */
        public Student getStudent() {
            if (isBeforeFirst() || isAfterLast()) {
                return null; // There is no current row.
            }
            Student student = new Student();
            student.setID(getLong(getColumnIndex(COLUMN_ID)));
            student.setName(getString(getColumnIndex(COLUMN_NAME)));
            student.setImgName(getString(getColumnIndex(COLUMN_IMAGE_NAME)));
            student.setNumStickers(getInt(getColumnIndex(COLUMN_NUM_STICKERS)));
            return student;
        }

    }
}
