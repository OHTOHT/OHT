package template.travel.model;

import java.io.Serializable;
import java.util.Date;

public class Flight implements Serializable {

    public Airport origin = new Airport();
    public Airport destination = new Airport();
    public Date date = new Date();
    public int adult = 1;
    public int child = 0;
    public int infant = 0;
    public SeatClass seatClass = SeatClass.BUSINESS;

}
