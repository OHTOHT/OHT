package template.travel.model;

import java.io.Serializable;
import java.util.Date;

public class Hotel implements Serializable {
    public String city;
    public Date check_in = new Date();
    public int night = 1;
    public int guest = 1;
    public int room = 1;
}
