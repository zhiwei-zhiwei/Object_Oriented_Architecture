public abstract class Account {
    protected double balance;
    protected double rate;
    public void GetAccountSummary(){
        CalcInterest();
        UpdateBalance();
        PrintSummary();
    }

    protected abstract void CalcInterest();
    protected void UpdateBalance() {
        System.out.println("Account UpdateBalance()");
    }
    protected void PrintSummary() {
    System.out.println("Account balance: " + balance);
}




}
