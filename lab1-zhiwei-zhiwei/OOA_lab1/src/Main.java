public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();

        SavingsAccount savingsAccount = new SavingsAccount(1000);
        CheckingAccount checkingAccount = new CheckingAccount(2000);
        bank.addAccount(savingsAccount);
        bank.addAccount(checkingAccount);

        System.out.println("----------------");
        savingsAccount.GetAccountSummary();
        System.out.println("----------------");
        checkingAccount.GetAccountSummary();
    }
}