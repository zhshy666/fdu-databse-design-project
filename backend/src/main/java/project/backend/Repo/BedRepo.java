package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}
