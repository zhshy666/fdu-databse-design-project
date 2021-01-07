package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Doctor;
import project.backend.Entity.Patient;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Repository
public class PatientRepo {
    public void findPatientsByTreatmentRegionLevel(String level, List<Patient> list, String type) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql;
        sql = "select * from database_project.patient where treatment_region_level = ?";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
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

    public void findPatientsNeedTransfer(String type, List<Integer> list, String level) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.patient where disease_level != ? and treatment_region_level = ?";

        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            preparedStatement.setString(2, level);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                Patient patient = new Patient();
                Util.toObject(rs, patient);
                list.add(patient.getPatient_id());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public void updatePatientLifeStatusById(String type, int patientId, String newStatus) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.patient set life_status = ? where patient_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, patientId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public void updateTreatmentRegionLevelAndNurseIdById(String type, String level, String nurseId, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.patient set treatment_region_level = ? where patient_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            preparedStatement.setInt(2, patientId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sql = "update database_project.patient set nurse_id = ? where patient_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nurseId);
            preparedStatement.setInt(2, patientId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public List<Patient> findPatientByDiseaseLevel(String type, String diseaseLevel) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.patient where disease_level = ?";
        ResultSet rs;
        List<Patient> patients = new LinkedList<>();
        Patient patient;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, diseaseLevel);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                patient = new Patient();
                Util.toObject(rs, patient);
                patients.add(patient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return patients;
    }

    public void updatePatientDiseaseLevelById(String type, int patientId, String newDiseaseLevel) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.patient set disease_level = ? where patient_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newDiseaseLevel);
            preparedStatement.setInt(2, patientId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public int insertBasicInfo(String type, Patient patient) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "insert into database_project.patient(name, gender, age, disease_level)" +
                "values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getGender());
            preparedStatement.setInt(3, patient.getAge());
            preparedStatement.setString(4, patient.getDisease_level());
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return getPatientNumber(type);
    }

    public List<Patient> findPatients(String type) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.patient where treatment_region_level is not null";
        ResultSet rs;
        List<Patient> patients = new LinkedList<>();
        Patient patient;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                patient = new Patient();
                Util.toObject(rs, patient);
                patients.add(patient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return patients;
    }

    private int getPatientNumber(String type){
        Connection conn = Util.connect(type);
        assert conn != null;
        int num = 0;
        String sql = "select count(*) from database_project.patient";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                num = rs.getInt(1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return num;
    }


    public List<Patient> findPatientsByNurseId(String type, String hospitalNurseId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.patient where nurse_id = ?";
        ResultSet rs;
        List<Patient> patients = new LinkedList<>();
        Patient patient;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, hospitalNurseId);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                patient = new Patient();
                Util.toObject(rs, patient);
                patients.add(patient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return patients;
    }
}
