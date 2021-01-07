package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.ChiefNurse;
import project.backend.Entity.HospitalNurse;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

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

    public void updateNurseById(String id, String password, int age) {
        Connection conn = Util.connectSQL(Config.DB_URL, Config.HOSPITAL_NURSE, Config.PASSWORD_H);
        // update
        String sql = "update database_project.hospital_nurse set password = ?, age = ? where id = ?";
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

    public void findHospitalNursesByRegion(String type, String level, List<HospitalNurse> hospitalNurses) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.hospital_nurse where treatment_region_level = ?";
        ResultSet rs;
        HospitalNurse hospitalNurse;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                hospitalNurse = new HospitalNurse();
                Util.toObject(rs, hospitalNurse);
                hospitalNurses.add(hospitalNurse);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public void updateRespPatientNum(String type, String nurseId, int flag) {
        int num = this.findCurrentRespNumByNurseId(type, nurseId) + flag;
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.hospital_nurse set current_resp_num = ? where id = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, num);
            preparedStatement.setString(2, nurseId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    private int findCurrentRespNumByNurseId(String type, String nurseId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select current_resp_num from database_project.hospital_nurse where id = ?";
        ResultSet rs;
        int num = -1;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nurseId);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                num = rs.getInt("current_resp_num");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.close(conn);
        return num;
    }

    public HospitalNurse findHospitalNurseByRegionAndRespNum(String type, String region, int num) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.hospital_nurse where treatment_region_level = ? and current_resp_num < ?";
        ResultSet rs;
        HospitalNurse hospitalNurse = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, region);
            preparedStatement.setInt(2, num);
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

    public void deleteHospitalNurseById(String type, String nurseId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "delete from database_project.hospital_nurse where id = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nurseId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public HospitalNurse findFreeHospitalNurseByNurseId(String type, String nurseId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        // query
        String sql = "select * from database_project.hospital_nurse where id = ? and treatment_region_level is null";
        ResultSet rs;
        HospitalNurse hospitalNurse = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nurseId);
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

    public void updateTreatmentRegionLevel(String type, String id, String level) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.hospital_nurse set treatment_region_level = ? where id = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, level);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

}