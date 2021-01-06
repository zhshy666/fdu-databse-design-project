package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Checklist;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ChecklistRepo {
    public List<Checklist> findByPatientId(String type, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<Checklist> list = new LinkedList<>();
        String sql = "select * from database_project.checklist where patient_id = ? order by date desc";
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
        List<Checklist> list = new LinkedList<>();
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
}
