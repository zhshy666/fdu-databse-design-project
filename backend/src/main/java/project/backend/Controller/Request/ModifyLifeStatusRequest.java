package project.backend.Controller.Request;

public class ModifyLifeStatusRequest {
    private String doctor_id;
    private int patient_id;
    private String new_life_status;

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getNew_life_status() {
        return new_life_status;
    }

    public void setNew_life_status(String new_life_status) {
        this.new_life_status = new_life_status;
    }
}
