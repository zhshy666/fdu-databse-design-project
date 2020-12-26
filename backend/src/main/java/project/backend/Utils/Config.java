package project.backend.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    // 最初始的配置，登录时查表连数据库需要 root 登录
    public static String SQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String ROOT = "root";
    public static String PASSWORD = "zsy666";
    public static String DB_URL = "jdbc:mysql://localhost/database_project";
}
