public class QueryMsg extends Message {
    public QueryMsg(String id, String body) {
        super(id, body, MessageType.QUERY);
    }
}
