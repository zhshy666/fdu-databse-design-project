package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Doctor;
import project.backend.Entity.Patient;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class PatientRepo {
    // 如果是 1 或者 2 类型的就直接查，其他的需要设置一下筛选条件
    public void findPatientsByTreatmentRegionLevelAndCondition(String level, List<Patient> list, String type, String condition) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql;
        String[] map = null;
        boolean flag = condition.equals("0") || condition.equals("1") || condition.equals("2");
        if (flag){
            sql = "select * from database_project.patient where treatment_region_level = ?";
        }
        else {
            map = condition.split("=");
            sql = "select * from database_project.patient where treatment_region_level = ? and ? = ?";
        }
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            if (!flag) {
                preparedStatement.setString(2, map[0]);
                preparedStatement.setString(3, map[1]);
            }
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

    public Patient findPatientById(String type, Integer id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.patient where patient_id = ?";
        ResultSet rs;
        Patient patient = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                patient = new Patient();
                Util.toObject(rs, patient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return patient;
    }

    public void findPatientsNeedTransfer(String type, List<Patient> list, String level) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.patient where treatment_region_level = ? and disease_level != ?";

        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            preparedStatement.setString(2, level);
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
}
