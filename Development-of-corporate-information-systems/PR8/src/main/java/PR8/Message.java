package PR8;

public class Message {

    private String to;
    private String body;
  
    public Message() {
    }
  
    public Message(String to, String body) {
      this.to = to;
      this.body = body;
    }
  
    public static Message newMessage(String to, String body) {
        return new Message(to, body);
    }

    public String getTo() {
      return this.to;
    }
  
    public void setTo(String to) {
      this.to = to;
    }
  
    public String getBody() {
      return this.body;
    }
  
    public void setBody(String body) {
      this.body = body;
    }
  
    @Override
    public String toString() {
      return String.format("Message{to=%s, body=%s}", getTo(), getBody());
    }
  
  }