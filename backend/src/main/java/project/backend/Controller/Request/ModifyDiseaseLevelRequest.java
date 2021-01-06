package project.backend.Controller.Request;

public class ModifyDiseaseLevelRequest {
    private String doctor_id;
    private int patient_id;
    private String new_disease_level;

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

    public String getNew_disease_level() {
        return new_disease_level;
    }

    public void setNew_disease_level(String new_disease_level) {
        this.new_disease_level = new_disease_level;
    }
}
