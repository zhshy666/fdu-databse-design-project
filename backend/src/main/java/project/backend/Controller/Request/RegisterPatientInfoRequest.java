package project.backend.Controller.Request;

import java.sql.Timestamp;
import java.util.Date;

public class RegisterPatientInfoRequest {
    private String emergency_nurse_id;
    private String name;
    private String gender;
    private int age;
    private String disease_level;
    private String test_result;
    private String date;

    public String getEmergency_nurse_id() {
        return emergency_nurse_id;
    }

    public void setEmergency_nurse_id(String emergency_nurse_id) {
        this.emergency_nurse_id = emergency_nurse_id;
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
}
