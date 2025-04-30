package com.grocery.assist.service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private AuditService() {
        try {
            File file = new File("audit.csv");
            if (file.createNewFile() || file.length() == 0) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
                    pw.println("nume_acțiune,timestamp");
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing audit file: " + e.getMessage());
        }
    }
    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String actName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("audit.csv", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println(actName + "," + timestamp);
        } catch (IOException e) {
            System.out.println("Error writing to audit file: " + e.getMessage());
        }

    }

}
