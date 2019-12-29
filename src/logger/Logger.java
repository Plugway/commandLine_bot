import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static FileWriter fileWriter;
    private static boolean loggerIsON = false;
    private static boolean isLoggerIsInitialized = false;
    private static DateFormat fullDateFormat;
    private static DateFormat timeFormat;
    private static void initializeLogger()
    {
        fullDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            fileWriter = new FileWriter(FilePaths.LogPath, true);
            loggerIsON = true;
        } catch (IOException e) {
            System.out.println("Can't open log file. Logging to file is turned off.");
        }
        isLoggerIsInitialized = true;
    }
    public static void flushFW() {
        try
        {
            if (loggerIsON)
                fileWriter.flush();
        } catch (IOException ignored){}//хз, фикс
    }
    private static String getLogLevel(LogLevels level)
    {
        switch (level)
        {
            case info:
                return "INFO";
            case warn:
                return "WARN";
            case debug:
                return "DEBUG";
            case error:
                return "ERROR";
            case fatal:
                return "FATAL";
            default:
                return "ALL";
        }
    }
    public static void log(LogLevels logLevel, String message)
    {
        message = message.replaceAll("\n", "/n");
        var date = new Date();
        if (!isLoggerIsInitialized) {
            initializeLogger();
            if (loggerIsON)
                try {
                    fileWriter.write("\n[" + fullDateFormat.format(date) + "]LOGGING STARTED\n");
                } catch (IOException ignored){
                    System.out.println("[" + fullDateFormat.format(date) + "]Log failed.\n");
                }
            System.out.print("[" + fullDateFormat.format(date) + "]LOGGING STARTED\n");
        }
        var res = "["+timeFormat.format(date)+"](" + getLogLevel(logLevel) + ")|"+message+"|[" + fullDateFormat.format(date) + "]\n";
        System.out.print(res);
        if (loggerIsON)
        {
            try {
                fileWriter.write(res);
                if (logLevel == LogLevels.fatal)
                    fileWriter.flush();
            }catch (IOException ignored){}
        }
    }
}
