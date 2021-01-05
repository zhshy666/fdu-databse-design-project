package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Repository
public class PatientStatusRepo {
    // 拿到最近的三条记录
    public List<Double> findTemperaturesByPatientId(String type, int patient_id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<Double> list = new LinkedList<>();
        String sql = "select * from database_project.patient_status where patient_id = ? order by date desc";
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patient_id);
            rs = preparedStatement.executeQuery();
            int i = 0;
            Date lastDate = null;
            while (rs.next()){
                if (i == 0){
                    // 获取最新的日期，减三天作为一个阈值
                    Date latestDate = rs.getDate("date");
                    lastDate = new Date(latestDate.getTime() - 3 * 24 * 60 * 60 * 1000);
                }
                else {
                    Date date = rs.getDate("date");
                    if (date.compareTo(lastDate) < 0){
                        break;
                    }
                }
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
