public class Responder {
    private MessageQueue queue;

    public Responder(MessageQueue queue) {
        this.queue = queue;
    }

    public void respondToMessages() {
        while (!queue.isEmpty()) {
            Message msg = queue.peekMsg();
            if (msg instanceof QueryMsg) {
                System.out.println("QueryMsg ID: " + msg.getId() + ", Body: " + msg.getBody());
                queue.popMsg();
                String translation = translateWord(msg.getBody());
                ReplyMsg reply = new ReplyMsg(msg.getId(), translation);
                queue.addMsg(reply);
                System.out.println("ReplyMsg ID: " + reply.getId() + ", Body: " + reply.getBody());
            } else {
                queue.popMsg();
            }
        }
    }


    private String translateWord(String word) {
        try {
            Integer.parseInt(word);
            return word;
        } catch (NumberFormatException e) {
            return switch (word.toLowerCase()) {
                case "one" -> "1";
                case "two" -> "2";
                case "three" -> "3";
                case "four" -> "4";
                case "five" -> "5";
                case "six" -> "6";
                case "seven" -> "7";
                case "eight" -> "8";
                case "nine" -> "9";
                case "ten" -> "10";
                default -> "Undefined";
            };
        }
    }
}
