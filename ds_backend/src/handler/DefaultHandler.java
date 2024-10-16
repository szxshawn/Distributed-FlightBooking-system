package handler;

import model.User;
import utils.Sender;

public class DefaultHandler {
    public static void handle(User user){
        Sender sender =  new Sender(user);
        sender.send("undefined service, please try again!");
        return;
    }
}
