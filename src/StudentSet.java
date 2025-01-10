import university.Student;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentSet extends ArrayList<Student> {
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

        return "StudentSet[" + newString + "]";
    }
}