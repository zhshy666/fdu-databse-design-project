package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Patient;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class PatientRepo {

    public void findPatientsByTreatmentRegionLevel(Integer level, List<Patient> list, String type) {
        Connection conn = connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.patient where treatment_region_level = ?";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, level);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                Patient patient = new Patient();
                Util.toObject(rs, patient);
                list.add(patient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    private Connection connect(String type) {
        switch (type){
            case "DOCTOR":
                return Util.connectSQL(Config.DB_URL, Config.DOCTOR, Config.PASSWORD_D);
            case "CHIEF_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.CHIEF_NURSE, Config.PASSWORD_C);
            case "EMERGENCY_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.EMERGENCY_NURSE, Config.PASSWORD_E);
            case "HOSPITAL_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.HOSPITAL_NURSE, Config.PASSWORD_H);
            default:
                System.out.println("Error");
        }
        return null;
    }
}
