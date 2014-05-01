package edu.mills.cs180a.pocketpoints;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 *
 * An {@code Fragment} called by {@link EditStickerActivity} that displays a graphical
 * {@code GridView} representation of the number of stickers currently associated with a the
 * selected student.
 *
 * <P>
 * After clicking on the "empty sticker", the user is given the choice (via an alert dialog) of
 * adding or canceling the sticker. If added, the student's sticker count will increment by one, the
 * new sticker count is added to the database, and the result code {@link Activity#RESULT_OK} is
 * provided to the parent activity. Otherwise, the database is not modified, and the result code
 * {@link Activity#RESULT_CANCELED} is provided.
 *
 * <P>
 * Clicking the "Undo" button will, after confirming via an alert dialog, erase the most recently
 * added sticker.
 *
 * <P>
 * Clicking the "Clear ALl" button will, after confirming via an alert dialog, clear all stickers
 * associated with the student and will update the database accordingly.
 *
 *
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 */
public class StickerChartFragment extends Fragment {
    private static final String TAG = "StickerChartFragment";

    private GridView gridView;
    private ArrayList<Bitmap> gridArray = new ArrayList<Bitmap>();
    private GridViewCustomAdapter customGridAdapter;
    private LayoutInflater mInflater;
    private List<Student> mStudentList;
    private StudentManager mStudentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set grid view item
        Bitmap smileSticker = BitmapFactory.decodeResource(this.getResources(),R.drawable.satisfied);

        gridArray.add(smileSticker);
        gridArray.add(smileSticker);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_sticker_chart, container, false);

        // Set up the adapter.
        Activity activity = getActivity();
        GridViewCustomAdapter<Bitmap> adapter = new GridViewCustomAdapter<Bitmap>(activity);
        GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        gridView.setAdapter(adapter);

        return view;
    }

    private class GridViewCustomAdapter<Bitmap> extends ArrayAdapter {
        public GridViewCustomAdapter(Context context) {
            super(context, R.id.rowSmileImage, gridArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                Log.d(TAG, "row was null");
                convertView = mInflater.inflate(R.layout.fragment_sticker_chart_row, null);
            }

            ImageView sticker = (ImageView) convertView.findViewById(R.id.rowSmileImage);
            sticker.setImageResource(R.drawable.satisfied);
            return convertView;
        }

    }
}
