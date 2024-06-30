abstract class Component {
    protected String name;

    // Constructors
    public Component(String name) {
        this.name = name;
    }

    abstract public void print();

    abstract public double calculateMacaulayDuration();

    abstract public double calculateModifiedDuration();

}