package project.backend.Utils;

public class Config {
    //最初始的配置，登录时查表连数据库需要 root 登录     
    public static String SQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String ROOT = "root";
    public static String PASSWORD = "zsy666";
//    public static String PASSWORD = "admin";
    public static String DB_URL = "jdbc:mysql://localhost/database_project?serverTimezone=GMT%2B8";
//    public static String DB_URL = "jdbc:mysql://localhost/database_project?serverTimezone=UTC";

    // 4 种身份
    public static String DOCTOR = "doctor";
    public static String PASSWORD_D = "123456";

    public static String CHIEF_NURSE = "chief_nurse";
    public static String PASSWORD_C = "123456";

    public static String EMERGENCY_NURSE = "emergency_nurse";
    public static String PASSWORD_E = "123456";

    public static String HOSPITAL_NURSE = "hospital_nurse";
    public static String PASSWORD_H = "123456";
}
