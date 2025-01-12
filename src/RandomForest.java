import decisionTree.*;
import university.Course;
import university.CurrStudent;
import university.Student;

import java.util.*;

public class RandomForest {
    private final Course[] courses;
    private final Random random;
    private final StudentSet studentSet;
    private ArrayList<Node<Student, Double>> decisionTrees = null;
    private final int classIndex;
    private final HashMap<Node<Student, Double>, StudentSet> bootstrappedSets = new HashMap<>();

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
            ArrayList<Course> randomCourses = new ArrayList<>(Arrays.asList(getRandomCourses(featureCount)));
            StudentSet bootstrappedSet = getBootstrappedSet();

            Node<Student, Double> decisionTree = generateDecisionTree(
                    bootstrappedSet,
                    randomCourses,
                    classIndex,
                    featureCount
            );
            decisionTrees.add(decisionTree);
            bootstrappedSets.put(decisionTree, bootstrappedSet);
        }
    }

    public double classify(Student student) {
        int[] outcomeCount = new int[10];

        for (Node<Student, Double> decisionTree : decisionTrees) {
            int index = decisionTree.getConditionalClass(student).intValue();
            outcomeCount[index]++;
        }

        int maxIndex = 0;
        for (int i = 1; i < outcomeCount.length; i++) {
            if (outcomeCount[i] > outcomeCount[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    public double regress(Student student) {
        double sum = 0.0;

        for (Node<Student, Double> decisionTree : decisionTrees) {
            int index = decisionTree.getConditionalClass(student).intValue();
            sum += index;
        }

        return sum / decisionTrees.size();
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
        if (depth < 0 || studentSet.getEntropy(classIndex) <= 0.0 || courseSet.isEmpty() || courseSet.getFirst().coursePosition == classIndex) {
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

        // getting best decision split
        int maxGainCourseIndex = -1;
        double maxGain = -1;
        double maxGainSplitValue = -1;

        for (Course course : courseSet) {
            int argumentIndex = course.coursePosition;

            if (argumentIndex == classIndex) continue;

            for (double j = 6.0; j <= 9.0; j++) {
                double gain = studentSet.getInformationGain(classIndex, argumentIndex, j);
                if (gain > maxGain || maxGainCourseIndex == -1) {
                    maxGain = gain;
                    maxGainSplitValue = j;
                    maxGainCourseIndex = argumentIndex;
                }
            }
        }

        final int finalMaxGainCourseIndex = maxGainCourseIndex; // needs to be (java syntax thing :/)
        courseSet.removeIf(course -> course.coursePosition == finalMaxGainCourseIndex);
        DecisionTree.Tester<Student> tester = new GradeTester(maxGainCourseIndex, maxGainSplitValue);
        StudentSet.SetPair pair = studentSet.subdivide(maxGainCourseIndex, maxGainSplitValue);

        return new DecisionTree<>(
                tester,
                generateDecisionTree(pair.left(), courseSet, classIndex, depth - 1),
                generateDecisionTree(pair.right(), courseSet, classIndex, depth - 1)
        );
    }

    public double getOOBError() {
        HashMap<Student, ArrayList<Node<Student, Double>>> outOfBagTrees = new HashMap<>();
        for (Student student : studentSet) {
            ArrayList<Node<Student, Double>> trees = new ArrayList<>(decisionTrees);
            outOfBagTrees.put(student, trees);
        }

        for (Map.Entry<Node<Student, Double>, StudentSet> bootstrapEntry : bootstrappedSets.entrySet()) {
            Node<Student, Double> tree = bootstrapEntry.getKey();
            StudentSet bootstrappedSet = bootstrapEntry.getValue();

            for (Student student : bootstrappedSet) {
                if (outOfBagTrees.containsKey(student))
                    outOfBagTrees.get(student).remove(tree);
            }
        }

        double totalErrorSum = 0.0;

        for (Map.Entry<Student, ArrayList<Node<Student, Double>>> oobEntry : outOfBagTrees.entrySet()) {
            Student student = oobEntry.getKey();
            ArrayList<Node<Student, Double>> trees = oobEntry.getValue();

            if (trees.isEmpty()) continue;

            double errorSum = 0.0;

            for (Node<Student, Double> tree : trees) {
                // Loss function: mean absolute error (MAE)
                double error = Math.abs(student.grades[classIndex] - tree.getConditionalClass(student));
                errorSum += error;
            }

            totalErrorSum += errorSum / trees.size();
        }

        return totalErrorSum / outOfBagTrees.size();
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

    public static class PlasmaTester implements DecisionTree.Tester<CurrStudent> {
        private final int splitValue;

        public PlasmaTester(int splitValue) {
            this.splitValue = splitValue;
        }

        @Override
        public boolean test(CurrStudent object) {
            return object.info.plasmaCQ >= splitValue;
        }
    }

    public static class StudentInfoTester implements DecisionTree.Tester<CurrStudent> {
        public enum Property {
            NEURO_SIL,
            CHRONO_AR,
            TELEPATHIC_SI,
            AETHERIC_RC
        }

        public final String[] neuroValues = new String[] {"nothing", "low", "medium", "high", "full"};
        public final int[] chronoValues = new int[] {1,2,3};
        public final char[] telepathicValues = new char[] {'A', 'B', 'C', 'D', 'E', 'F'};
        public final double[] aethericValues = new double[] {0.1, 0.5, 1.0, 5.0};

        private final int splitIndex;
        private final Property selectedProperty;

        public StudentInfoTester(Property property, int splitIndex) throws IllegalArgumentException {
            this.splitIndex = splitIndex;
            int maxSplitIndex = 0;
            switch (property) {
                case NEURO_SIL -> maxSplitIndex = neuroValues.length;
                case CHRONO_AR -> maxSplitIndex = chronoValues.length;
                case TELEPATHIC_SI -> maxSplitIndex = telepathicValues.length;
                case AETHERIC_RC -> maxSplitIndex = aethericValues.length;
            }

            if (maxSplitIndex < splitIndex)
                throw new IllegalArgumentException("Invalid split index for a given property (" + property + "): " + splitIndex + "(max: " + maxSplitIndex + ")");

            selectedProperty = property;
        }

        @Override
        public boolean test(CurrStudent object) {
            int index = -1;

            switch (selectedProperty) {
                case NEURO_SIL -> {
                    for (int i = 0; i < neuroValues.length; i++) {
                        if (neuroValues[i].equals(object.info.neuroSIL)) {
                            index = i;
                            break;
                        }
                    }
                }
                case CHRONO_AR -> {
                    for (int i = 0; i < chronoValues.length; i++) {
                        if (chronoValues[i] == object.info.chronoAR) {
                            index = i;
                            break;
                        }
                    }
                }
                case TELEPATHIC_SI ->
                {
                    for (int i = 0; i < telepathicValues.length; i++) {
                        if (telepathicValues[i] == object.info.telepathicSI) {
                            index = i;
                            break;
                        }
                    }
                }
                case AETHERIC_RC -> {
                    for (int i = 0; i < aethericValues.length; i++) {
                        if (aethericValues[i] == object.info.aethericRC) {
                            index = i;
                            break;
                        }
                    }
                }
            }

            return index >= splitIndex;
        }
    }
}
