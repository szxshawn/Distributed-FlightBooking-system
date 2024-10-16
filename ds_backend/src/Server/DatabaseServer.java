package Server;

import model.BookInfo;
import model.Flight;
import model.User;
import utils.DuplicateFilterMap;
import utils.TimedHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DatabaseServer {
    public Integer bookIdCounter;
    public HashMap<Integer,Flight> flights;
    public TimedHashMap<Integer,User> callback;
    public HashSet<User> users;
    public DuplicateFilterMap responseFilter;
    public HashMap<String,List<BookInfo>> userBookingInfo;
    public DatabaseServer(){
        this.bookIdCounter = 0;
        this.flights = new HashMap<>();
        this.callback = new TimedHashMap<>();
        this.users = new HashSet<>();
        this.userBookingInfo = new HashMap<>();
        this.responseFilter = new DuplicateFilterMap();
        Flight flight1 = new Flight(101, "New York", "London", 1697025600L, 500.75f, 20);
        Flight flight2 = new Flight(102, "Beijing", "Tokyo", 1697112000L, 300.50f, 15);
        Flight flight3 = new Flight(103, "Los Angeles", "Paris", 1697198400L, 650.00f, 10);
        Flight flight4 = new Flight(104, "Sydney", "Singapore", 1697284800L, 400.25f, 25);
        Flight flight5 = new Flight(105, "Berlin", "Rome", 1697371200L, 150.00f, 5);
        Flight flight6 = new Flight(106, "New York", "Paris", 1697457600L, 520.00f, 18);
        Flight flight7 = new Flight(107, "New York", "London", 1697544000L, 480.75f, 12);
        Flight flight8 = new Flight(108, "Beijing", "Seoul", 1697630400L, 350.50f, 20);
        Flight flight9 = new Flight(109, "Beijing", "Shanghai", 1697716800L, 120.25f, 30);
        Flight flight10 = new Flight(110, "Los Angeles", "Tokyo", 1697803200L, 680.00f, 25);
        Flight flight11 = new Flight(111, "Los Angeles", "New York", 1697889600L, 300.00f, 25);
        Flight flight12 = new Flight(112, "Sydney", "Bangkok", 1697976000L, 450.00f, 22);
        Flight flight13 = new Flight(113, "Sydney", "Tokyo", 1698062400L, 520.25f, 15);
        Flight flight14 = new Flight(114, "Berlin", "Madrid", 1698148800L, 170.00f, 10);
        Flight flight15 = new Flight(115, "Berlin", "London", 1698235200L, 180.50f, 7);
        this.flights.put(flight1.identifier,flight1);
        this.flights.put(flight2.identifier,flight2);
        this.flights.put(flight3.identifier,flight3);
        this.flights.put(flight4.identifier, flight4);
        this.flights.put(flight5.identifier,flight5);
        this.flights.put(flight6.identifier, flight6);
        this.flights.put(flight7.identifier, flight7);
        this.flights.put(flight8.identifier, flight8);
        this.flights.put(flight9.identifier, flight9);
        this.flights.put(flight10.identifier, flight10);
        this.flights.put(flight11.identifier, flight11);
        this.flights.put(flight12.identifier, flight12);
        this.flights.put(flight13.identifier, flight13);
        this.flights.put(flight14.identifier, flight14);
        this.flights.put(flight15.identifier, flight15);
    }
}
