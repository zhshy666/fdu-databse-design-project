package project.backend.Entity;

public class TreatmentRegion {
    private int level;
    private int nurse_num;
    private int nurse_resp_num;

    public TreatmentRegion(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNurse_num() {
        return nurse_num;
    }

    public void setNurse_num(int nurse_num) {
        this.nurse_num = nurse_num;
    }

    public int getNurse_resp_num() {
        return nurse_resp_num;
    }

    public void setNurse_resp_num(int nurse_resp_num) {
        this.nurse_resp_num = nurse_resp_num;
    }
}
