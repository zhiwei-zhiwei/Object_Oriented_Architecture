class Bond extends Component {
    private int size;
    private double F; // face value
    private double y; // yield
    private double k; // coupon frequency
    private double c; // coupon rate
    private int m; // number of coupons periods
    private double x; // numberof years

    public Bond(String name, double x, double k, double c, double y) {
        super(name);
        this.x = x;
        this.k = k;
        this.c = c;
        this.y = y;
        this.F = 10000;
        this.m = (int) (x * k);
    }

    @Override
    public void print() {
        System.out.println(this.name + " MacD is " + calculateMacaulayDuration() + " ModD is " + calculateModifiedDuration());
    }

    @Override
    public double calculateMacaulayDuration() {
        return (((1 + (y / k)) / (y / k)) - ((100 * (1 + (y / k)) + (m * ((c / k) - (100 * (y / k))))) / ((c / k) * (Math.pow((1 + (y / k)), m) - 1) + (100 * (y / k))))) / k;
    }

    @Override
    public double calculateModifiedDuration() {
        return calculateMacaulayDuration() / (1 + (y / k));
    }

    public double calculatePresentValue() {
        double C = (this.c / this.k) / 100;
        double F = this.F;
        double i = 0.025; // current prevailing interest rate
        double r = i / this.k;
        double t = this.m;
        return C * F * ((1 - Math.pow(1 + r, -t)) / r) + (F / Math.pow(1 + r, t));
    }


}