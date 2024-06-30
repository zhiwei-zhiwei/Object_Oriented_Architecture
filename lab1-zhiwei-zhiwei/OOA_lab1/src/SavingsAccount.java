import java.util.Calendar;

public class SavingsAccount extends Account {


    public SavingsAccount(double balance) {
        this.balance = balance;
        this.rate = 0.09;
    }

    @Override
    protected void CalcInterest() {
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.MONTH) == Calendar.JANUARY && today.get(Calendar.DAY_OF_MONTH) == 1) {
            double interest = balance * rate;
            balance += interest;
            System.out.println("SavingsAccount CalcInterest(): " + interest);
        }else {
            System.out.println("SavingsAccount CalcInterest(): No interest added.");
        }
    }
}
