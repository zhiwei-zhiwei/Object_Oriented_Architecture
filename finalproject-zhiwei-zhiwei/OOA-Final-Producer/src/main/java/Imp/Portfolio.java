package Imp;

import java.util.ArrayList;
import java.util.List;

public class Portfolio implements PortfolioComponent {
    private List<PortfolioComponent> components = new ArrayList<>();
    private String name;

    public Portfolio(String name) {
        this.name = name;
    }

    @Override
    public void add(PortfolioComponent component) {
        components.add(component);
    }

    @Override
    public void remove(PortfolioComponent component) {
        components.remove(component);
    }

    public int size() {
        return this.components.size();
    }

    public String getName() {
        return name;
    }

    public List<PortfolioComponent> getComponents() {
        return components;
    }

    @Override
    public PortfolioComponent getChild(int index) {
        return components.get(index);
    }

    @Override
    public double getTotalBidPrice() {
        return components.stream().mapToDouble(PortfolioComponent::getTotalBidPrice).sum();
    }

    @Override
    public double getTotalBidQuantity() {
        return components.stream().mapToDouble(PortfolioComponent::getTotalBidQuantity).sum();
    }

    @Override
    public double getTotalAskPrice() {
        return components.stream().mapToDouble(PortfolioComponent::getTotalAskPrice).sum();
    }

    @Override
    public double getTotalAskQuantity() {
        return components.stream().mapToDouble(PortfolioComponent::getTotalAskQuantity).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Portfolio{" +
                "name='" + name + '\'' +
                ", components=\n");
        for (PortfolioComponent component : components) {
            sb.append(component.toString()).append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
