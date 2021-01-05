package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Doctor;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class DoctorRepo {
    public Doctor findUserByIdAndPassword(String id, String psw) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
        // query
        String sql = "select * from database_project.doctor where id = ? and password = ?";
        ResultSet rs;
        Doctor doctor = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, psw);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                doctor = new Doctor();
                Util.toObject(rs, doctor);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return doctor;
    }

    public void updateDoctorById(String id, String password, int age) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.DOCTOR, Config.PASSWORD_D);
        // update
        String sql = "update database_project.doctor set password = ?, age = ? where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }
}
