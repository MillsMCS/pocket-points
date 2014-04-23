package edu.mills.cs180a.pocketpoints.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.mills.cs180a.pocketpoints.Student;
import edu.mills.cs180a.pocketpoints.StudentSQLiteOpenHelper;
import edu.mills.cs180a.pocketpoints.StudentSQLiteOpenHelper.StudentCursor;

/**
 * JUnit tests for {@link StudentSQLiteOpenHelper}.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentSQLiteOpenHelperTest extends AndroidTestCase {
    // Fields for Student 1 (Name is Lexicographically before student2).
    private static final String STUDENT_1_NAME = "Phillipa Gordon";
    private static final String STUDENT_1_IMG_NAME = "PhilG_student_profile.jpg";
    private static final int STUDENT_1_NUM_STICKERS = 4;

    // Fields for Student 2 (Name is Lexicographically after student1).
    private static final String STUDENT_2_NAME = "Stella Maynard";
    private static final String STUDENT_2_IMG_NAME = "StelMay_student_profile.jpg";
    private static final int STUDENT_2_NUM_STICKERS = 0;

    private StudentSQLiteOpenHelper studentDbHelper;
    private SQLiteDatabase db;
    private Student student1;
    private Student student2;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create an isolated context that does not affect the production database.
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        // Initialize the variables that will be used during this test.
        studentDbHelper = new StudentSQLiteOpenHelper(context);
        db = studentDbHelper.getWritableDatabase();
        student1 = new Student()
                .setName(STUDENT_1_NAME)
                .setImgName(STUDENT_1_IMG_NAME)
                .setNumStickers(STUDENT_1_NUM_STICKERS);
        student2 = new Student()
                .setName(STUDENT_2_NAME)
                .setImgName(STUDENT_2_IMG_NAME)
                .setNumStickers(STUDENT_2_NUM_STICKERS);
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_expectedColumnsPresent() {
        Cursor cursor = queryEntireStudentTable();

        // Right now, there are only 4 columns in the students table definition.
        assertEquals(4, cursor.getColumnCount());

        // Verify that the columns have the expected names.
        cursor.getColumnIndexOrThrow(StudentSQLiteOpenHelper.COLUMN_ID);
        cursor.getColumnIndexOrThrow(StudentSQLiteOpenHelper.COLUMN_NAME);
        cursor.getColumnIndexOrThrow(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME);
        cursor.getColumnIndexOrThrow(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS);

        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_allFieldsProvided_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntries_idProvidedAutomatically() {
        verifyDatabaseInitiallyEmpty();

        // Insert a student into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Insert another student into the database.
        values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_2_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_2_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_2_NUM_STICKERS);
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected two entries.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(2, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        int student1Id = cursor.getInt(cursor.getColumnIndex(StudentSQLiteOpenHelper.COLUMN_ID));
        cursor.moveToNext();
        verifyRowContainsExpectedInfo(cursor, STUDENT_2_NAME, STUDENT_2_IMG_NAME,
                STUDENT_2_NUM_STICKERS);
        int student2Id = cursor.getInt(cursor.getColumnIndex(StudentSQLiteOpenHelper.COLUMN_ID));
        assertTrue(student1Id != student2Id); // The IDs of the two rows should be distinct.
        assertTrue(cursor.isLast());

        // Verify that the IDs of the two entries were provided automatically and are distinct.
        assertTrue(student1Id != student2Id); // The IDs of the two rows should be distinct.

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_nameNotProvided_fails() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        try {
            long row = db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);
            fail("Inserted student without name successfully into the database at row " + row);
        } catch (SQLException expected) {
            // Expected.
        }
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_nameNull_fails() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, (String) null);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        try {
            long row = db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);
            fail("Inserted student with null name successfully into the database at row " + row);
        } catch (SQLException expected) {
            // Expected.
        }
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_nameEmptyString_fails() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, "");
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        try {
            long row = db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);
            fail("Inserted student with empty name successfully into the database at row " + row);
        } catch (SQLException expected) {
            // Expected.
        }
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_imageNameNotProvided_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_1_NAME, null /* student image name */,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_imageNameNull_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, (String) null);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_1_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_1_NAME, null /* Student image name */,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_numStickersNotProvided_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                0 /* Number of stickers: Default should be zero */);

        // Close the cursor.
        assertTrue(cursor.isLast());
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_numStickersNull_fails() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, (Integer) null);

        // Insert the values into the database.
        try{
            long row = db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);
            fail("Inserted student with 'NULL' number of stickers successfully into the database"
                    + " at row " + row);
        } catch (SQLException expected) {
            // Expected.
        }
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_numStickersNegative_fails() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_1_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_1_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, -1);

        // Insert the values into the database.
        try {
            long row = db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);
            fail("Inserted student with 'NULL' number of stickers successfully into the database"
                    + " at row " + row);
        } catch (SQLException expected) {
            // Expected.
        }
    }

    public void testInsertStudent() {
        verifyDatabaseInitiallyEmpty();

        // Insert the first student.
        long student1Id = studentDbHelper.insertStudent(student1);
        assertEquals(student1Id, student1.getId()); // Verify student1 ID has been updated.

        // Verify that now database now contains only student1.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, student1Id, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());
        cursor.close();

        // Insert the second student.
        long student2Id = studentDbHelper.insertStudent(student2);
        assertEquals(student2Id, student2.getId()); // Verify student2 ID has been updated.

        // Verify that now database now contains both student1 and student2.
        cursor = queryEntireStudentTable();
        assertEquals(2, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, student1Id, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        cursor.moveToNext();
        verifyRowContainsExpectedInfo(cursor, student2Id, STUDENT_2_NAME, STUDENT_2_IMG_NAME,
                STUDENT_2_NUM_STICKERS);
        assertTrue(cursor.isLast());
        cursor.close();
    }

    public void testQueryStudents_databaseEmpty() {
        verifyDatabaseInitiallyEmpty();

        // Query for students in the empty database.
        StudentCursor studentCursor = studentDbHelper.queryStudents();

        // Verify get the expected results.
        assertEquals(0, studentCursor.getCount());
        assertFalse(studentCursor.moveToFirst());
        studentCursor.close();
    }

    public void testQueryStudents_databaseHasSingleEntry() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);

        // Query for students in the database.
        StudentCursor studentCursor = studentDbHelper.queryStudents();

        // Verify get the expected results.
        assertEquals(1, studentCursor.getCount());

        // Verify that the first student returned is as expected.
        assertTrue(studentCursor.moveToFirst());
        Student student = studentCursor.getStudent();
        assertEquals(student1.getId(), student.getId());
        assertEquals(student1.getName(), student.getName());
        assertEquals(student1.getImgName(), student.getImgName());
        assertEquals(student1.getNumStickers(), student.getNumStickers());

        // Close the cursor.
        assertTrue(studentCursor.isLast());
        studentCursor.close();
    }

    public void testQueryStudents_datbaseHasMultipleEntries() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);
        studentDbHelper.insertStudent(student2);

        // Query for students in the database.
        StudentCursor studentCursor = studentDbHelper.queryStudents();

        // Verify get the expected results.
        assertEquals(2, studentCursor.getCount());

        // Verify that the first student returned is as expected.
        assertTrue(studentCursor.moveToFirst());
        Student student = studentCursor.getStudent();
        assertEquals(student1.getId(), student.getId());
        assertEquals(student1.getName(), student.getName());
        assertEquals(student1.getImgName(), student.getImgName());
        assertEquals(student1.getNumStickers(), student.getNumStickers());

        // Verify that the second student returned is as expected.
        assertTrue(studentCursor.moveToNext());
        student = studentCursor.getStudent();
        assertEquals(student2.getId(), student.getId());
        assertEquals(student2.getName(), student.getName());
        assertEquals(student2.getImgName(), student.getImgName());
        assertEquals(student2.getNumStickers(), student.getNumStickers());

        // Close the cursor.
        assertTrue(studentCursor.isLast());
        studentCursor.close();
    }

    public void testQueryStudent_studentExistsInDatabase() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);

        StudentCursor studentCursor = studentDbHelper.queryStudent(student1.getId());

        // Verify get the expected results.
        assertEquals(1, studentCursor.getCount());

        // Verify that the first student returned is as expected.
        assertTrue(studentCursor.moveToFirst());
        Student student = studentCursor.getStudent();
        assertEquals(student1.getId(), student.getId());
        assertEquals(student1.getName(), student.getName());
        assertEquals(student1.getImgName(), student.getImgName());
        assertEquals(student1.getNumStickers(), student.getNumStickers());

        // Close the cursor.
        assertTrue(studentCursor.isLast());
        studentCursor.close();
    }

    public void testQueryStudent_studentDoesNotExistInDatabase() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);

        StudentCursor studentCursor = studentDbHelper.queryStudent(
                student1.getId() + 1 /* Non-existent ID*/);

        // Verify get the expected results.
        assertEquals(0, studentCursor.getCount());
        assertFalse(studentCursor.moveToFirst());
        studentCursor.close();
    }

    public void testUpdateStudent_fails() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);

        // Try to update a student that doesn't exist.
        int numStudentsUpdated = studentDbHelper.updateStudent(student2);
        assertEquals(0, numStudentsUpdated);

        // Verify that the database hasn't been changed.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        verifyRowContainsExpectedInfo(cursor, student1.getId(), STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());
        cursor.close();
    }

    public void testUpdateStudent_succeeds() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);
        studentDbHelper.insertStudent(student2);

        // Update one of the students.
        student2.setName("Royal Gardner");
        student2.setImgName("rgardner_student_profile.jpg");
        student2.setNumStickers(42);
        int numStudentsUpdated = studentDbHelper.updateStudent(student2);
        assertEquals(1, numStudentsUpdated);

        // Verify that the contents of the database are as expected.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(2, cursor.getCount());

        // Verify that the student1 entry hasn't been changed.
        assertTrue(cursor.moveToFirst());
        verifyRowContainsExpectedInfo(cursor, student1.getId(), STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);

        // Verify that the student2 entry has been updated.
        assertTrue(cursor.moveToNext());
        verifyRowContainsExpectedInfo(cursor, student2.getId(), student2.getName(),
                student2.getImgName(), student2.getNumStickers());

        // Close the cursor.
        assertTrue(cursor.isLast());
        cursor.close();
    }

    public void testDeleteStudent_fails() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);

        // Try to delete a student that doesn't exist.
        int numStudentsDeleted = studentDbHelper.deleteStudent(
                student1.getId() + 1 /* Non-existent ID*/);
        assertEquals(0, numStudentsDeleted);

        // Verify that the database hasn't changed.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        verifyRowContainsExpectedInfo(cursor, student1.getId(), STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());
        cursor.close();
    }

    public void testDeleteStudent_succeeds() {
        verifyDatabaseInitiallyEmpty();
        studentDbHelper.insertStudent(student1);
        studentDbHelper.insertStudent(student2);

        // Delete student2.
        int numStudentsDeleted = studentDbHelper.deleteStudent(student2.getId());
        assertEquals(1, numStudentsDeleted);

        // Verify that student1 is unchanged in the database, but student2 has been deleted.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        verifyRowContainsExpectedInfo(cursor, student1.getId(), STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
        assertTrue(cursor.isLast());
        cursor.close();
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        studentDbHelper.close();
        super.tearDown();
    }

    private Cursor queryEntireStudentTable() {
        return db.query(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, // Get all columns.
                null, // Get all rows
                null, // No selection args
                null, // No 'group by' argument
                null, // No 'having' argument
                StudentSQLiteOpenHelper.COLUMN_NAME + " asc"); // Order by ascending student name.
    }

    private void verifyDatabaseInitiallyEmpty() {
        Cursor cursor = db.query(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, // Get all columns.
                null, // Get all rows
                null, // No selection args
                null, // No 'group by' argument
                null, // No 'having' argument
                null); // Don't care about order
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    private void verifyRowContainsExpectedInfo(Cursor cursor, String expectedName,
            String expectedImgName, int expectedNumStickers) {
        assertEquals(expectedName, cursor.getString(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_NAME)));
        assertEquals(expectedImgName, cursor.getString(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME)));
        assertEquals(expectedNumStickers, cursor.getInt(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS)));
    }

    private void verifyRowContainsExpectedInfo(Cursor cursor, long expectedId, String expectedName,
            String expectedImgName, int expectedNumStickers) {
        assertEquals(expectedId, cursor.getLong(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_ID)));
        assertEquals(expectedName, cursor.getString(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_NAME)));
        assertEquals(expectedImgName, cursor.getString(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME)));
        assertEquals(expectedNumStickers, cursor.getInt(cursor
                .getColumnIndex(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS)));
    }
}
