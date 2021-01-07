package project.backend.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.backend.Entity.Staff;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Connection connectSQL(String url, String username, String password) {
        Connection conn = null;
        try {
            // jdbc driver
            Class.forName(Config.SQL_DRIVER);
            // connect
            conn = DriverManager.getConnection(url, username, password);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection connect(String type) {
        switch (type){
            case "doctor":
                return Util.connectSQL(Config.DB_URL, Config.DOCTOR, Config.PASSWORD_D);
            case "chief_nurse":
                return Util.connectSQL(Config.DB_URL, Config.CHIEF_NURSE, Config.PASSWORD_C);
            case "emergency_nurse":
                return Util.connectSQL(Config.DB_URL, Config.EMERGENCY_NURSE, Config.PASSWORD_E);
            case "hospital_nurse":
                return Util.connectSQL(Config.DB_URL, Config.HOSPITAL_NURSE, Config.PASSWORD_H);
            case "root":
                return Util.connectSQL(Config.DB_URL, Config.ROOT, Config.PASSWORD);
            default:
                System.out.println("Error");
        }
        return null;
    }

    public static void close(Connection connection) {
        try{
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void toObject(ResultSet rs, Object object) {
        Class<?> c = object.getClass();

        Map<String, Object> map = new HashMap<>();

        Field[] fields = c.getDeclaredFields(); // 获取属性数组

        for (Field field : fields) {
            try {
                map.put(field.getName(), rs.getObject(field.getName()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Method[] methods = c.getMethods(); // 获取对象方法
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String name = method.getName();
                name = name.substring(3, 4).toLowerCase() + name.substring(4); // 获取属性名
                if (map.containsKey(name)) {
                    try {
                        method.invoke(object, map.get(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Timestamp transferDateFormat(String dateTime) {
        dateTime = dateTime.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd'T'HH:mm:ss.SSS Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp newTime = null;
        try {
            Date time = format.parse(dateTime);
            String result = defaultFormat.format(time);
            newTime = Timestamp.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTime;
    }
}
