package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {

    private MessageDao messageDao;

    public MessageService() {
        this.messageDao = new MessageDao();
    }

    // for testing with mock objects
    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message createMessage(Message message) {
        return this.messageDao.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return this.messageDao.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return this.messageDao.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        return this.messageDao.deleteMessageById(message_id);
    }

    public Message updateMessageById(int message_id, String newMessageText) {
        return this.messageDao.updateMessageById(message_id, newMessageText);
    }

    public List<Message> getAllMessagesByUser(int account_id) {
        return this.messageDao.getAllMessagesByUser(account_id);
    }
    
}
