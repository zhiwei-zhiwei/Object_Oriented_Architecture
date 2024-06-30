public class ConstantCurrentStrategy implements MachineControlStrategy {
    @Override
    public void execute(Hardware hardware, int partSize) {
        int amps = partSize + 50;
        for (int t = 0; t <= 20; t++) {
            int psi = Math.max(50 - (t * 2), 10);
            hardware.applyControlValues(psi, amps, t);
        }
    }
}
