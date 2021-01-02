package project.backend.Controller.Request;

public class LoginRequest {
    private String id;
    private String password;

    public LoginRequest() {
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
