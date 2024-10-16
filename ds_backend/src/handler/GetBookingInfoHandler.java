package handler;

import Server.DatabaseServer;
import model.BookInfo;
import model.User;
import utils.RequestSerializer;
import utils.Sender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBookingInfoHandler {
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

        List<BookInfo> bookInfos = databaseServer.userBookingInfo.getOrDefault(user.toString(),new ArrayList<>());
        if(bookInfos.isEmpty()){
            sender.send("no booking information found!");
        }
        else{
            for(BookInfo bi : bookInfos){
                sb.append(bi.toString() + "\n");
            }
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
            sender.send(sb.toString());
        }
    }
}
