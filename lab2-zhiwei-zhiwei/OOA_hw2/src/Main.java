public class Main {
    public static void main(String[] args) {
        Account account = new Account();

        Bond bondk = new Bond("Bond 1", 10, 2, 20, 0.04);
        System.out.println("Present Value of bondk \"Test\": "+bondk.calculatePresentValue());

        Bond bond1 = new Bond("Bond 1", 10, 2, 20, 0.04);
        Bond bond2 = new Bond("Bond 2", 15, 2, 20, 0.04);
        Bond bond3 = new Bond("Bond 3", 15, 2, 20, 0.06);

        account.add(bond1);
        account.add(bond2);
        account.add(bond3);

        Portfolio portfolio1 = new Portfolio("Portfolio 1");
        portfolio1.add(new Bond("Bond 4", 20, 2, 20, 0.04));
        portfolio1.add(new Bond("Bond 5", 5, 3, 20, 0.05));
        portfolio1.add(new Bond("Bond 6", 12, 2, 18, 0.04));

        Portfolio portfolio2 = new Portfolio("Portfolio 2");
        portfolio2.add(new Bond("Bond 7", 11, 3, 23, 0.05));
        portfolio2.add(new Bond("Bond 8", 30, 2, 20, 0.04));
        portfolio2.add(new Bond("Bond 9", 14, 2, 18, 0.06));
        account.add(portfolio1);
        account.add(portfolio2);

        account.print();
    }
}