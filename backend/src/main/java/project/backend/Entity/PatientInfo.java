package project.backend.Entity;

public class PatientInfo {
    private int patient_id;
    private String name;
    private String gender;
    private int age;
    private String disease_level;
    private String life_status;
    private String nurse_id;
    private int treatment_region_level;
    private int canBeDischarged;
    private int needTransfer;

    public PatientInfo(int patient_id, String name, String gender, int age,
                       String disease_level, String life_status, String nurse_id, int treatment_region_level) {
        this.patient_id = patient_id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.disease_level = disease_level;
        this.life_status = life_status;
        this.nurse_id = nurse_id;
        this.treatment_region_level = treatment_region_level;
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

    public int getTreatment_region_level() {
        return treatment_region_level;
    }

    public void setTreatment_region_level(int treatment_region_level) {
        this.treatment_region_level = treatment_region_level;
    }

    public int getCanBeDischarged() {
        return canBeDischarged;
    }

    public void setCanBeDischarged(int canBeDischarged) {
        this.canBeDischarged = canBeDischarged;
    }

    public int getNeedTransfer() {
        return needTransfer;
    }

    public void setNeedTransfer(int needTransfer) {
        this.needTransfer = needTransfer;
    }
}
