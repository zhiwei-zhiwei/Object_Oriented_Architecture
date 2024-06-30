import java.util.Calendar;

public class CheckingAccount extends Account {

    public CheckingAccount(double balance) {
        this.balance = balance;
        this.rate = 0.00;
    }

    @Override
    protected void CalcInterest() {
        double interest = balance * rate;
        balance += interest;
        System.out.println("CheckingAccount CalcInterest(): Checking account does not have interest.");
    }
}
