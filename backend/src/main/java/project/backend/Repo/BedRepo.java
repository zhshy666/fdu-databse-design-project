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

    public int findFreeBedByRegion(String type, String level) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.bed where treatment_region_level = ? and patient_id is null";
        ResultSet rs;
        int id = -1;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                id = rs.getInt("bed_id");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return id;
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
