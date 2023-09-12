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

    /**
     * Use the MessageDAO to insert a new message into the database.
     * @param message an object representing a new message.
     * @return the newly added message if the insert operation was successful, including 
     *         its message_id. Return <code>null</code> if the message failed to meet the 
     *         message requirements or if the insert operation was unsuccessful.
     */
    public Message insertMessage(Message message) {
        if (message.getMessage_text().isBlank())
            return null;
        else if (message.getMessage_text().length() >= 255)
            return null;
        else if (messageDAO.getPostedBy(message.getPosted_by()) < 0)
            return null;
        
        return messageDAO.insertMessage(message);
    }

    /**
     * Use the MessageDAO to retrieve a List containing all messages from the database.
     * @return all messages in the database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Use the MessageDAO to retrieve a message from the database.
     * @param message_id the id of the message to be retrieved
     * @return the message if the get operation was successful. Return <code>null</code> 
     *         if the get operation was unsuccessful.
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Use the MessageDAO to delete a message from the database.
     * @param message_id the id of the message to be deleted
     * @return the now deleted message if the message was found in the database.
     *         Return <code>null</code> if a message was not found with the given 
     *         message id.
     */
    public Message deleteMessage(int message_id) {
        Message deletedMessage = messageDAO.getMessageById(message_id);
        messageDAO.deleteMessage(message_id);
        return deletedMessage;
    }

    /**
     * Use the MessageDAO to update a message from the database.
     * @param message_id the id of the message to be updated.
     * @param message_text the text string containing all the data that should replace
     *         the text string contained by the existing message.
     * @return the now updated message if the message was found in the database.
     *         Return <code>null</code> if the message text does not meet the requirements 
     *         for a message text or a message was not found with the given message id.
     */
    public Message updateMessage(int message_id, String message_text) {
        if (messageDAO.getMessageById(message_id) == null)
            return null;
        else if (message_text.isBlank())
            return null;
        else if (message_text.length() >= 255)
            return null;
        
        messageDAO.updateMessage(message_id, message_text);
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Use the MessageDAO to retrieve a List containing all messages posted by a 
     * given account from the database.
     * @param account_id the id of the account having its messages retrieved.
     * @return all messages posted by the account in the database.
     */
    public List<Message> getUserMessages(int account_id) {
        return messageDAO.getMessagesByAccountId(account_id);
    }
}
