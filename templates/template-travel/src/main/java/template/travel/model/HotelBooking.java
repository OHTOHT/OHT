package template.travel.model;

import java.io.Serializable;

public class HotelBooking implements Serializable {
    public long id;
    public String order_id;
    public String booking_code;
    public Hotel hotel = new Hotel();
    public HotelResult hotelResult = new HotelResult();
    public String status = "COMPLETED";
}
