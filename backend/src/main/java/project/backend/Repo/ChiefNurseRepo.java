package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.ChiefNurse;
import project.backend.Entity.Doctor;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class ChiefNurseRepo {
    public ChiefNurse findUserByIdAndPassword(String id, String psw) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
        // query
        String sql = "select * from database_project.chief_nurse where id = ? and password = ?";
        ResultSet rs;
        ChiefNurse chiefNurse = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, psw);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                chiefNurse = new ChiefNurse();
                Util.toObject(rs, chiefNurse);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return chiefNurse;
    }

    public void updateNurseById(String id, String name, String password, int age) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.CHIEF_NURSE, Config.PASSWORD_C);
        // update
        String sql = "update database_project.chief_nurse set name = ?, password = ?, age = ? where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }
}
