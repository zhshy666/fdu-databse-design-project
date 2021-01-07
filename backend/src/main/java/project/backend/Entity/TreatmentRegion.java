package project.backend.Entity;

public class TreatmentRegion {
    private String level;
    private String nurse_id;
    private String doctor_id;
    private int nurse_resp_num;

    public TreatmentRegion(){}

    public String getLevel() {
        return level;
    }

    public String getNurse_id() {
        return nurse_id;
    }

    public void setNurse_id(String nurse_id) {
        this.nurse_id = nurse_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getNurse_resp_num() {
        return nurse_resp_num;
    }

    public void setNurse_resp_num(int nurse_resp_num) {
        this.nurse_resp_num = nurse_resp_num;
    }
}
