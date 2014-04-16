package edu.mills.cs180a.pocketpoints;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates and upgrades a database for storing student data.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentSQLiteOpenHelper extends SQLiteOpenHelper {
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
     * The title of the column containing the name of the image associated with a student (if any).
     */
    public static final String COLUMN_IMAGE_NAME = "image_name";

    /**
     * The title of the column containing the number of stickers associated with a student.
     */
    public static final String COLUMN_NUM_STICKERS = "num_stickers";

    /**
     * Creates a {@code StudentSQLiteOpenHelper} for the 'students' database with the given context.
     *
     * @param context the context in which to create this {@code StudentSQLiteOpenHelper}
     */
    public StudentSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STUDENTS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_NAME + " varchar(50) not null "
                + "check (" + COLUMN_NAME + " != ''), "
                + COLUMN_IMAGE_NAME + " varchar(100), "
                + COLUMN_NUM_STICKERS + " integer not null default 0 "
                + "check (" + COLUMN_NUM_STICKERS + ">-1)"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add code when upgrading the database.
    }
}
