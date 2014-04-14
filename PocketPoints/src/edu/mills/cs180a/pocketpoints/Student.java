package edu.mills.cs180a.pocketpoints;

/**
 * Model class representing a single student.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class Student {
    private final long mId;
    private String mName;
    private String mImgName = null;
    private int mNumStickers = 0;

    /**
     * Create a student with the given required fields.
     *
     * @param id the unique ID of the student
     * @param name the non-empty name of the student
     */
    public Student(long id, String name) {
        mId = id;
        setName(name);
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
