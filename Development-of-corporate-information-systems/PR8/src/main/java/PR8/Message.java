package PR8;

public class Message {

    private String action;
    private String body;
  
    public Message() {
    }
  
    public Message(String action, String body) {
      this.action = action;
      this.body = body;
    }
  
    public static Message newMessage(String action, String body) {
        return new Message(action, body);
    }

    public String getAction() {
      return this.action;
    }
  
    public void setAction(String action) {
      this.action = action;
    }
  
    public String getBody() {
      return this.body;
    }
  
    public void setBody(String body) {
      this.body = body;
    }
  
    @Override
    public String toString() {
      return String.format("Message[Action=%s, body=%s]", getAction(), getBody());
    }
  }