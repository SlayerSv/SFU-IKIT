package PR8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.PostConstruct;

@Controller
@RequestMapping("/messages")
public class MessageController {
    

    @Autowired
    private JmsTemplate jms;

    @PostConstruct
    private void init() {
        this.jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
    }

    @GetMapping()
	public String getAll(Model model) {
        ArrayList<Message> messages = new ArrayList<Message>();
        Message message = (Message) jms.receiveAndConvert("products");
        while (message != null) {
            messages.add(message);
            message = (Message) jms.receiveAndConvert("products");
        }
        model.addAttribute("messages", messages);
		return "messages";
	}
}
