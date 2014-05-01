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
 * @author Renee
 *
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
        Bitmap smileSticker = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.satisfied);

        gridArray.add(smileSticker);
        gridArray.add(smileSticker);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sticker_chart, container, false);

        // Set up the adapter.
        Activity activity = getActivity();
        GridViewCustomAdapter<Bitmap> adapter = new GridViewCustomAdapter<Bitmap>(activity,
                R.layout.row_grid, gridArray);
        GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        // gridView.setAdapter(adapter);

        return view;
    }

    private class GridViewCustomAdapter<Bitmap> extends ArrayAdapter {
        Context context;
        int layoutResourceId;
        ArrayList<Bitmap> data = new ArrayList<Bitmap>();

        public GridViewCustomAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                Log.d(TAG, "row was null");
                convertView = mInflater.inflate(R.layout.row_grid, null);
            }

            ImageView sticker = (ImageView) convertView.findViewById(R.id.grid_row_image);
            sticker.setImageResource(R.drawable.satisfied);

            return convertView;
        }

    }
}
