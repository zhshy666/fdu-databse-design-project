package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Patient;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Repository
public class PatientStatusRepo {
    // 拿到最近的三条记录
    public List<Double> findTemperaturesByPatientId(String type, int patient_id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<Double> list = new LinkedList<>();
        String sql = "select temperature from database_project.patient_status where patient_id = ? order by date desc";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patient_id);
            rs = preparedStatement.executeQuery();
            int i = 0;
            while (rs.next() && i < 3){
                list.add(rs.getDouble("temperature"));
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
