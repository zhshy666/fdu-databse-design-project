package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.ChiefNurse;
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
    public List<String> findLevelByDoctorId(String id, String type) {
        Connection conn = Util.connect(type);
        assert conn != null;
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

    public List<String> findChiefNurseIdsByDoctorId(String type, String id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.treatment_region where doctor_id = ?";
        ResultSet rs;
        List<String> chiefNurses = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                chiefNurses.add(rs.getString("nurse_id"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return chiefNurses;
    }

    public TreatmentRegion findByLevel(String type, String level) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.treatment_region where level = ?";
        ResultSet rs;
        TreatmentRegion treatmentRegion = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                treatmentRegion = new TreatmentRegion();
                Util.toObject(rs, treatmentRegion);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return treatmentRegion;
    }

    public List<String> findLevelByChiefNurseId(String id, String type) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.treatment_region where nurse_id = ?";
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
