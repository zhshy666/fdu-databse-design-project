package project.backend.Entity;

public class Bed {
    private int bed_id;
    private int patient_id;
    private String treatment_region_level;

    public Bed(){}

    public int getBed_id() {
        return bed_id;
    }

    public void setBed_id(int bed_id) {
        this.bed_id = bed_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getTreatment_region_level() {
        return treatment_region_level;
    }

    public void setTreatment_region_level(String treatment_region_level) {
        this.treatment_region_level = treatment_region_level;
    }
}
