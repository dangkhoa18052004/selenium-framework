package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try {
            // Đọc file .env nếu có (khi chạy local)
            FileInputStream fis = new FileInputStream(".env");
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Không tìm thấy file .env (Chắc là đang chạy trên CI/CD).");
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
