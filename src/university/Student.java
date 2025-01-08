package university;

import java.util.ArrayList;

public abstract class Student {
    public int sID;
    public double GPA;
    public double stdDev;
    public double[] grades;

    public Student(int sID, double[] Grades) {
        this.sID = sID;
        grades = Grades;
        GPA = avg(grades);
        stdDev = standardDev(grades);
    }

    public static double avg(double[] grades) {
        double all = 0;
        int mid = 0;

        for (int i = 0; i < grades.length; i++) {
            if (grades[i] == -1.0) continue;
            all = all + grades[i];
            mid++;
        }
        double result = all / mid;
        return result;
    }

    public static double standardDev(double[] grades) {
        ArrayList<Double> valueList = new ArrayList<Double>();
        for(int i=0; i<grades.length; i++) {
            if(grades[i]!=-1) {
                valueList.add(grades[i]);
            }
        }
        double[] values = new double[valueList.size()];
        for(int i=0; i<values.length; i++) {
            values[i] = valueList.get(i);
        }
        double sum = 0;
        double average = avg(values);

        for (int i = 0; i < values.length; i++) {
            sum += (values[i] - average) * (values[i] - average); // (x[i] - x_avg)^2
        }

        return Math.sqrt(sum / values.length);
    }
}