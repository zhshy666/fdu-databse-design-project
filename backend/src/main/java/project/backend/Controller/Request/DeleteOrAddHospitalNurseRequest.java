package project.backend.Controller.Request;

public class DeleteOrAddHospitalNurseRequest {
    private String chief_nurse_id;
    private String nurse_id;

    public String getChief_nurse_id() {
        return chief_nurse_id;
    }

    public void setChief_nurse_id(String chief_nurse_id) {
        this.chief_nurse_id = chief_nurse_id;
    }

    public String getNurse_id() {
        return nurse_id;
    }

    public void setNurse_id(String nurse_id) {
        this.nurse_id = nurse_id;
    }
}
