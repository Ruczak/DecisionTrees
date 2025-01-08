package university;

public class CurrCourse extends Course {
    public int ngCount;
    public CurrCourse(String Name, double[] Grades) {
        super(Name, Grades);
        ngCount = retrieveNgCount(grades);
    }

    public int retrieveNgCount(double[] grades) {
        int ngCount = 0;
        for (double grade : grades) {
            if (grade == -1) {
                ngCount++;
            }
        }
        return ngCount;
    }
}
