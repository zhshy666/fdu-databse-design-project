package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Checklist;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ChecklistRepo {
    public List<Checklist> findByPatientId(String type, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<Checklist> list = new LinkedList<>();
        String sql = "select * from database_project.checklist where patient_id = ? and test_result is not null order by date desc";
        ResultSet rs;
        Checklist checklist;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                checklist = new Checklist();
                Util.toObject(rs, checklist);
                list.add(checklist);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return list;
    }

    public String findResultByChecklistId(String type, int checklistId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select test_result from database_project.checklist where id = ?";
        ResultSet rs;
        String testResult = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, checklistId);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                testResult = rs.getString("test_result");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return testResult;
    }

    public void insertNewChecklist(String type, String doctorId, int patientId, Timestamp time) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "insert into database_project.checklist(doctor_id, patient_id, date) values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, doctorId);
            preparedStatement.setInt(2, patientId);
            preparedStatement.setTimestamp(3, time);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public void insertInitialChecklist(String type, Checklist checklist) {
        Connection conn = Util.connect(type);
        assert conn != null;
        Timestamp timestamp = new Timestamp(checklist.getDate().getTime());
        String sql = "insert into database_project.checklist(test_result, date, disease_level, patient_id) values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, checklist.getTest_result());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setString(3, checklist.getDisease_level());
            preparedStatement.setInt(4, checklist.getPatient_id());
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public Checklist findByChecklistId(String type, int checklistId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.checklist where id = ?";
        ResultSet rs;
        Checklist checklist = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, checklistId);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                checklist = new Checklist();
                Util.toObject(rs, checklist);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return checklist;
    }

    public void updateChecklist(String type, Checklist checklist) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.checklist set disease_level = ? where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, checklist.getDisease_level());
            preparedStatement.setInt(2, checklist.getId());
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sql = "update database_project.checklist set test_result = ? where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, checklist.getTest_result());
            preparedStatement.setInt(2, checklist.getId());
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sql = "update database_project.checklist set date = ? where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(checklist.getDate().getTime()));
            preparedStatement.setInt(2, checklist.getId());
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public int findChecklistNum(String type) {
        Connection conn = Util.connect(type);
        assert conn != null;
        int num = 0;
        String sql = "select count(*) from database_project.checklist";
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

    public Map<String, Integer> findEarliestChecklistIdAndPatientIdByHospitalNurseId(String type, String nurseId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        int id;
        int patientId;
        Map<String, Integer> map = null;
        String sql = "select id, patient_id from database_project.checklist where patient_id in (" +
                "select patient_id from database_project.patient where nurse_id = ?) " +
                "and test_result not in ('positive', 'negative') order by date";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nurseId);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                id = rs.getInt("id");
                patientId = rs.getInt("patient_id");
                map = new LinkedHashMap<>();
                map.put("checklist_id", id);
                map.put("patient_id", patientId);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return map;
    }
}
