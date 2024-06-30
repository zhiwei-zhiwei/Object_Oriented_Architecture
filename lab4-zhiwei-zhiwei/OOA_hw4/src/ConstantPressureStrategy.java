public class ConstantPressureStrategy implements MachineControlStrategy
{
    @Override
    public void execute(Hardware hardware, int partSize) {
        int psi = partSize + 100;
        for (int t = 0; t <= 10; t++) {
            int amps = t * 2;
            hardware.applyControlValues(psi, amps, t);
        }
    }
}
