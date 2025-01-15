package Controller;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    public SocialMediaController() {
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
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::processLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acct = mapper.readValue(ctx.body(), Account.class);
        Account newAcct = this.accountService.createAccount(acct);
        if (newAcct != null) {
            ctx.json(mapper.writeValueAsString(newAcct));
        } else {
            ctx.status(400);
        }
    }

    private void processLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acct = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAcct = this.accountService.loginAccount(acct.getUsername(), acct.getPassword());

        if (loggedInAcct != null) {
            ctx.json(mapper.writeValueAsString(loggedInAcct));
        } else {
            ctx.status(401);
        }
 
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (this.accountService.getAccountById(message.getPosted_by()) != null) {
            Message newMessage = this.messageService.createMessage(message);
            if (newMessage != null) {
                ctx.json(mapper.writeValueAsString(newMessage));
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = new ArrayList<>();

        messages = this.messageService.getAllMessages();

        ctx.json(mapper.writeValueAsString(messages));
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = mapper.readValue(ctx.pathParam("message_id"), Integer.class);
        Message message = this.messageService.getMessageById(message_id);

        if (message != null) {
            ctx.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = mapper.readValue(ctx.pathParam("message_id"), Integer.class);
        Message deletedMessage = this.messageService.deleteMessageById(message_id);

        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        }
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = mapper.readValue(ctx.pathParam("message_id"), Integer.class);

        Message newMessage = mapper.readValue(ctx.body(), Message.class);

        String newMessageText = newMessage.getMessage_text();

        Message updatedMessage = this.messageService.updateMessageById(message_id, newMessageText);

        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = mapper.readValue(ctx.pathParam("account_id"), Integer.class);

        List<Message> userMessages = new ArrayList<>();

        userMessages = this.messageService.getAllMessagesByUser(account_id);

        ctx.json(userMessages);
    }

}