package handler;

import Server.DatabaseServer;
import model.Flight;
import model.User;
import utils.RequestSerializer;
import utils.Sender;

import java.util.HashMap;

public class QueryFlightByIdentifierHandler {
    public static void handle(User user, DatabaseServer databaseServer, String req) {
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
        sb.append("Query Flights by identifier:\n");
        int cnt = 0;
        for (int i : databaseServer.flights.keySet()) {
            if(Integer.parseInt(info.get("id")) == i){
                Flight f = databaseServer.flights.get(i);
                sb.append(f.toString() + "\n");
                cnt++;
            }
        }
        if (cnt == 0) {
            sb.append("no matches found");
            sender.send(sb.toString());
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
        } else {
            sender.send(sb.toString());
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
        }
    }
}
