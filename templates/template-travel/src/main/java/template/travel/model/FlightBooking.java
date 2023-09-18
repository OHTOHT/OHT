package template.travel.model;

import java.io.Serializable;

public class FlightBooking implements Serializable {

    public long id;
    public String order_id;
    public String booking_code;
    public Flight flight = new Flight();
    public FlightResult flightResult = new FlightResult();
    public String status = "COMPLETED";

}
