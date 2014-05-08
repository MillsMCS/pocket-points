package edu.mills.cs180a.pocketpoints;

import java.lang.ref.WeakReference;

import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * A {@link ListFragment} that supports loading images displayed in each row of the {@link ListView}
 * in a background thread rather than on the UI thread. Child classes can load images in the
 * background thread by calling the {@link #loadBitmap(String, ImageView)} method.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
// The code for loading the images in the ListView in a background thread was adapted from
// http://developer.android.com/training/displaying-bitmaps/index.html
public class BitmapListFragment extends ListFragment {
    protected Bitmap mDefaultProfileImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDefaultProfileImg = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_contact_picture);
    }

    /**
     * Loads the image with the given path on a background thread, displaying the resulting
     * {@link Bitmap} in the given {@link ImageView} (if that {@link ImageView} has not already been
     * garbage collected).
     *
     * @param imagePath the path of the image to load
     * @param imageView the image view in which to desplay the loaded image
     */
    public void loadBitmap(String imagePath, ImageView imageView) {
        if (cancelPotentialWork(imagePath, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imagePath);
        }
    }

    private static boolean cancelPotentialWork(String path, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            String imagePath = bitmapWorkerTask.imagePath;
            // If bitmapData is not yet set or it differs from the new data
            if (imagePath == null || !imagePath.equals(path)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }

        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    // Drawable with a reference to the BitmapWorkerTask loading the desired image into the
    // ImageView. The default image for this Drawable is mDefaultProfileImage.
    private class AsyncDrawable extends BitmapDrawable {
        private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        private AsyncDrawable(BitmapWorkerTask workerTask) {
            super(getResources(), mDefaultProfileImg);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(workerTask);
        }

        private BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    // Async task that loads a bitmap in a background thread.
    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String imagePath = null;

        private BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected.
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imagePath = params[0];
            return ImageUtils.loadImage(imagePath, mDefaultProfileImg.getWidth(),
                    mDefaultProfileImg.getHeight());
        }

        // Once complete, see if ImageView is still around and if so, set the bitmap to the loaded
        // result.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                ImageView imageView = imageViewReference.get();
                BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
