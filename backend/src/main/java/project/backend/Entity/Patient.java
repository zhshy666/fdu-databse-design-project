package project.backend.Entity;

public class Patient {
    private int patient_id;
    private String name;
    private String gender;
    private int age;
    private String disease_level;
    private String life_status;
    private String nurse_id;
    private String treatment_region_level;

    public Patient(){}

    public Patient(String name, String gender, int age, String disease_level) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.disease_level = disease_level;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDisease_level() {
        return disease_level;
    }

    public void setDisease_level(String disease_level) {
        this.disease_level = disease_level;
    }

    public String getLife_status() {
        return life_status;
    }

    public void setLife_status(String life_status) {
        this.life_status = life_status;
    }

    public String getNurse_id() {
        return nurse_id;
    }

    public void setNurse_id(String nurse_id) {
        this.nurse_id = nurse_id;
    }

    public String getTreatment_region_level() {
        return treatment_region_level;
    }

    public void setTreatment_region_level(String treatment_region_level) {
        this.treatment_region_level = treatment_region_level;
    }
}
