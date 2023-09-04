package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    public MessageDAO messageDAO;

    // No-args constructor for messageService which creates a MessageDAO.
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO.
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Use MessageDAO to insert a new message into the message table
    public Message insertMessage(Message message) {
        if (message.getMessage_text().isEmpty()) {
            return null;
        } else if (message.getMessage_text().length() >= 255) {
            return null;
        } else if (messageDAO.getPostedBy(message.getPosted_by()) == null) {
            return null;
        }
        return insertMessage(message);
    }

    // Use MessageDAO to retrieve all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Use MessageDAO to retrieve a message by its message id
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }
}
