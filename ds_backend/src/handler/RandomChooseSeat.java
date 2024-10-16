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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomChooseSeat {
    public static void handle(User user, DatabaseServer databaseServer, String req){
        // Create a Sender instance to send responses to the client
        Sender sender = new Sender(user);
        // Deserialize the request string into a HashMap for easy access to its fields
        HashMap<String, String> info = RequestSerializer.deserialize(req);
        if (!(info.get("error") == null)) {
            System.out.println(info.get("error"));
            sender.send(info.get("error"));
            return;
        }
        StringBuilder sb = new StringBuilder();// Initialize a StringBuilder to construct the response message
        Flight f = databaseServer.flights.get(Integer.parseInt(info.get("id")));
        int bookId = Integer.parseInt(info.get("bookid"));
        if(f != null){
            List<BookInfo> infos = databaseServer.userBookingInfo.getOrDefault(user.toString(),new ArrayList<>());
            boolean flag = true;
            for(BookInfo bookInfo : infos){
                if(bookInfo.bookId == bookId){
                    flag = false;
                    Set<Integer> uniqueSeats = new HashSet<>(); // Used to store unique seat numbers
                    List<Integer> l = new ArrayList<>();
                    Random random = new Random();
                    while(l.size() < bookInfo.bookedSeats) {
                        int randomSeat = random.nextInt(f.seatAvailability) + 1; // Generate a random number between 1 and seatAvailability
                        if(!uniqueSeats.contains(randomSeat)) { // Check if this seat number has already been generated
                            uniqueSeats.add(randomSeat);
                            l.add(randomSeat);
                        }
                    }
                    bookInfo.seatsId = l;
                    sb.append(bookInfo.toString());
                }
            }
            if(flag){
                sb.append("please book a flight first");
                sender.send(sb.toString());
                return;
            }
            databaseServer.userBookingInfo.put(user.toString(),infos);
            if(databaseServer.callback.get(f.identifier) != null){
                List<TimedHashMap.TimedValue<User>> users = databaseServer.callback.get(f.identifier);
                for(TimedHashMap.TimedValue<User> timedValue : users){
                    User user1 = timedValue.getValue();
                    if(user1.ip == user.ip && user.port == user1.port) {
                        continue;
                    }
                    Sender callbackSender = new Sender(user1);
                    callbackSender.send("some one has booked this flight:\n" + f.toString());
                }
            }
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
            sender.send(sb.toString());
        }
        else{
            sb.append("error: unable to find Flight, please try another Flight ID");
            if(info.get("semantic").equals("at-most-once"))
                databaseServer.responseFilter.put(user.toString() + req, sb.toString(), 15000);
            sender.send(sb.toString());
        }
    }

}
