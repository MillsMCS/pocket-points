package edu.mills.cs180a.pocketpoints.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

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

    @Test
    public void ctor_nullName_throws() {
        try {
            Student student = new Student(STUDENT_ID, null);
            fail("Expected AssertionError thrown; instead got " + student);
        } catch (AssertionError expected) {
            // Expected.
        }
    }

    @Test
    public void ctor_emptyName_throws() {
        try {
            Student student = new Student(STUDENT_ID, null);
            fail("Expected AssertionError thrown; instead got " + student);
        } catch (AssertionError expected) {
            // Expected.
        }
    }

    @Test
    public void ctor_succeeds() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        assertEquals(STUDENT_ID, student.getId());
        assertEquals(STUDENT_NAME, student.getName());
        assertEquals(null, student.getImgName()); // Initialized to null, since none specified.
        assertEquals(0, student.getNumStickers()); // Initialized to 0, since none specified.
    }

    @Test
    public void setName_null_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        try {
            student.setName(null);
            fail("Expected AssertionError thrown");
        } catch (AssertionError expected) {
            // Expected.
        }
    }

    @Test
    public void setName_emptyString_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        try {
            student.setName("");
            fail("Expected AssertionError thrown; instead, set student name to '"
                    + student.getName() + "'");
        } catch (AssertionError expected) {
            // Expected.
        }
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

    @Test
    public void setNumStickers_negativeValue_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME);
        try {
            student.setNumStickers(-1);
            fail("Expected AssertionError thrown; instead, set num stickers to '"
                    + student.getNumStickers() + "'");
        } catch (AssertionError expected) {
            // Expected.
        }
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

    @Test
    public void removeLastSticker_noStickersToRemove_throws() {
        Student student = new Student(STUDENT_ID, STUDENT_NAME).setNumStickers(0);
        try {
            student.removeLastSticker();
            fail("Expected AssertionError thrown; instead, num stickers is now '"
                    + student.getNumStickers() + "'");
        } catch (AssertionError expected) {
            // Expected.
        }
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
