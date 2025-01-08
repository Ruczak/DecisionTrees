package university;

public abstract class Course{
    public String name;
    public double[] grades;
    public double averageGrade;
    public int coursePosition; // course column in the csv file

    public Course(String Name, double[] Grades) {
        name = Name;
        grades = Grades;
        coursePosition = retrievePosition(name);
        averageGrade = Student.avg(grades);
    }

    @Override
    public String toString() {
        return "University.Course{name='" + name + "', averageGrade=" + averageGrade + "}";
    }

    public static int retrievePosition(String name) {
        return switch (name) {
            case "ExoGenetics Evolution" -> 1;
            case "Transdimensional Navigation" -> 2;
            case "Warp Field Theory" -> 3;
            case "Dark Matter Biophysics" -> 4;
            case "Zarnithian Philosophy" -> 5;
            case "Drakthon Linguistics" -> 6;
            case "Xynthium Material Sciences" -> 7;
            case "Hyperspace Topology" -> 8;
            case "Helio-Bio Interface" -> 9;
            case "Luminarian Art Theory" -> 10;
            case "Stellar Cartography" -> 11;
            case "Chrono-Kinetics" -> 12;
            case "Technotronic Linguistic Fusion" -> 13;
            case "Cybernetic Ethics" -> 14;
            case "Nebulon Astrophysics" -> 15;
            case "Quasar Dynamics" -> 16;
            case "Glacial Holo-Architecture" -> 17;
            case "Aether Resonance" -> 18;
            case "Gravix Planetary Studies" -> 19;
            case "Vortex Quantum Mechanics" -> 20;
            case "Holo-Temporal Engineering" -> 21;
            case "Psi-Energy Manipulation" -> 22;
            case "Plasmawave Analysis" -> 23;
            case "Neutronia Metallurgy" -> 24;
            case "Syntho-Chemical Engineering" -> 25;
            case "Sublight Propulsion Systems" -> 26;
            case "Krythos Biomechanics" -> 27;
            case "Flux Capacitor Management" -> 28;
            case "Xyloprax Computation" -> 29;
            case "Yridium Power Systems" -> 30;
            case "Quantum Neuro-Hacking" -> 31;
            case "Zyglon Neurology" -> 32;
            default -> 0;
        };
    }


}