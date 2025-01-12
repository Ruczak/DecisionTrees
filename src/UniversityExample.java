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

        ArrayList<Course> courseSet = new ArrayList<>(Arrays.asList(courses));

        int cPos = 0;
        Course predictedCourse = courses[cPos];

        System.out.println("Predicted course grade: " + predictedCourse.name + "[" + cPos + "]");
        System.out.println("Entropy: " + globalSet.getEntropy(cPos));

        Node<Student, Double> tree = RandomForest.generateDecisionTree(globalSet, courseSet, cPos, 4);
        RandomForest forest = new RandomForest(cPos, students, courses, 1000);
        forest.generate(3, 10);

        System.out.println("For random forest:");
        System.out.println("Out-of-bag error: " + forest.getOOBError());
    }
}
