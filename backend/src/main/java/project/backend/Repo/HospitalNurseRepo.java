package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.ChiefNurse;
import project.backend.Entity.HospitalNurse;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class HospitalNurseRepo {
    public HospitalNurse findUserByIdAndPassword(String id, String psw) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
        // query
        String sql = "select * from database_project.hospital_nurse where id = ? and password = ?";
        ResultSet rs;
        HospitalNurse hospitalNurse = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, psw);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                hospitalNurse = new HospitalNurse();
                Util.toObject(rs, hospitalNurse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.close(conn);
        return hospitalNurse;
    }
}