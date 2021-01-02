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

            System.out.println("Connect to db successfully");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection connection) {
        try{
            connection.close();
            System.out.println("Disconnect successfully");
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
//                System.out.println(field.getName());
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
                        method.invoke(object, map.get(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
