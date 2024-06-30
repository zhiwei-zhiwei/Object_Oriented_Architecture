public class Main {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue();

        queue.addMsg(new QueryMsg("100", "one"));
        queue.addMsg(new QueryMsg("101", "two"));
        queue.addMsg(new QueryMsg("102", "three"));
        queue.addMsg(new QueryMsg("103", "four"));
        queue.addMsg(new QueryMsg("104", "five"));
        queue.addMsg(new QueryMsg("105", "six"));
        Responder responder = new Responder(queue);
        System.out.println("QueryMsg, ReplyMsg: ");
        responder.respondToMessages();
    }
}