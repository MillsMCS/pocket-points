package edu.mills.cs180a.pocketpoints.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.mills.cs180a.pocketpoints.Student;

/**
 * JUnit test for {@link Student}.
 *
 * @author ajkwak@users.noreply.github.com (AJ Parmidge)
 */
public class StudentTest {
    private Student student;

    @Before
    public void setUp() {
        student = new Student();
    }

    @Test
    public void ctor() {
        Student student = new Student();
        assertEquals(null, student.getName()); // Should be initialized to null.
        assertEquals(null, student.getImgName()); // Should be initialized to null.
        assertEquals(0, student.getNumStickers()); // Should be initialized to 0.
    }

    @Test
    public void setId() {
        // CASE: Set ID to positive number.
        long id = 4;
        student.setID(id);
        assertEquals(id, student.getId());

        // CASE: Set ID to 0.
        id = 0;
        student.setID(id);
        assertEquals(id, student.getId());

        // CASE: Set ID to negative number.
        id = 0;
        student.setID(id);
        assertEquals(id, student.getId());
    }

    @Test
    public void setName_null_throws() {
        Student student = new Student();
        try {
            student.setName(null);
            fail("Expected AssertionError thrown");
        } catch (AssertionError expected) {
            // Expected.
        }
    }

    @Test
    public void setName_emptyString_throws() {
        Student student = new Student();
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
        String name = "Josie Pye";
        Student student = new Student();
        student.setName(name);
        assertEquals(name, student.getName());
    }

    @Test
    public void setImgName() {
        Student student = new Student();

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
        Student student = new Student();
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
        Student student = new Student();

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
        Student student = new Student();
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
        Student student = new Student().setNumStickers(0);
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
        Student student = new Student();
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
