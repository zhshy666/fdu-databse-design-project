package project.backend.Entity;

public class HospitalNurse {
    private String id;
    private String name;
    private String password;
    private int age;
    private String gender;
    private int current_resp_num;

    public HospitalNurse(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCurrent_resp_num() {
        return current_resp_num;
    }

    public void setCurrent_resp_num(int current_resp_num) {
        this.current_resp_num = current_resp_num;
    }
}
