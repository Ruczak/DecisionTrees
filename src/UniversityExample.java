import decisionTree.DecisionTree;
import decisionTree.Node;
import university.Course;
import university.Student;
import university.FileReader;

import java.util.ArrayList;
import java.util.Arrays;

public class UniversityExample {
    public static void main(String[] args) {
        String filename = "data/GraduateGrades.csv";
        Student[] students = new Student[19354];
        Course[] courses = new Course[33];
        FileReader.read(filename, students, courses, false);

        StudentSet globalSet = new StudentSet(students);

        ArrayList<Course> courseSet = new ArrayList<Course>(Arrays.asList(courses));

        int cPos = 0;
        Course predictedCourse = courses[cPos];

        System.out.println("Predicted course grade: " + predictedCourse.name + "[" + cPos + "]");
        System.out.println("Entropy: " + globalSet.getEntropy(cPos));

        Node<Student, Double> tree = RandomForest.generateDecisionTree(globalSet, courseSet, cPos, 4);

        if (tree instanceof DecisionTree<Student, Double>) {
            ((DecisionTree<Student, Double>) tree).prettyPrint(4, "");
        }

        int correctTests = 0;

        for (Student student : students) {
            if (student.grades[cPos] == tree.getConditionalClass(student)) correctTests++;
        }

        System.out.println("Correct tests: " + correctTests + " per " + students.length + " students.");
        System.out.println("Effectiveness: " + ((double) correctTests / (double) students.length));
    }
}
