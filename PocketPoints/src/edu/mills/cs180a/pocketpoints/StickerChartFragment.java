package edu.mills.cs180a.pocketpoints;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 *
 * @author manish.s
 *
 */

public class StickerChartFragment extends Fragment {
    GridView gridView;
    ArrayList<Bitmap> gridArray = new ArrayList<Bitmap>();
    CustomGridViewAdapter customGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set grid view item
        Bitmap smileSticker = BitmapFactory.decodeResource(this.getResources(), R.drawable.satisfied);

        gridArray.add(smileSticker);
        gridArray.add(smileSticker);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sticker_chart, container, false);

        super.onCreate(savedInstanceState);
        int iconSize=getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);

        GridView gridview = (GridView) view.findViewById(R.id.gridView1);


        return view;
    }
}
