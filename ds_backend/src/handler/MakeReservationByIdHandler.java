package handler;

import Server.DatabaseServer;
import model.BookInfo;
import model.Flight;
import model.User;
import utils.RequestSerializer;
import utils.Sender;
import utils.TimedHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeReservationByIdHandler {
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
        Flight f = databaseServer.flights.get(Integer.parseInt(info.get("id")));
        if(!(f == null)){
            int seats = Integer.parseInt(info.get("seats"));
            if(seats > f.seatAvailability){
                sb.append("error: not enough seats");
                sender.send((sb.toString()));
                return;
            }

            List<BookInfo> bookInfos = databaseServer.userBookingInfo.getOrDefault(user.toString(),new ArrayList<>());
            bookInfos.add(new BookInfo(f,seats,new ArrayList<>(),databaseServer.bookIdCounter++));
            databaseServer.userBookingInfo.put(user.toString(),bookInfos);
            List<BookInfo> ttt = databaseServer.userBookingInfo.get(user.toString());
            for(BookInfo bif : ttt){
                System.out.println(bif.toString()+"\n");
            }

            sb.append("Booking Flight Info:\n");
            f.seatAvailability -= seats;
            sb.append(f.toString() + "\n");
            sb.append("successfully booked " + seats + " seats");
            sender.send(sb.toString());
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
            if(databaseServer.callback.get(f.identifier) != null){
                List<TimedHashMap.TimedValue<User>> users = databaseServer.callback.get(f.identifier);
                for(TimedHashMap.TimedValue<User> timedValue : users){
                    User user1 = timedValue.getValue();
                    if(user1.ip == user.ip && user.port == user1.port) {
                        continue;
                    }
                    Sender callbackSender = new Sender(user1);
                    callbackSender.send("some one has book this flight:\n" + f.toString());
                }
            }
        }
        else{
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
            sb.append("error: unable to find Flight, please try another Flight ID");
            sender.send(sb.toString());
        }
    }
}
