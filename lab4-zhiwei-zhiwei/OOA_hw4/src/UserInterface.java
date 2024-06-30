import java.util.Scanner;

public class UserInterface {
    private MachineControl machineControl;
    private Scanner scanner;

    public UserInterface() {
        this.machineControl = new MachineControl();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to PhFMM Control Panel!");
            System.out.println("1. Set Control Values");
            System.out.println("2. Get Current Control Values");
            System.out.println("3. Manually Start Machine");
            System.out.println("4. Execute Recipe");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    setControlValues();
                    break;
                case 2:
                    getCurrentControlValues();
                    break;
                case 3:
                    manuallyStartMachine();
                    break;
                case 4:
                    executeRecipe();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void setControlValues() {
        System.out.print("Enter Air Pressure (psi): ");
        int psi = scanner.nextInt();
        System.out.print("Enter Current (amps): ");
        int amps = scanner.nextInt();
        machineControl.setControlValues(psi, amps);
    }

    private void getCurrentControlValues() {
        int[] values = machineControl.getControlValues();
        System.out.println("Current Air Pressure: " + values[0] + " psi");
        System.out.println("Current Electrical Current: " + values[1] + " amps");
    }

    private void manuallyStartMachine() {
        System.out.print("Enter time in seconds to run: ");
        int time = scanner.nextInt();
        machineControl.startMachine(time);
    }

    private void executeRecipe() {
        System.out.print("Enter the path of the recipe file: ");
        String path = scanner.next();
        boolean result = machineControl.executeRecipe(path);
        if (result) {
            System.out.println("Recipe executed successfully, part is good.");
        } else {
            System.out.println("Recipe execution failed, part is bad.");
        }
    }

}
