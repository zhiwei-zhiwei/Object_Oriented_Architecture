import java.util.Vector;

public class MessageQueue {
    private Vector<Message> queue;

    public MessageQueue() {
        this.queue = new Vector<>();
    }

    public synchronized void addMsg(Message msg) {
        queue.add(msg);
    }

    public synchronized Message popMsg() {
        if (!queue.isEmpty()) {
            return queue.removeFirst();
        }
        return null;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized Message peekMsg() {
        if (!queue.isEmpty()) {
            return queue.firstElement();
        }
        return null;
    }

}
