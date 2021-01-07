package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Checklist;
import project.backend.Entity.PatientStatus;
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
        Date lastDate = null;
        List<Date> dates = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patient_id);
            rs = preparedStatement.executeQuery();
            int i = 0;
            while (rs.next()){
                if (i == 0){
                    // 获取最新的日期，减三天作为一个阈值
                    Date latestDate = new Date(rs.getTimestamp("date").getTime());
                    lastDate = new Date(latestDate.getTime() - 3 * 24 * 60 * 60 * 1000);
                }
                else {
                    Date date = new Date(rs.getTimestamp("date").getTime());
                    if (date.compareTo(lastDate) < 0){
                        break;
                    }
                }
                dates.add(new Date(rs.getTimestamp("date").getTime()));
                list.add(rs.getDouble("temperature"));
                i++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        if (lastDate == null){
            return new LinkedList<>();
        }
        int[] flagList = new int[3];
        Date d1 = new Date(lastDate.getTime() + 24 * 60 * 60 * 1000);
        Date d2 = new Date(lastDate.getTime() + 2 * 24 * 60 * 60 * 1000);
        for (Date date: dates){
            if (date.compareTo(d1) <= 0) flagList[0] = 1;
            else if (date.compareTo(d2) <= 0) flagList[1] = 1;
            else flagList[2] = 1;
        }
        boolean flag = true;
        for (int i = 0; i < 3; i++){
            if (flagList[i] == 0){
                flag = false;
                break;
            }
        }
        if (!flag){
            return new LinkedList<>();
        }
        return list;
    }

    public List<PatientStatus> findByPatientId(String type, int patientId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        List<PatientStatus> list = new LinkedList<>();
        String sql = "select * from database_project.patient_status where patient_id = ? order by date desc";
        ResultSet rs;
        PatientStatus patientStatus;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                patientStatus = new PatientStatus();
                Util.toObject(rs, patientStatus);
                list.add(patientStatus);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return list;
    }
}
