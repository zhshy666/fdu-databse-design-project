package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Staff;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Repository
public class StaffRepo {

    public Staff findUserByUsernameAndPassword(String name, String psw) {
//        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
//        // query
//        String sql = "select * from staff where name = ? and password = ?";
//        ResultSet rs;
//        Staff staff = null;
//        try {
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, psw);
//            rs = preparedStatement.executeQuery();
//            if (rs.next()){
//                staff = new Staff();
//                Util.toObject(rs, staff);
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        Util.close(conn);
//        return staff;
        return null;
    }

    public Staff findUserByUsername(String username) {
//        Connection conn = Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
//        // query
//        String sql = "select * from staff where name = ?";
//        ResultSet rs;
//        Staff staff = null;
//        try {
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, name);
//            rs = preparedStatement.executeQuery();
//            if (rs.next()){
//                staff = new Staff();
//                Util.toObject(rs, staff);
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        Util.close(conn);
//        return staff;
        return null;
    }
}
