package edu.mills.cs180a.pocketpoints;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Uninstantiable class with static methods for decoding a scaled image.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
// Methods in this class were adapted from:
// http://developer.android.com/training/camera/photobasics.html
public class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException(); // This class is uninstantiable.
    }

    /**
     * Load the image from the given file path such that the returned image's dimensions are as
     * similar as possible to the given dimensions.
     *
     * @param imagePath the file path from which to load the image
     * @param targetWidth the maximum width that the returned image should have
     * @param targetHeight the minimum width that the returned image should have
     * @return the scaled image
     */
    public static Bitmap loadImage(String imagePath, int targetWidth, int targetHeight) {
        // Get the dimensions of the bitmap.
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int unscaledImageWidth = bmOptions.outWidth;
        int unscaledImageHeight = bmOptions.outHeight;

        // Determine how much to scale down the image.
        int scaleFactor = Math.max(unscaledImageWidth / targetWidth,
                unscaledImageHeight / targetHeight);

        // Decode the image file into a Bitmap sized to fill the View.
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }
}
