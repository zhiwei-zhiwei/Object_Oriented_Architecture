import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Hardware {
    private static final int MAX_PSI = 200;
    private static final int MIN_PSI = 0;
    private static final int MAX_AMPS = 200;
    private static final int MIN_AMPS = 0;

    private int currentPsi = 0;
    private int currentAmps = 0;

    public void setControlValues(int psi, int amps) {
        this.currentPsi = Math.min(MAX_PSI, Math.max(MIN_PSI, psi));
        this.currentAmps = Math.min(MAX_AMPS, Math.max(MIN_AMPS, amps));
    }

    public int[] getCurrentValues() {
        return new int[]{currentPsi, currentAmps};
    }

    public void run(int seconds) {
        System.out.println("Starting the hardware...");
        for (int i = 0; i < seconds; i++) {
            System.out.println("Running... Time: " + i + "s, PSI: " + currentPsi + ", AMPS: " + currentAmps);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Stopping the hardware...");
    }

    public void applyControlValues(int psi, int amps, int time) {
        setControlValues(psi, amps);
        System.out.println(time + "," + currentPsi + "," + currentAmps);
    }
}
