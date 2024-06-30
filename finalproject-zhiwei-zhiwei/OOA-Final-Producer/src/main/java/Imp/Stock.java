package Imp;

public class Stock implements PortfolioComponent {
    private String ticker;
    private double bidPrice;
    private double bidQuantity;
    private double askPrice;
    private double askQuantity;

    public Stock(String ticker, double bidPrice, double bidQuantity, double askPrice, double askQuantity) {
        this.ticker = ticker;
        this.bidPrice = bidPrice;
        this.bidQuantity = bidQuantity;
        this.askPrice = askPrice;
        this.askQuantity = askQuantity;
    }

    @Override
    public void add(PortfolioComponent component) {
        throw new UnsupportedOperationException("Cannot add to a stock.");
    }

    @Override
    public void remove(PortfolioComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a stock.");
    }

    @Override
    public PortfolioComponent getChild(int index) {
        throw new UnsupportedOperationException("No children for a stock.");
    }

    @Override
    public double getTotalBidPrice() {
        return bidPrice * bidQuantity;
    }

    @Override
    public double getTotalBidQuantity() {
        return bidQuantity;
    }

    @Override
    public double getTotalAskPrice() {
        return askPrice * askQuantity;
    }

    @Override
    public double getTotalAskQuantity() {
        return askQuantity;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", bidPrice=" + bidPrice +
                ", bidQuantity=" + bidQuantity +
                ", askPrice=" + askPrice +
                ", askQuantity=" + askQuantity +
                '}';
    }
}
