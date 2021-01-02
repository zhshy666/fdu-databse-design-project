package project.backend.Utils;

import project.backend.Entity.Staff;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            case "DOCTOR":
                return Util.connectSQL(Config.DB_URL, Config.DOCTOR, Config.PASSWORD_D);
            case "CHIEF_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.CHIEF_NURSE, Config.PASSWORD_C);
            case "EMERGENCY_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.EMERGENCY_NURSE, Config.PASSWORD_E);
            case "HOSPITAL_NURSE":
                return Util.connectSQL(Config.DB_URL, Config.HOSPITAL_NURSE, Config.PASSWORD_H);
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
//                System.out.println(name);
                name = name.substring(3, 4).toLowerCase() + name.substring(4); // 获取属性名
//                System.out.println(name);
                if (map.containsKey(name)) {
                    try {
//                        System.out.println(map);
                        method.invoke(object, map.get(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
