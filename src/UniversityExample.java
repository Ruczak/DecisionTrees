import decisionTree.DecisionTree;
import decisionTree.LeafNode;
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

        int cPos = 0;
        Course predictedCourse = courses[cPos];

        System.out.println("Predicted course grade: " + predictedCourse.name + "[" + cPos + "]");
        System.out.println("Entropy: " + globalSet.getEntropy(cPos));

        System.out.println("\nInformation gains for each course");
        for (int i = 0; courses.length > i; i++) {
            if (i == cPos) continue;

            System.out.print(i + ". " + courses[i].name + ": ");

            for (double j = 6.0; j <= 9.0; j++) {
                System.out.printf("Gain(%.1f) = %.3f; ", j, globalSet.getInformationGain(cPos, i, j));
            }
            System.out.println();
        }

        Node<Student, Double> tree = generateDecisionTree(globalSet, cPos, courses, 8);

        if (tree instanceof DecisionTree<Student, Double>) {
            ((DecisionTree<Student, Double>) tree).prettyPrint(8, "");
        }
    }

    public static Node<Student, Double> generateDecisionTree(StudentSet studentSet, int classIndex, Course[] courses, int depth) {
        if (depth < 0 || studentSet.getEntropy(classIndex) <= 0.0) {
            int[] gradeCount = studentSet.getGradeCount(classIndex);
            int maxGradeCount = -1;
            int maxGradeCountIndex = -1;

            for (int i = 0; i < gradeCount.length; i++) {
                if (gradeCount[i] > maxGradeCount) {
                    maxGradeCount = gradeCount[i];
                    maxGradeCountIndex = i;
                }
            }

            return new LeafNode<>(maxGradeCountIndex + 1.0);
        }

        int maxGainCourseIndex = -1;
        double maxGain = -1;
        double maxGainSplitValue = -1;

        for (int i = 0; courses.length > i; i++) {
            int argumentIndex = courses[i].coursePosition;

            if (argumentIndex == classIndex) continue;

            for (double j = 6.0; j <= 9.0; j++) {
                double gain = studentSet.getInformationGain(classIndex, argumentIndex, j);
                if (gain > maxGain) {
                    maxGain = gain;
                    maxGainSplitValue = j;
                    maxGainCourseIndex = argumentIndex;
                }
            }
        }

        DecisionTree.Tester<Student> tester = new GradeTester(maxGainCourseIndex, maxGainSplitValue);
        StudentSet.SetPair pair = studentSet.subdivide(maxGainCourseIndex, maxGainSplitValue);

        return new DecisionTree<>(
                tester,
                generateDecisionTree(pair.left(), classIndex, courses, depth - 1),
                generateDecisionTree(pair.right(), classIndex, courses, depth - 1)
        );
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

    public static class StudentSet extends ArrayList<Student> {
        public StudentSet() {
            super();
        }

        public StudentSet(Student[] students) {
            super(students.length);
            this.addAll(Arrays.asList(students));
        }

        public int[] getGradeCount(int classIndex) {
            int[] gradeCount = new int[10];

            for (Student student : this) {
                int index = (int)student.grades[classIndex] - 1;
                if (index > 0) gradeCount[index]++;
            }

            return gradeCount;
        }

        public double getEntropy(int classIndex) {
            int[] gradeCount = getGradeCount(classIndex);

            double sum = 0;

            for (int j : gradeCount) {
                double proportion = (double) j / size();
                if (proportion > 0) sum -= proportion * Math.log10(proportion);
            }

            return sum;
        }

        public double getInformationGain(int classIndex, int argumentIndex, double splittingValue) {
            SetPair setPair = subdivide(argumentIndex, splittingValue);
            return getInformationGain(classIndex, setPair);
        }

        public double getInformationGain(int classIndex, SetPair splitPair) {
            StudentSet s1 = splitPair.left();
            StudentSet s2 = splitPair.right();

            double p1 = (double)s1.size() / size();
            double p2 = (double)s2.size() / size();

            return getEntropy(classIndex) - p1 * s1.getEntropy(classIndex) - p2 * s2.getEntropy(classIndex);
        }

        public record SetPair(StudentSet left, StudentSet right) {}

        public SetPair subdivide(int argumentIndex, double splittingValue) {
            StudentSet s1 = new StudentSet();
            StudentSet s2 = new StudentSet();

            for (Student student : this) {
                if (student.grades[argumentIndex] >= splittingValue) s1.add(student);
                else s2.add(student);
            }

            return new SetPair(s1, s2);
        }

        @Override
        public String toString() {
            StringBuilder newString = new StringBuilder();

            for (int i = 0; i < this.size(); i++) {
                if (i > 0) newString.append(", ");
                newString.append(get(i).sID);
            }

            return "StudentSet{" + newString + "}";
        }
    }
}
