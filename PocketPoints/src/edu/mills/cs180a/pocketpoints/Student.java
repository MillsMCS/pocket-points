package edu.mills.cs180a.pocketpoints;

/**
 * Model class representing a single student.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class Student {
    /**
     * A value representing an invalid student ID (a student will have an invalid ID, for example,
     * if the {@link Student} object has just been constructed and/or does not yet exist in the
     * database).
     */
    public static final long INVALID_ID = -1;

    private long mId = INVALID_ID;
    private String mName = null;
    private String mImgName = null;
    private int mNumStickers = 0;

    /**
     * Create an empty student object. The ID of the student will be set to {@link #INVALID_ID}.
     *
     * <p>
     * Note that upon construction, this {@code Student} object is not valid for insertion into the
     * database. The name of the student must be set for the student to insertable into the
     * database.
     */
    public Student() {
        // Dummy constructor.
    }

    /**
     * Gets the unique ID of this {@code Student}.
     *
     * @return the unique ID of the student
     */
    public long getId() {
        return mId;
    }

    /**
     * Sets the unique ID of the student to the given value.
     *
     * <p>
     * NOTE: this method should ONLY be called by the {@link StudentManager} class! This ID
     * represents the unique ID of the student in the underlying database, and should not be changed
     * by other classes.
     *
     * @param id the ID to set
     * @return {@code this}, for chaining
     */
    public Student setID(long id) {
        mId = id;
        return this;
    }

    /**
     * Gets the name of the student.
     *
     * @return the name of the student
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the name of the student to the given value.
     *
     * @param name the name to set (must be a non-empty string)
     * @return {@code this}, for chaining
     */
    public Student setName(String name) {
        assert name != null && !name.isEmpty() : "Name must be non-empty string.";
        mName = name;
        return this;
    }

    /**
     * Gets the name of the image of this student, if there is one. The image itself is stored in an
     * external file that is private to this app.
     *
     * @return the name of the image associated with the student (if it exists); otherwise
     *         {@code null}
     */
    public String getImgName() {
        return mImgName;
    }

    /**
     * Sets the name of the image associated with this student to the given value. Setting the image
     * name to {@code null} or to an empty string removes the image associated with this
     * {@code Student}.
     *
     * @param imgName the image name to set
     * @return {@code this}, for chaining
     */
    public Student setImgName(String imgName) {
        if (imgName == null || imgName.isEmpty()) {
            mImgName = null;
        } else {
            mImgName = imgName;
        }
        return this;
    }

    /**
     * Gets the number of stickers this student has earned. The number of stickers is always a
     * nonnegative value.
     *
     * @return the number of stickers earned by the student
     */
    public int getNumStickers() {
        return mNumStickers;
    }

    /**
     * Sets the number of stickers earned by the student to the given value.
     *
     * @param numStickers the number of stickers to set (must be nonnegative)
     * @return {@code this}, for chaining
     */
    public Student setNumStickers(int numStickers) {
        assert numStickers >= 0 : "Number of stickers cannot be negative.";
        mNumStickers = numStickers;
        return this;
    }

    /**
     * Adds a single sticker to the student's profile).
     *
     * @return {@code this}, for chaining
     */
    public Student addSticker() {
        mNumStickers++;
        return this;
    }

    /**
     * Removes the most recently added sticker from the student's profile.
     *
     * @return {@code this}, for chaining
     */
    public Student removeLastSticker() {
        assert mNumStickers > 0 : "Cannot remove a sticker!  There are no stickers left.";
        mNumStickers--;
        return this;
    }
}
