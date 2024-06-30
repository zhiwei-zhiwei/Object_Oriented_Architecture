public abstract class Message {
    protected String id;
    protected String body;
    protected MessageType type;

    public Message(String id, String body, MessageType type) {
        this.id = id;
        this.body = body;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public enum MessageType {
        QUERY, REPLY
    }
}
