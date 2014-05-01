package edu.mills.cs180a.pocketpoints;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * ArrayAdapter for displaying a list of {@link Student}s.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentArrayAdapter extends ArrayAdapter<Student> {

    /**
     * Create a {@code StudentArrayAdapter} for the given context and resource.
     * 
     * @param context the context in which to create this {@code StudentArrayAdapter}
     * @param resource the resource ID for a layout file containing a {@link TextView} to use when
     *        instantiating views
     */
    protected StudentArrayAdapter(Context context, int resource) {
        super(context, resource, R.id.rowStudentName, StudentManager.get(context).getAllStudents());
    }
}
