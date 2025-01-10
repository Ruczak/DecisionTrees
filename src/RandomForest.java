import decisionTree.*;
import university.Course;
import university.Student;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomForest {
    private final Course[] courses;
    private final Random random;
    private final StudentSet studentSet;
    private ArrayList<Node<Student, Double>> decisionTrees = null;
    private final int classIndex;

    public RandomForest(int classIndex, Student[] students, Course[] courses, long seed) {
        random = new Random(seed);
        this.studentSet = new StudentSet(students);
        this.courses = courses;
        this.classIndex = classIndex;
    }

    public void generate(int featureCount, int treeCount) {
        // creating
        decisionTrees = new ArrayList<>();

        for (int i = 0; i < treeCount; i++) {
            StudentSet bootstrappedSet = getBootstrappedSet();
            decisionTrees.add(generateDecisionTree(
                    bootstrappedSet,
                    (ArrayList<Course>) Arrays.asList(getRandomCourses(featureCount)),
                    classIndex,
                    featureCount
            ));
        }
    }

    public double classify(Student student) {
        int[] outcomeCount = new int[10];

        for (Node<Student, Double> decisionTree : decisionTrees) {
            int index = decisionTree.getConditionalClass(student).intValue();
        }

        int maxIndex = 0;
        for (int i = 1; i < outcomeCount.length; i++) {
            if (outcomeCount[i] > outcomeCount[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private StudentSet getBootstrappedSet() {
        StudentSet newSet = new StudentSet();

        while (newSet.size() < studentSet.size()) {
            newSet.add(studentSet.get(random.nextInt(studentSet.size())));
        }

        return newSet;
    }

    private Course[] getRandomCourses(int count) {
        Course[] newCourses = new Course[count];
        for (int i = 0; i < count; i++) {
            newCourses[i] = courses[random.nextInt(courses.length)];
        }

        return newCourses;
    }

    public static Node<Student, Double> generateDecisionTree(StudentSet studentSet, ArrayList<Course> courseSet, int classIndex, int depth) {
        if (depth < 0 || studentSet.getEntropy(classIndex) <= 0.0 || courseSet.isEmpty()) {
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

        for (Course course : courseSet) {
            int argumentIndex = course.coursePosition;

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

        final int finalMaxGainCourseIndex = maxGainCourseIndex;
        courseSet.removeIf(course -> course.coursePosition == finalMaxGainCourseIndex);
        DecisionTree.Tester<Student> tester = new GradeTester(maxGainCourseIndex, maxGainSplitValue);
        StudentSet.SetPair pair = studentSet.subdivide(maxGainCourseIndex, maxGainSplitValue);

        return new DecisionTree<>(
                tester,
                generateDecisionTree(pair.left(), courseSet, classIndex, depth - 1),
                generateDecisionTree(pair.right(), courseSet, classIndex, depth - 1)
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
}
