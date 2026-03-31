package utils;

public class EnvUtils {
    public static String getPassword() {
        // Ưu tiên đọc từ biến môi trường (khi chạy trên CI/CD)
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            // Fallback: đọc từ file config (khi chạy local)
            password = ConfigReader.getInstance().getProperty("APP_PASSWORD");
        }
        return password;
    }

    public static String getUsername() {
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            username = ConfigReader.getInstance().getProperty("APP_USERNAME");
        }
        return username;
    }
}
