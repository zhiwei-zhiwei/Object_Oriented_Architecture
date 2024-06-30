package Imp;

public class AverageTradingEngine implements TradingEngine{
    @Override
    public void onDataUpdate(String data) {
        System.out.println("AverageTradingEngine: " + data);
    }
}
