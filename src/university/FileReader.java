package university;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class FileReader {
	//Improved method (290 ms -> 140 ms)
	public static void read(String fileName, String[] cNames, int[] sIDs, double[][] grades, boolean printer) {
		int cNamesIterator = 0;
		int sIDsIterator = 0;
		int gradesI = -1; int gradesJ = 0;
		try {
			File file = new File(fileName);
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] tokens = line.split(",");
				for (String token : tokens) {
					// Handle student ID
					if (isInteger(token)) {
						sIDs[sIDsIterator] = Integer.parseInt(token);
						sIDsIterator++;
						continue;
					}
					// Handle grades
					Double grade = parseGrade(token);
					if (grade != null) {
						double gradeValue = parseGrade(token);
						grades[gradesI][gradesJ] = gradeValue;
						gradesJ++;
						continue;
					}
					// Handle class names
					if (cNamesIterator != 0) {
						cNames[cNamesIterator - 1] = token;
					}
					cNamesIterator++;
				}
				gradesI++;
				gradesJ = 0;
			}
			fileScanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (printer) {
			//if you dont want the University.FileReader to print stuff,
			//just pass "printer" as false.
			System.out.println("cNames:");
			System.out.println(Arrays.toString(cNames));
			System.out.println();
			System.out.println("sIDs:");
			System.out.println(Arrays.toString(sIDs));
			System.out.println();
			System.out.println("grades:");
			System.out.println(Arrays.deepToString(grades));
		}
	}

	public static void read(String fileName, Student[] students, Course[] courses, boolean printer) {
		String[] cNames = new String[courses.length];
		int[] sIDs = new int[students.length];
		double[][] grades = new double[students.length][courses.length];
		read(fileName, cNames, sIDs, grades, printer);
		String type = "";
		if(students.length == 1328) {
			type = "Curr";
		} else {
			type = "Grad";
		}
		if(type.equals("Curr")) {
			for(int i=0; i<students.length; i++) {
				Student student = new CurrStudent(sIDs[i], grades[i]);
				students[i] = student;
			}
			for(int i=0; i<courses.length; i++) {
				double[] courseGrades = new double[33];
				for(int j=0; j<33; j++) {
					courseGrades[i] = grades[j][i];
				}
				Course course = new CurrCourse(cNames[i], courseGrades);
				courses[i] = course;
			}
		} else {
			for(int i=0; i<students.length; i++) {
				Student student = new GradStudent(sIDs[i], grades[i]);
				students[i] = student;
			}
			for(int i=0; i<courses.length; i++) {
				double[] courseGrades = new double[33];
				for(int j=0; j<33; j++) {
					courseGrades[i] = grades[j][i];
				}
				Course course = new GradCourse(cNames[i], courseGrades);
				courses[i] = course;
			}
		}
	}

	// Helper method to check if a string is an integer
	private static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	// Helper method to parse a grade
	private static Double parseGrade(String str) {
		if (str.equals("NG")) {
			return -1.0;
		} else {
			try {
				return Double.parseDouble(str);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
	}

	// assuming that sIDs in CurrentGrades.csv and University.StudentInfo.csv are in the same order (before any sorting)
	public static void readStudentInfo(String fileName, CurrStudent[] students) {
		try {
			File file = new File(fileName);
			Scanner fileScanner = new Scanner(file);
			fileScanner.nextLine(); // Skip the first line
			int i = 0;
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] tokens = line.split(",");

				// Assuming the order in the file matches the students array
				CurrStudent student = students[i];
				student.info.neuroSIL = tokens[1];
				student.info.plasmaCQ = Integer.parseInt(tokens[2]);
				student.info.chronoAR = Integer.parseInt(tokens[3].split(" ")[0]);
				student.info.telepathicSI = tokens[4].strip().charAt(0);
				student.info.aethericRC = Double.parseDouble(tokens[5].split(" ")[0]);

				i++;
			}
			fileScanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Recode fields as needed (same as your current implementation)
		for (CurrStudent student : students) {
			student.recodeFields();
		}
	}

	public static void main(String[] args) {
		String filename = "C:\\Users\\HP\\ProjectAgain\\out\\production\\ProjectAgain\\CurrentGrades.csv\\";
		Student[] students = new Student[1328];
		Course[] courses = new Course[33];
		read(filename, students, courses, true);
		System.out.println(students[1327].sID);
	}
}