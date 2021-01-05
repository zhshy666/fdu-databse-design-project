package project.backend.Entity;


public class Staff{

    private String id;
    private String type;
    private String name;
    private String gender;
    private int age;

    public Staff(String id, String type, String name, String gender, int age) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
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
}
