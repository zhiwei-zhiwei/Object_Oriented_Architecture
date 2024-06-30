import java.io.*;
import java.util.*;

public class MachineControl {
    private Hardware hardware;
    private Map<String, MachineControlStrategy> strategies;

    public MachineControl() {
        this.hardware = new Hardware();
        this.strategies = new HashMap<>();
        strategies.put("ConstantPressure", new ConstantPressureStrategy());
        strategies.put("ConstantCurrent", new ConstantCurrentStrategy());
        strategies.put("Ramp", new RampStrategy());
    }

    public int[] getControlValues() {
        return hardware.getCurrentValues();
    }

    public void setControlValues(int psi, int amps) {
        hardware.setControlValues(psi, amps);
    }

    public void startMachine(int timeInSeconds) {
        hardware.run(timeInSeconds);
    }

    public boolean executeRecipe(String recipeFilePath) {
        try {
            File recipeFile = new File(recipeFilePath);
            Scanner scanner = new Scanner(recipeFile);
            String line = scanner.nextLine();
            scanner.close();
            String[] parts = line.split(",");
            String mode = parts[1];
            int partSize = Integer.parseInt(parts[2]);
            MachineControlStrategy strategy = strategies.get(mode);
            if (strategy == null) {
                throw new IllegalArgumentException("Invalid machine mode: " + mode);
            }
            strategy.execute(hardware, partSize);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.err.println("Error: Recipe file not found.");
            return false;
        }
    }



    private boolean validateOutput(String dasFilePath, String referenceFilePath) throws IOException {
        try (BufferedReader dasReader = new BufferedReader(new FileReader(dasFilePath));
             BufferedReader referenceReader = new BufferedReader(new FileReader(referenceFilePath))) {

            String dasLine, refLine;
            while ((dasLine = dasReader.readLine()) != null && (refLine = referenceReader.readLine()) != null) {
                if (!compareLines(dasLine, refLine)) {
                    return false;
                }
            }
            if (dasReader.readLine() != null || referenceReader.readLine() != null) {
                return false;
            }
        }
        return true;
    }

    private boolean compareLines(String dasLine, String refLine) {
        String[] dasParts = dasLine.split(",");
        String[] refParts = refLine.split(",");
        if (dasParts.length != refParts.length) {
            System.out.println("Line length mismatch: " + dasLine + " vs " + refLine);
            return false;
        }
        for (int i = 0; i < dasParts.length; i++) {
            if (!dasParts[i].trim().equals(refParts[i].trim())) {
                System.out.println("Mismatch found: " + dasParts[i] + " vs " + refParts[i]);
                return false;
            }
        }
        return true;
    }



}
