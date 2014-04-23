package edu.mills.cs180a.pocketpoints.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.mills.cs180a.pocketpoints.Student;
import edu.mills.cs180a.pocketpoints.StudentManager;

public class StudentManagerTest extends AndroidTestCase {
    // Fields for Student 1 (Name is Lexicographically before student2).
    private static final String STUDENT_1_NAME = "Phillipa Gordon";
    private static final String STUDENT_1_IMG_NAME = "PhilG_student_profile.jpg";
    private static final int STUDENT_1_NUM_STICKERS = 4;

    // Fields for Student 2 (Name is Lexicographically after student1).
    private static final String STUDENT_2_NAME = "Stella Maynard";
    private static final String STUDENT_2_IMG_NAME = "StelMay_student_profile.jpg";
    private static final int STUDENT_2_NUM_STICKERS = 0;

    StudentManager testStudentManager;
    private Student student1;
    private Student student2;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create an isolated context that does not affect the production database.
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        // Initialize the variables that will be used during this test.
        testStudentManager = StudentManager.getTestInstance(context);
        student1 = new Student()
                .setName(STUDENT_1_NAME)
                .setImgName(STUDENT_1_IMG_NAME)
                .setNumStickers(STUDENT_1_NUM_STICKERS);
        student2 = new Student()
                .setName(STUDENT_2_NAME)
                .setImgName(STUDENT_2_IMG_NAME)
                .setNumStickers(STUDENT_2_NUM_STICKERS);
    }

    public void testCreateStudent() {
        verifyDatabaseEmpty();

        // Create a student in the database.
        boolean studentAdded = testStudentManager.createStudent(student1);
        assertTrue(studentAdded);

        // Make sure the student was added to the database.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyAllFieldsEqual(student1, students.get(0));

        // Create a second student in the database.
        studentAdded = testStudentManager.createStudent(student2);
        assertTrue(studentAdded);
        assertFalse(student1.getId() == student2.getId()); // Verify the student IDs are different.

        // Make sure that both students are now in the database.
        students = testStudentManager.getAllStudents();
        assertEquals(2, students.size());
        for (Student student : students) {
            long studentId = student.getId();
            if (studentId == student1.getId()) {
                verifyAllFieldsEqual(student1, student);
            } else if (studentId == student2.getId()) {
                verifyAllFieldsEqual(student2, student);
            } else {
                fail("Unexpected student in the database with id = " + studentId);
            }
        }
    }

    public void testCreateStudent_duplicateNamesAllowed() {
        verifyDatabaseEmpty();

        // Create a student in the database.
        boolean studentAdded = testStudentManager.createStudent(student1);
        assertTrue(studentAdded);

        // Make sure the student was added to the database.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyAllFieldsEqual(student1, students.get(0));

        // Create a second student (with identical fields as student1) in the database.
        Student student1Copy = new Student()
                .setName(STUDENT_1_NAME)
                .setImgName(STUDENT_1_IMG_NAME)
                .setNumStickers(STUDENT_1_NUM_STICKERS);

        // Add this new student to the database.
        studentAdded = testStudentManager.createStudent(student1Copy);
        assertTrue(studentAdded);
        assertFalse(student1.getId() == student2.getId()); // Verify the student IDs are different.

        // Make sure that both students are now in the database.
        students = testStudentManager.getAllStudents();
        assertEquals(2, students.size());
        for (Student student : students) {
            long studentId = student.getId();
            if (studentId == student1.getId()) {
                verifyAllFieldsEqual(student1, student);
            } else if (studentId == student1Copy.getId()) {
                verifyAllFieldsEqual(student1Copy, student);
            } else {
                fail("Unexpected student in the database with id = " + studentId);
            }
        }
    }

    public void testGetStudents_ReturnsStudentsInAlphabeticalOrder() {
        verifyDatabaseEmpty();

        // Create students to add to the database.
        Student studentC = new Student().setName("Cat");
        Student studentE = new Student().setName("Eduardo");
        Student studentJa = new Student().setName("Jack");
        Student studentJi = new Student().setName("Jimmy");
        Student studentS = new Student().setName("Stan");

        // Add the students to the database in non-alphabetical order.
        testStudentManager.createStudent(studentJi);
        testStudentManager.createStudent(studentS);
        testStudentManager.createStudent(studentC);
        testStudentManager.createStudent(studentJa);
        testStudentManager.createStudent(studentE);

        // Verify that getAllStudents() returns all the students in alphabetical order.
        List<Student> studentList = testStudentManager.getAllStudents();
        assertEquals(5, studentList.size());
        verifyAllFieldsEqual(studentC, studentList.get(0));
        verifyAllFieldsEqual(studentE, studentList.get(1));
        verifyAllFieldsEqual(studentJa, studentList.get(2));
        verifyAllFieldsEqual(studentJi, studentList.get(3));
        verifyAllFieldsEqual(studentS, studentList.get(4));
    }

    public void testGetStudent_databaseEmpty() {
        verifyDatabaseEmpty();

        // Try to get a student that doesn't exist.
        assertNull(testStudentManager.getStudent(1));

        // Verify database unchanged.
        verifyDatabaseEmpty();
    }

    public void testGetStudent_databaseNotEmpty_studentDoesNotExist() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));

        // Try to get a student that doesn't exist.
        assertNull(testStudentManager.getStudent(student1.getId() + 1));

        // Verify database unchanged.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyAllFieldsEqual(student1, students.get(0));
    }

    public void testGetStudent_studentExists() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));
        assertTrue(testStudentManager.createStudent(student2));
        assertFalse(student1.getId() == student2.getId());

        // Get student1 from the database.
        Student student = testStudentManager.getStudent(student1.getId());
        verifyAllFieldsEqual(student1, student);

        // Get student2 from the database.
        student = testStudentManager.getStudent(student2.getId());
        verifyAllFieldsEqual(student2, student);
    }

    public void testUpdateStudent_databaseEmpty() {
        verifyDatabaseEmpty();

        // Try to update a student that doesn't exist.
        assertFalse(testStudentManager.updateStudent(student1));

        // Verify database unchanged.
        verifyDatabaseEmpty();
    }

    public void testUpdateStudent_databaseNotEmpty_studentDoesNotExist() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));

        // Try to update a student that doesn't exist.
        long student1Id = student1.getId();
        assertFalse(testStudentManager.updateStudent(student2));

        // Verify database unchanged.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyFieldsAsExpected(students.get(0), student1Id, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
    }

    public void testUpdateStudent_studentExists() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));
        assertTrue(testStudentManager.createStudent(student2));
        long student1Id = student1.getId();
        long student2Id = student2.getId();
        assertFalse(student1Id == student2Id);

        // Update student1.
        String newName = "Royal Gardner";
        String newImgName = "rgardner_profile_photo.jpg";
        int newNumStickers = 90;
        student1.setName(newName).setImgName(newImgName).setNumStickers(newNumStickers);
        assertTrue(testStudentManager.updateStudent(student1));

        // Verify student1 updated, student2 not updated.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(2, students.size());
        for (Student student : students) {
            long studentId = student.getId();
            if (studentId == student1Id) {
                verifyFieldsAsExpected(student, student1Id, newName, newImgName, newNumStickers);
            } else if (studentId == student2Id) {
                verifyFieldsAsExpected(student, student2Id, STUDENT_2_NAME, STUDENT_2_IMG_NAME,
                        STUDENT_2_NUM_STICKERS);
            } else {
                fail("Unexpected student in the database with id = " + studentId);
            }
        }
    }

    public void testDeleteStudent_databaseEmpty() {
        verifyDatabaseEmpty();

        // Try to delete a student that doesn't exist.
        assertFalse(testStudentManager.deleteStudent(1));

        // Verify database unchanged.
        verifyDatabaseEmpty();
    }

    public void testDeleteStudent_databaseNotEmpty_studentDoesNotExist() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));

        // Try to delete a student that doesn't exist.
        long student1Id = student1.getId();
        assertFalse(testStudentManager.deleteStudent(student1Id + 1));

        // Verify database unchanged.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyFieldsAsExpected(students.get(0), student1Id, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);
    }

    public void testDeleteStudent_studentExists() {
        verifyDatabaseEmpty();
        assertTrue(testStudentManager.createStudent(student1));
        assertTrue(testStudentManager.createStudent(student2));
        long student1Id = student1.getId();
        long student2Id = student2.getId();
        assertFalse(student1Id == student2Id);

        // Delete student2.
        assertTrue(testStudentManager.deleteStudent(student2.getId()));

        // Verify that only student1 exists in the database, and is unchanged.
        List<Student> students = testStudentManager.getAllStudents();
        assertEquals(1, students.size());
        verifyFieldsAsExpected(students.get(0), student1Id, STUDENT_1_NAME, STUDENT_1_IMG_NAME,
                STUDENT_1_NUM_STICKERS);

        // Delete student1.
        assertTrue(testStudentManager.deleteStudent(student1.getId()));

        // Verify that the database is now empty.
        verifyDatabaseEmpty();
    }

    private void verifyDatabaseEmpty(){
        assertEquals(0, testStudentManager.getAllStudents().size());
    }

    private void verifyAllFieldsEqual(Student expected, Student actual) {
        verifyFieldsAsExpected(actual, expected.getId(), expected.getName(), expected.getImgName(),
                expected.getNumStickers());
    }

    private void verifyFieldsAsExpected(Student student, long expectedId, String expectedName,
            String expectedImgName, int expectedNumStickers) {
        assertEquals(expectedId, student.getId());
        assertEquals(expectedName, student.getName());
        assertEquals(expectedImgName, student.getImgName());
        assertEquals(expectedNumStickers, student.getNumStickers());
    }
}
