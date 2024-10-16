package model;

import java.io.File;

public class Flight {
    public Integer identifier;
    public String sourcePlace;
    public String destinationPlace;
    public Long time;
    public Float airfare;
    public Integer seatAvailability;
    public Flight(Integer identifier,String sourcePlace,String destinationPlace,Long time,Float airfare,Integer seatAvailability){
        this.identifier = identifier;
        this.sourcePlace = sourcePlace;
        this.destinationPlace = destinationPlace;
        this.time = time;
        this.airfare = airfare;
        this.seatAvailability = seatAvailability;
    }
    public String toString() {
        return "[" +
                "identifier=" + identifier +
                ",sourcePlace=" + sourcePlace +
                ",destinationPlace=" + destinationPlace +
                ",time=" + time +
                ",airfare=" + airfare +
                ",seatAvailability=" + seatAvailability +
                ']';
    }
    public Flight(){}
}
