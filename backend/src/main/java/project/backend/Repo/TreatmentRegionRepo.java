package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Doctor;
import project.backend.Entity.TreatmentRegion;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Repository
public class TreatmentRegionRepo {
    public List<String> findLevelByDoctorId(String id) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
        // query
        String sql = "select * from database_project.treatment_region where doctor_id = ?";
        ResultSet rs;
        List<String> levels = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                levels.add(rs.getString("level"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return levels;
    }
}
