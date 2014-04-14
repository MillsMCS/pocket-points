package edu.mills.cs180a.pocketpoints.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import edu.mills.cs180a.pocketpoints.Student;

/**
 * JUnit test for {@link Student}.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentTest {
    private static final long STUDENT_ID = 1;
    private static final String STUDENT_NAME = "Josie Pye";

    @Test(expected = AssertionError.class)
    public void ctor_nullName_throws() {
        new Student(STUDENT_ID, null);
    }

    @Test(expected = AssertionError.class)
    public void ctor_emptyName_throws() {
        new Student(STUDENT_ID, "");
    }

    @Test
    public void ctor_succeeds() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        assertEquals(STUDENT_ID, student.getId());
        assertEquals(STUDENT_NAME, student.getName());
        assertEquals(null, student.getId()); // Initialized to null, since none specified.
        assertEquals(0, student.getNumStickers()); // Initialized to 0, since none specified.
    }

    @Test(expected = AssertionError.class)
    public void setName_null_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        student.setName(null);
    }

    @Test(expected = AssertionError.class)
    public void setName_emptyString_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        student.setName("");
    }

    @Test
    public void setName_nonEmptyString_succeeds() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        String newName = "Diana Barry";
        assertFalse(newName.equals(student.getName())); // Want to set the name to a NEW value.
        student.setName(newName);
        assertEquals(newName, student.getName());
    }

    @Test
    public void setImgName() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);

        // CASE: Set image name to non-empty string.
        String imgName = "34_student_profile.jpg";
        student.setImgName(imgName);
        assertEquals(imgName, student.getImgName());

        // CASE: Set image name to null.
        student.setImgName(null);
        assertEquals(null, student.getImgName());

        // CASE: Set image name to empty string is equivalent to setting image name to null.
        student.setImgName("");
        assertEquals(null, student.getImgName());
    }

    @Test(expected = AssertionError.class)
    public void setNumStickers_negativeValue_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        student.setNumStickers(-1);
    }

    @Test
    public void setNumStickers_succeeds() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);

        // CASE: Set number of stickers to 0.
        int numStickers = 0;
        student.setNumStickers(numStickers);
        assertEquals(numStickers, student.getNumStickers());

        // CASE: Set number of stickers to positive number.
        numStickers = 56;
        student.setNumStickers(numStickers);
        assertEquals(numStickers, student.getNumStickers());
    }

    @Test
    public void addSticker() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        int initialNumStickers = student.getNumStickers();

        // Add stickers.
        student.addSticker();
        assertEquals(initialNumStickers + 1, student.getNumStickers());
        student.addSticker();
        assertEquals(initialNumStickers + 2, student.getNumStickers());
        student.addSticker();
        assertEquals(initialNumStickers + 3, student.getNumStickers());
        student.addSticker();
        assertEquals(initialNumStickers + 4, student.getNumStickers());
    }

    @Test(expected = AssertionError.class)
    public void removeLastSticker_noStickersToRemove_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        student.setNumStickers(0);
        student.removeLastSticker();
    }

    @Test
    public void removeLastSticker_succeeds() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        int initialNumStickers = 4;
        student.setNumStickers(initialNumStickers);

        // Add stickers.
        student.removeLastSticker();
        assertEquals(initialNumStickers - 1, student.getNumStickers());
        student.removeLastSticker();
        assertEquals(initialNumStickers - 2, student.getNumStickers());
        student.removeLastSticker();
        assertEquals(initialNumStickers - 3, student.getNumStickers());
        student.removeLastSticker();
        assertEquals(initialNumStickers - 4, student.getNumStickers());
    }
}
