package handler;

import Server.DatabaseServer;
import model.Flight;
import model.User;
import utils.RequestSerializer;
import utils.Sender;

import java.util.HashMap;

public class SubscribeByIdHandler {
    public static void handle(User user, DatabaseServer databaseServer, String req){
        Sender sender = new Sender(user);
        HashMap<String, String> info = RequestSerializer.deserialize(req);
        if (!(info.get("error") == null)) {
            System.out.println(info.get("error"));
            sender.send(info.get("error"));
            return;
        }
        if(info.get("semantic").equals("at-most-once")){
            if(databaseServer.responseFilter.get(user.toString() + req) != null){
                System.out.println("duplicated request!\n");
                sender.send(databaseServer.responseFilter.get(user.toString() + req));
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        Flight f = databaseServer.flights.get(Integer.parseInt(info.get("id")));
        if(!(f == null)){
            Integer timeout = Integer.parseInt(info.get("timeinterval"));
            databaseServer.callback.put(f.identifier,user,timeout);
            sender.send("user: " + user.ip + ":" + user.port + " has successfully subscribed to the flight:\n" + f.toString() + " \ntimeout: " + timeout + " milliseconds");
        }
        else{
            sb.append("error: unable to find Flight, please try another Flight ID");
            sender.send(sb.toString());
        }
    }

}
