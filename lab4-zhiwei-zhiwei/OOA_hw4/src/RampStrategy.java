public class RampStrategy implements MachineControlStrategy {
    public void execute(Hardware hardware, int partSize) {
        if (partSize < 50) {
            throw new IllegalArgumentException("Part size must be 50 or above for Ramp mode.");
        }
        int psi = 0;
        int amps = partSize;
        for (int t = 0; t <= 30; t++) {
            psi = Math.min(psi + 10, 100);
            amps = amps + 20;
            hardware.applyControlValues(psi, amps, t);
        }
    }
}
