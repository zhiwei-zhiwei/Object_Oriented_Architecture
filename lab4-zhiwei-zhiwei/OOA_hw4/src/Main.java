import java.io.File;
public class Main {
    public static void main(String[] args) {

        File file = new File("/Users/zhiweicao/Documents/A-Zhiwei/UChicago/Spring2024/OOA/Labs/lab4-zhiwei-zhiwei/OOA_hw4/data/LAB4_ConstantCurrent.csv");
        System.out.println("Absolute path: " + file.getAbsolutePath());
        System.out.println("Exists: " + file.exists());

        UserInterface ui = new UserInterface();
        ui.start();
    }
}