package Imp;

public class ReportEngine {
    private static ReportEngine instance;

    private ReportEngine() {
    }

    public static ReportEngine getInstance() {
        if (instance == null) {
            synchronized (ReportEngine.class) {
                if (instance == null) {
                    instance = new ReportEngine();
                }
            }
        }
        return instance;
    }

    public void reportStatistics(String statistics) {
        System.out.println("Reporting statistics: " + statistics);
    }
}

