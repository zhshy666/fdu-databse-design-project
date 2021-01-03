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
    public List<Checklist> findByPatientId(String type, int patient_id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<Checklist> list = new LinkedList<>();
        String sql = "select * from database_project.checklist where patient_id = ? order by date desc";
        ResultSet rs;
        Checklist checklist;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patient_id);
            rs = preparedStatement.executeQuery();
            int i = 0;
            while (rs.next() && i < 2){
                checklist = new Checklist();
                Util.toObject(rs, checklist);
                list.add(checklist);
                i++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return list;
    }
}
