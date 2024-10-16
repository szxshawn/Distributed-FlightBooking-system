package test;
import model.Flight;

public class testSerialization {
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Flight flight1 = new Flight(101, "New York", "London", 1697025600L, 500.75f, 20);
        Flight flight2 = new Flight(102, "Beijing", "Tokyo", 1697112000L, 300.50f, 15);
        Flight flight3 = new Flight(103, "Los Angeles", "Paris", 1697198400L, 650.00f, 10);
        Flight flight4 = new Flight(104, "Sydney", "Singapore", 1697284800L, 400.25f, 25);
        Flight flight5 = new Flight(105, "Berlin", "Rome", 1697371200L, 150.00f, 5);
    }
}
