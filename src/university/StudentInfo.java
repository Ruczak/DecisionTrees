package university;

public class StudentInfo {

    public String neuroSIL; // [nothing | low | medium | high | full]
    public int plasmaCQ;
    public int chronoAR; // [tau]
    public char telepathicSI; // [A-F]
    public double aethericRC; // [Hz]

    public int neuroSIL_asInt; // [0, 1, 2, 3, 4]
    public int telepathicSI_asInt; // [1, 2, 3, 4, 5, 6]

    public static String[] propertyNames = {"None", "neuroSIL", "plasmaCQ", "chronoAR", "telepathicSI", "aethericRC"};
    public static double[][][] propertyGroups = {
        {{Integer.MIN_VALUE, Integer.MAX_VALUE}}, // None
        {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}}, // neuroSIL
        {{Integer.MIN_VALUE, -1}, {0, Integer.MAX_VALUE}}, // plasmaCQ
        {{1, 1}, {2, 2}, {3, 3}}, // chronoAR
        {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}}, // telepathicSI
        {{0.1, 0.1}, {0.5, 0.5}, {1.0, 1.0}, {5.0, 5.0}} // aethericRC
        };
    
    public double[] properties;
    public int[] propertyIndexes;

    public StudentInfo(String neurosil, int plasmacq, int chronoar, char telepathicsi, double aethericrc) {

        neuroSIL = neurosil;
        plasmaCQ = plasmacq;
        chronoAR = chronoar;
        telepathicSI = telepathicsi;
        aethericRC = aethericrc;
        switch (neurosil) {
            case "nothing" -> neuroSIL_asInt = 0;
            case "low" -> neuroSIL_asInt = 1;
            case "medium" -> neuroSIL_asInt = 2;
            case "high" -> neuroSIL_asInt = 3;
            case "full" -> neuroSIL_asInt = 4;
            default -> throw new IllegalStateException("Unexpected value: " + neurosil);
        }
        if(telepathicsi == 'A') {
            telepathicSI_asInt = 1;
        } else if(telepathicsi == 'B') {
            telepathicSI_asInt = 2;
        } else if(telepathicsi == 'C') {
            telepathicSI_asInt = 3;
        } else if(telepathicsi == 'D') {
            telepathicSI_asInt = 4;
        } else if(telepathicsi == 'E') {
            telepathicSI_asInt = 5;
        } else if(telepathicsi == 'F') {
            telepathicSI_asInt = 6;
        }

        // stores the values of the properties in an array for easier iteration in some use cases
        this.properties = new double[]{0, neuroSIL_asInt, plasmaCQ, chronoAR, telepathicSI_asInt, aethericRC};

        // indexes used to navigate the propertyGroups array (e.g. if propertyIndexes[1] = 3, this corresponds to group {3, 3} in neuroSIL)
        // group {3, 3} encompasses all students who score between 3 and 3 on neuroSIL (essentially propertyIndex indicates which groups this student belongs to)
        int[] propertyIndexes = new int[properties.length];

        for (int i = 0; i < properties.length; i++) {

            for (int j = 0; j < propertyGroups[i].length; j++) { // iterate through groups
                if (properties[i] >= propertyGroups[i][j][0] && properties[i] <= propertyGroups[i][j][1]) {
                    propertyIndexes[i] = j;
                    break;
                }
            }
        }
    }
}