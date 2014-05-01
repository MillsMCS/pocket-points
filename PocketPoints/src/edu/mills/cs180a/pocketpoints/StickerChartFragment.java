package edu.mills.cs180a.pocketpoints;

import android.app.Activity;

/**
 * 
 * An {@code Fragment} that displays a graphical representation of the number of stickers currently
 * associated with a selected student.
 * 
 * <P>
 * After clicking on the "empty sticker", the user is given the choice (via an alert dialog) of
 * adding or canceling the sticker. If added, the student's sticker count will increment by one, the
 * new sticker count is added to the database, and the result code {@link Activity.RESULT_OK} is
 * provided to the parent activity. Otherwise, the database is not modified, and the result code
 * {@link Activity.RESULT_CANCELED} is provided.
 * 
 * <P>
 * Clicking the "Undo" button will, after confirming via an alert dialog, erase the most recently
 * added sticker.
 *
 * <P>
 * Clicking the "Clear ALl" button will, after confirming via an alert dialog, clear all stickers
 * associated with the student and will update the database accordingly.
 * 
 * @author renee.johnston1149@gmail.com (Renee Johnston)
 */
public class StickerChartFragment {

}
