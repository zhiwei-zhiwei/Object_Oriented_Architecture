public class Account {
    private Portfolio rootPortfolio;
    final int F = 10000;

    public Account() {
        this.rootPortfolio = new Portfolio("Root Portfolio");
    }

    public void add(Component component) {
        rootPortfolio.add(component);
    }

    public void print() {
        int numBonds = 0;
        int numPortfolios = 0;
        Node current = rootPortfolio.getHead();
        while (current != null) {
            Component c = current.data;
            if (c instanceof Bond) numBonds++;
            if (c instanceof Portfolio) {
                numPortfolios++;
                Node portfolioCurrent = ((Portfolio) c).getHead();
                while (portfolioCurrent != null) {
                    if (portfolioCurrent.data instanceof Bond) numBonds++;
                    portfolioCurrent = portfolioCurrent.next;
                }
            }
            current = current.next;
        }

        double totalValue_MacD = 0;
        current = rootPortfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            totalValue_MacD += F;
            current = current.next;
        }

        double MacD = 0;
        current = rootPortfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            MacD += bond.calculateMacaulayDuration() * F / totalValue_MacD;
            current = current.next;
        }

        double totalValue_ModD = 0;
        current = rootPortfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            totalValue_ModD += F;
            current = current.next;
        }

        double ModD = 0;
        current = rootPortfolio.getHead();
        while (current != null) {
            Component bond = current.data;
            ModD += bond.calculateModifiedDuration() * F / totalValue_ModD;
            current = current.next;
        }

        System.out.println("I am an account and I have " + numPortfolios + " portfolios and " + numBonds + " individual bonds. Account MacD is " + MacD + " and ModD is " + ModD);

        current = rootPortfolio.getHead();
        while (current != null) {
            Component c = current.data;
            c.print();
            current = current.next;
        }

        System.out.println("I also have " + numPortfolios + " portfolios and the durations of the portfolios are as follows:");
        current = rootPortfolio.getHead();
        while (current != null) {
            Component c = current.data;
            if (c instanceof Portfolio) {
                PortfolioStrategy macaulayStrategy = new MacaulayDurationStrategy();
                PortfolioStrategy modifiedStrategy = new ModifiedDurationStrategy();
                System.out.println(c.name + " MacD is " + macaulayStrategy.calculateDuration((Portfolio) c) + " ModD is " + modifiedStrategy.calculateDuration((Portfolio) c));
            }
            current = current.next;
        }
    }
}