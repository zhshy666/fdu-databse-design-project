package project.backend.Entity;

public class Checklist {
    private int id;
    private String test_result;
    private String date;
    private int disease_level;
    private String doctor_id;
    private int patient_id;

    public Checklist(){}

    public int getId() {
        return id;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDisease_level() {
        return disease_level;
    }

    public void setDisease_level(int disease_level) {
        this.disease_level = disease_level;
    }

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
}
