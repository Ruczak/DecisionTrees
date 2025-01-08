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
    }

    public static class StudentSet extends ArrayList<Student> {
        public StudentSet() {
            super();
        }

        public StudentSet(Student[] students) {
            super(students.length);
            this.addAll(Arrays.asList(students));
        }

        public double getEntropy(int classIndex) {
            int[] gradeCount = new int[10];

            for (Student student : this) {
                int index = (int)student.grades[classIndex] - 1;
                if (index > 0) gradeCount[index]++;
            }

            double sum = 0;

            for (int j : gradeCount) {
                double proportion = (double) j / size();
                if (proportion > 0) sum -= proportion * Math.log10(proportion);
            }

            return sum;
        }

        public double getInformationGain(int classIndex, int argumentIndex, double splittingValue) {
            StudentSet s1 = new StudentSet();
            StudentSet s2 = new StudentSet();

            for (Student student : this) {
                if (student.grades[argumentIndex] >= splittingValue) s1.add(student);
                else s2.add(student);
            }

            double p1 = (double)s1.size() / size();
            double p2 = (double)s2.size() / size();

            return getEntropy(classIndex) - p1 * s1.getEntropy(classIndex) - p2 * s2.getEntropy(classIndex);
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
