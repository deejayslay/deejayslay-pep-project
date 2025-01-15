package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {

    private MessageDao messageDao;

    public MessageService() {
        this.messageDao = new MessageDao();
    }

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message createMessage(Message message) {
        return this.messageDao.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return this.messageDao.getAllMessages();
    }
    
}
