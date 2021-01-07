package project.backend.Controller.Request;

import java.util.Date;

public class RecordPatientStatusRequest {
    private String hospital_nurse_id;
    private int id;
    private String name;
    private double temperature;
    private String symptom;
    private String life_status;
    private String disease_level;
    private Date date;

    public String getHospital_nurse_id() {
        return hospital_nurse_id;
    }

    public void setHospital_nurse_id(String hospital_nurse_id) {
        this.hospital_nurse_id = hospital_nurse_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
