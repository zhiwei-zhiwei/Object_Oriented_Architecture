import java.util.ArrayList;
import java.util.List;

public class Bank {
    private static Bank instance = null;
    private List<Account> accountList = new ArrayList<>();
    public Bank() {
    }

    public static Bank getInstance() {
        if (instance == null) {
            synchronized (Bank.class){
                if(instance == null){
                    instance = new Bank();
                }
            }
        }
        return instance;
    }

    public void addAccount(Account account){
        accountList.add(account);
    }

}
