package project.backend.Entity;

import java.sql.Date;

public class PatientStatusInfo {
    private int id;
    private double temperature;
    private String symptom;
    private String life_status;
    private Date date;
    private String nurse_id;
    private String result;

    public PatientStatusInfo(int id, double temperature, String symptom, String life_status, Date date, String nurse_id) {
        this.id = id;
        this.temperature = temperature;
        this.symptom = symptom;
        this.life_status = life_status;
        this.date = date;
        this.nurse_id = nurse_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getLife_status() {
        return life_status;
    }

    public void setLife_status(String life_status) {
        this.life_status = life_status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNurse_id() {
        return nurse_id;
    }

    public void setNurse_id(String nurse_id) {
        this.nurse_id = nurse_id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
