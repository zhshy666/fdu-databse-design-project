package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.ChiefNurse;
import project.backend.Entity.Doctor;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

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

    public void updateNurseById(String id, String password, int age) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.CHIEF_NURSE, Config.PASSWORD_C);
        // update
        String sql = "update database_project.chief_nurse set  password = ?, age = ? where id = ?";
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

    public List<ChiefNurse> findByIds(String type, List<String> ids) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.chief_nurse where id = ?";
        ResultSet rs;
        List<ChiefNurse> chiefNurses = new LinkedList<>();
        ChiefNurse chiefNurse;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            for (String id: ids) {
                preparedStatement.setString(1, id);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    chiefNurse = new ChiefNurse();
                    Util.toObject(rs, chiefNurse);
                    chiefNurses.add(chiefNurse);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return chiefNurses;
    }
}
