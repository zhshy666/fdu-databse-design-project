package project.backend.Controller.Request;

import java.util.Date;

public class RecordChecklistRequest {
    private String hospital_nurse_id;
    private int patient_id;
    private String test_result;
    private String disease_level;
    private Date date;
    private int checklist_id;

    public String getHospital_nurse_id() {
        return hospital_nurse_id;
    }

    public void setHospital_nurse_id(String hospital_nurse_id) {
        this.hospital_nurse_id = hospital_nurse_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public String getDisease_level() {
        return disease_level;
    }

    public void setDisease_level(String disease_level) {
        this.disease_level = disease_level;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(int checklist_id) {
        this.checklist_id = checklist_id;
    }
}
