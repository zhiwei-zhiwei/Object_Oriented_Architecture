package Imp;
public interface PortfolioComponent {
    void add(PortfolioComponent component);
    void remove(PortfolioComponent component);
    PortfolioComponent getChild(int index);
    double getTotalBidPrice();
    double getTotalBidQuantity();
    double getTotalAskPrice();
    double getTotalAskQuantity();
}
