package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Message;
import project.backend.Repo.MessageRepo;

@Service
public class MessageService {
    private MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void addNewMessage(String type, Message message) {
        messageRepo.insertMessage(type, message);
    }
}
