package template.travel.model;

import java.io.Serializable;

public class FlightResult implements Serializable {

    public long id;
    public int icon;
    public String airline;
    public String time_origin;
    public String time_destination;
    public String duration;
    public String price;
    public String flight_code;

}
