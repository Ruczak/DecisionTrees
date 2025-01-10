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
        ArrayList<Course> courseSet = (ArrayList<Course>) Arrays.asList(courses);

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

    public static class GradeTester implements DecisionTree.Tester<Student> {
        private final int courseIndex;
        private final double splitValue;

        public GradeTester(int courseIndex, double splittingValue) {
            this.courseIndex = courseIndex;
            this.splitValue = splittingValue;
        }

        @Override
        public boolean test(Student object) {
            return object.grades[courseIndex] >= splitValue;
        }

        @Override
        public String toString() {
            return "GradeTester{" + "courseIndex=" + courseIndex + ", splitValue=" + splitValue + '}';
        }
    }
}
