package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Bed;
import project.backend.Entity.HospitalNurse;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class BedRepo {
    public void updateBedToFreeByPatientId(String type, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.bed set patient_id = null where patient_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public Bed findBedByRegionAndPatientId(String type, String level, Integer patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.bed where treatment_region_level = ? and patient_id = ?";
        ResultSet rs;
        Bed bed = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            preparedStatement.setInt(2, patientId);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                bed = new Bed();
                Util.toObject(rs, bed);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return bed;
    }

    public void updateBedByBedIdAndPatientId(String type, int bedId, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.bed set patient_id = ? where bed_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, bedId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }
}
