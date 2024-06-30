public class ModifiedDurationStrategy implements PortfolioStrategy {
    final int F = 10000;

    @Override
    public double calculateDuration(Portfolio portfolio) {

        double totalValue = 0.0;
        Node current = portfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            totalValue += F;
            current = current.next;
        }

        double weightedDurationSum = 0.0;
        current = portfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            double weight = F / totalValue;
            weightedDurationSum += bond.calculateModifiedDuration() * weight;
            current = current.next;
        }

        return weightedDurationSum;

    }
}