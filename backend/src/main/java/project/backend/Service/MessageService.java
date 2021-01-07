package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Message;
import project.backend.Repo.MessageRepo;

import java.util.List;

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

    public List<Message> getAllMessages(String type, String id) {
        return messageRepo.findMessagesById(type, id);
    }

    public void setRead(String type, int messageId) {
        messageRepo.updateStatusToRead(type, messageId);
    }
}
