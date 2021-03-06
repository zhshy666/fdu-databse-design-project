package project.backend.Entity;

import java.sql.Timestamp;
import java.util.Date;

public class Checklist {
    private int id;
    private String test_result;
    private Date date;
    private String disease_level;
    private String doctor_id;
    private int patient_id;

    public Checklist() {
    }

    public Checklist(String test_result, Date date, String disease_level) {
        this.test_result = test_result;
        this.date = date;
        this.disease_level = disease_level;
    }

    public Checklist(Timestamp date) {
        this.date = new Date(date.getTime());
    }

    public int getId() {
        return id;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = new Date(date.getTime());
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisease_level() {
        return disease_level;
    }

    public void setDisease_level(String disease_level) {
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
