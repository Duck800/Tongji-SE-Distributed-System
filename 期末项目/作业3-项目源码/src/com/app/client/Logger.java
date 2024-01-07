package com.app.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE_PATH = "client.log";
    private static final String LOG_FORMAT = "%s [%s] %s: %s\n";

    private static final int MAX_LOG_FILE_NUM = 10;
    private static final long MAX_LOG_FILE_SIZE = 1024L * 1024L; // 1MB

    private static volatile Logger instance;

    private String logFilePath;
    private BufferedWriter logWriter;
    private SimpleDateFormat dateFormat;

    Logger() {
        logFilePath = LOG_FILE_PATH;

        File logFile = new File(logFilePath);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true)));
        } catch (IOException e) {
            System.err.println("Failed to create log file " + logFilePath);
            e.printStackTrace();
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public synchronized void log(LogLevel level, String message) {
        Date now = new Date();
        String logLine = String.format(LOG_FORMAT, dateFormat.format(now), Thread.currentThread().getName(), level, message);

        try {
            logWriter.write(logLine);
            logWriter.flush();

            if (((CharSequence) logWriter).length() >= MAX_LOG_FILE_SIZE) {
                logWriter.close();
                rotateLogFile();
            }
        } catch (IOException e) {
            System.err.println("Failed to write log to file " + logFilePath);
            e.printStackTrace();
        }
    }

    private void rotateLogFile() {
        SimpleDateFormat rotateDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String rotateLogFilePath = String.format("%s.%s", logFilePath, rotateDateFormat.format(new Date()));

        File currentLogFile = new File(logFilePath);
        currentLogFile.renameTo(new File(rotateLogFilePath));

        try {
            logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentLogFile, true)));

            int logFileNum = 1;
            File[] logFiles = new File(".").listFiles((dir, name) -> name.matches("^" + logFilePath + ".\\d+$"));
            if (logFiles != null && logFiles.length > 0) {
                logFileNum = logFiles.length + 1;
                for (File logFile : logFiles) {
                    if (logFileNum > MAX_LOG_FILE_NUM) {
                        logFile.delete();
                    } else {
                        logFile.renameTo(new File(String.format("%s.%d", logFilePath, logFileNum)));
                        logFileNum++;
                    }
                }
            }

            if (logFileNum <= MAX_LOG_FILE_NUM) {
                File newLogFile = new File(String.format("%s.%d", logFilePath, logFileNum));
                newLogFile.createNewFile();
                logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newLogFile, true)));
            }
        } catch (IOException e) {
            System.err.println("Failed to rotate log file " + logFilePath);
            e.printStackTrace();
        }
    }

    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    public void writeInfo(String string) {
    }
}