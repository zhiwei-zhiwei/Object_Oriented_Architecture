public class ReplyMsg extends Message {
    public ReplyMsg(String id, String body) {
        super(id, body, MessageType.REPLY);
    }
}
