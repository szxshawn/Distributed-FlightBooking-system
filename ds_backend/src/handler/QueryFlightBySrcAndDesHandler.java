package handler;

import Server.DatabaseServer;
import model.Flight;
import model.User;
import utils.RequestSerializer;
import utils.Sender;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class QueryFlightBySrcAndDesHandler {
    public static void handle(User user, DatabaseServer databaseServer,String req, DatagramSocket socket) throws IOException {
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
            sb.append("Query Flights by source and destination:\n");
            int cnt = 0;
            for (int i : databaseServer.flights.keySet()) {
                Flight f = databaseServer.flights.get(i);
                if (info.get("source").equals(f.sourcePlace) && info.get("destination").equals(f.destinationPlace)) {
                    sb.append(f.toString() + "\n");
                    cnt++;
                }
            }
            if (cnt == 0) {
                sb.append("no matches found");
                if(info.get("semantic").equals("at-most-once"))
                    databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
                sender.send(sb.toString());
            } else {
                if(info.get("semantic").equals("at-most-once"))
                    databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
                sender.send(sb.toString());
            }
    }
}
