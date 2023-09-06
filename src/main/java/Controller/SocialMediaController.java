package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * Handler to post a new account.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If accountService returns a null account (meaning posting an account was unsuccessful), the API will return 
     * a 400 message (client error).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.insertAccount(account);
        if (newAccount != null)
            ctx.json(mapper.writeValueAsString(newAccount)); 
        else
            ctx.status(400);
    }

    /**
     * Handler to post an account login.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If accountService returns a null account (meaning an account with a matching username and password was 
     * not found), the API will return a 401 message (unauthorized).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if (loginAccount != null)
            ctx.json(mapper.writeValueAsString(loginAccount));
        else
            ctx.status(401);
    }

    /**
     * Handler to post a new message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If messageService returns a null message (meaning posting a message was unsuccessful), the API will return 
     * a 400 message (client error).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.insertMessage(message);
        if (newMessage != null)
            ctx.json(mapper.writeValueAsString(newMessage));
        else
            ctx.status(400);
    }

    /**
     * Handler to get all messages.
     * If messageService returns a list of message objects, The API will return a 200 message (OK), even if 
     * the returned list of messages is empty.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /**
     * Handler to get a message, identified by its message id
     * The message id is parsed from the PATH parameter of the context object. 
     * The API will return a 200 message (OK), even if messageService returns a null Message object (meaning 
     * a message with the given message id wasn't found).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message != null)
            ctx.json(message);
        else
            ctx.json("");
    }

    public void deleteMessageHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        if (deletedMessage != null)
            ctx.json(deletedMessage);
        else
            ctx.json("");
    }

    public void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message_text = mapper.readValue(ctx.body(), Message.class).getMessage_text();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message_text);
        if (updatedMessage != null)
            ctx.json(updatedMessage);
        else
            ctx.status(400);
    }

    public void getUserMessages(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getUserMessages(account_id);
        ctx.json(userMessages);
    }
}