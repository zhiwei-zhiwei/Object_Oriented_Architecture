class Portfolio extends Component {
    private Node head;

    public Portfolio(String name) {
        super(name);
    }

    public void add(Component c) {
        Node newNode = new Node(c);
        newNode.next = head;
        head = newNode;
    }

    public void remove(Component c) {
        if (head == null) return;
        if (head.data == c) {
            head = head.next;
            return;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.data == c) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    public void print() {
        Node current = head;
        while (current != null) {
            current.data.print();
            current = current.next;
        }
    }

    @Override
    public double calculateMacaulayDuration() {
        double sum = 0;
        Node current = head;
        while (current != null) {
            sum += current.data.calculateMacaulayDuration();
            current = current.next;
        }
        return sum;
    }

    @Override
    public double calculateModifiedDuration() {
        double sum = 0;
        Node current = head;
        while (current != null) {
            sum += current.data.calculateModifiedDuration();
            current = current.next;
        }
        return sum;
    }

    public Node getHead() {
        return head;
    }
}