import university.Course;
import university.Student;

import university.FileReader;

public class UniversityExample {
    public static void main(String[] args) {
        String filename = "data/CurrentGrades.csv";
        Student[] students = new Student[1328];
        Course[] courses = new Course[33];
        FileReader.read(filename, students, courses, true);


    }
}
