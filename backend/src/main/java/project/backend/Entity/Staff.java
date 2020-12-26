package project.backend.Entity;

public class Staff {
    private int id;
    private String type;
    private String name;
    private String password;
    private String age;
    private String gender;
    private Integer current_resp_num;

    public Staff(){}

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCurrent_resp_num() {
        return current_resp_num;
    }

    public void setCurrent_resp_num(Integer current_resp_num) {
        this.current_resp_num = current_resp_num;
    }
}
