package Imp;

public class StdDevTradingEngine implements TradingEngine{
    @Override
    public void onDataUpdate(String data) {
        System.out.println("StdDevTradingEngine: " + data);
    }
}
