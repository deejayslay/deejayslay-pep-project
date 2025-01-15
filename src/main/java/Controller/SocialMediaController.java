package Controller;


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
        System.out.println("Line 74!");
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (this.accountService.getAccountById(message.getPosted_by()) != null) {
            System.out.println("Line 76!");
            Message newMessage = this.messageService.createMessage(message);
            System.out.println("Line 78!");
            if (newMessage != null) {
                ctx.json(mapper.writeValueAsString(newMessage));
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400);
        }
    }


}