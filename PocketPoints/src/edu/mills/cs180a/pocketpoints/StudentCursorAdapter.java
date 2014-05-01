package edu.mills.cs180a.pocketpoints;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import edu.mills.cs180a.pocketpoints.StudentSQLiteOpenHelper.StudentCursor;

/**
 * A {@link CursorAdapter} that interacts with a {@link StudentCursor} used specifically for
 * displaying a list of {@link Student}s. Note that the {@link Cursor} for this adapter should
 * ALWAYS be an instance of {@link StudentCursor}. Otherwise errors may result.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public abstract class StudentCursorAdapter extends CursorAdapter {

    /**
     * Create a {@code StudentCursorAdapter} for the given context whose cursor has access to all
     * the students in the database.
     *
     * @param context the context in which to create this {@code StudentCursorAdapter}
     */
    public StudentCursorAdapter(Context context) {
        super(context, StudentManager.get(context).getAllStudentsCursor(), 0 /* No Flags */);
    }

    @Override
    public Student getItem(int position) {
        StudentCursor studentCursor = (StudentCursor) super.getItem(position);
        return studentCursor.getStudent();
    }

    @Override
    public StudentCursor getCursor() {
        return (StudentCursor) super.getCursor();
    }
}
