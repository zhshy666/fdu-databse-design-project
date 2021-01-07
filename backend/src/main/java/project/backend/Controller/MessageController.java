package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.GetMessageRequest;
import project.backend.Controller.Request.SetReadRequest;
import project.backend.Entity.Message;
import project.backend.Service.MessageService;
import project.backend.Utils.Config;

import java.util.LinkedList;
import java.util.List;

@Controller
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/getMessages")
    public ResponseEntity<?> getMessages(@RequestBody GetMessageRequest request){
        String id = request.getId();
        List<Message> messages = messageService.getAllMessages(Config.ROOT, id);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/setRead")
    public ResponseEntity<?> setRead(@RequestBody SetReadRequest request){
        int messageId = request.getId();
        messageService.setRead(Config.ROOT, messageId);
        return ResponseEntity.ok("success");
    }
}
