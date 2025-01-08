package university;

public class CurrStudent extends Student {
    public StudentInfo info;

    public CurrStudent(int sid, double[] grades) {
        super(sid, grades);
        this.info = new StudentInfo("nothing", 0, 0, 'A', 0); // Initialize the info object here
    }

    public void recodeFields() {
        switch (this.info.neuroSIL) {
            case "nothing" -> this.info.neuroSIL_asInt = 0;
            case "low" -> this.info.neuroSIL_asInt = 1;
            case "medium" -> this.info.neuroSIL_asInt = 2;
            case "high" -> this.info.neuroSIL_asInt = 3;
            case "full" -> this.info.neuroSIL_asInt = 4;
        }

        if (this.info.telepathicSI == 'A') { this.info.telepathicSI_asInt = 1; }
        else if (this.info.telepathicSI == 'B') { this.info.telepathicSI_asInt = 2; }
        else if (this.info.telepathicSI == 'C') { this.info.telepathicSI_asInt = 3; }
        else if (this.info.telepathicSI == 'D') { this.info.telepathicSI_asInt = 4; }
        else if (this.info.telepathicSI == 'E') { this.info.telepathicSI_asInt = 5; }
        else if (this.info.telepathicSI == 'F') { this.info.telepathicSI_asInt = 6; }
    }
}