package edu.mills.cs180a.pocketpoints.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.mills.cs180a.pocketpoints.StudentSQLiteOpenHelper;

/**
 * JUnit tests for {@link StudentSQLiteOpenHelper}.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentSQLiteOpenHelperTest extends AndroidTestCase {
    private static final String STUDENT_NAME = "Phillipa Gordon";
    private static final String STUDENT_IMG_NAME = "PhilG_student_profile.jpg";
    private static final int STUDENT_NUM_STICKERS = 4;

    private StudentSQLiteOpenHelper studentDbHelper;
    private SQLiteDatabase db;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create an isolated context that does not affect the production database.
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        // Initialize the variables that will be used during this test.
        studentDbHelper = new StudentSQLiteOpenHelper(context);
        db = studentDbHelper.getWritableDatabase();
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
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_NAME, STUDENT_IMG_NAME, STUDENT_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntries_idProvidedAutomatically() {
        verifyDatabaseInitiallyEmpty();

        // Insert a student into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Insert another student into the database.
        String student2Name = "Stella Maynard";
        String student2ImgName = "StelMay_student_profile.jpg";
        int student2NumStickers = 0;
        values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, student2Name);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, student2ImgName);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, student2NumStickers);
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected two entries.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(2, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_NAME, STUDENT_IMG_NAME, STUDENT_NUM_STICKERS);
        int student1Id = cursor.getInt(cursor.getColumnIndex(StudentSQLiteOpenHelper.COLUMN_ID));
        cursor.moveToNext();
        verifyRowContainsExpectedInfo(cursor, student2Name, student2ImgName, student2NumStickers);
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
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

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
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

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
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

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
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_NAME, null /* student image name */,
                STUDENT_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_imageNameNull_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, (String) null);
        values.put(StudentSQLiteOpenHelper.COLUMN_NUM_STICKERS, STUDENT_NUM_STICKERS);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_NAME, null /* Student image name */,
                STUDENT_NUM_STICKERS);
        assertTrue(cursor.isLast());

        // Close the cursor.
        cursor.close();
    }

    // Tests the schema of the 'students' table created in the onCreate() method.
    public void testSchema_insertEntry_numStickersNotProvided_succeeds() {
        verifyDatabaseInitiallyEmpty();

        // Set up the values to insert into the database.
        ContentValues values = new ContentValues();
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);

        // Insert the values into the database.
        db.insertOrThrow(StudentSQLiteOpenHelper.TABLE_STUDENTS, null, values);

        // Verify that the database contains the expected information.
        Cursor cursor = queryEntireStudentTable();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        verifyRowContainsExpectedInfo(cursor, STUDENT_NAME, STUDENT_IMG_NAME,
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
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
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
        values.put(StudentSQLiteOpenHelper.COLUMN_NAME, STUDENT_NAME);
        values.put(StudentSQLiteOpenHelper.COLUMN_IMAGE_NAME, STUDENT_IMG_NAME);
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
                null); // Don't care about order
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
}
