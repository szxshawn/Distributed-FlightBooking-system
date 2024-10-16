package model;

import java.security.Timestamp;
import java.util.List;

public class BookInfo {
    public Integer bookId;
    public Integer identifier;
    public String sourcePlace;
    public String destinationPlace;
    public Long time;
    public Float airfare;
    public long bookedTime;
    public int bookedSeats;
    public List<Integer> seatsId;
    public Timestamp bookTime;
    public BookInfo(Flight flight, int bookedSeats, List<Integer> seatsId,int bookId){
        this.bookId = bookId;
        this.identifier = flight.identifier;
        this.airfare = flight.airfare;
        this.sourcePlace = flight.sourcePlace;
        this.destinationPlace = flight.destinationPlace;
        this.time = flight.time;
        this.bookedTime = System.currentTimeMillis();
        this.bookedSeats = bookedSeats;
        this.seatsId = seatsId;
    }
    @Override
    public String toString() {
        return "[" +
                "bookingId=" + bookId +
                ", identifier=" + identifier +
                ", sourcePlace='" + sourcePlace + '\'' +
                ", destinationPlace='" + destinationPlace + '\'' +
                ", time=" + time +
                ", airfare=" + airfare +
                ", bookedTime=" + bookedTime +
                ", bookedSeats=" + bookedSeats +
                ", bookedSeatsId=" + this.seatsId.toString() +
                ']';
    }
}
