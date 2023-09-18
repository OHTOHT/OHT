package template.travel.data;

import android.content.Context;

import template.travel.model.Airport;
import template.travel.model.Flight;
import template.travel.model.FlightBooking;
import template.travel.model.Hotel;
import template.travel.model.HotelBooking;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {

    private static GlobalVariable mInstance;

    public static synchronized GlobalVariable getInstance(Context context) {
        if (mInstance == null) mInstance = new GlobalVariable(context);
        return mInstance;
    }

    private Flight flight;
    private Hotel hotel;
    private List<Airport> airports = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private List<FlightBooking> flightBookings= new ArrayList<>();
    private List<HotelBooking> hotelBookings = new ArrayList<>();

    public GlobalVariable(Context context) {
        mInstance = this;
        airports = Constant.getAirportData(context);
        cities = Constant.getCityData(context);
        flightBookings = Constant.getFlightBookingData(context);
        hotelBookings = Constant.getHotelBookingData(context);

        flight = new Flight();
        flight.origin = airports.get(10);
        flight.destination = airports.get(airports.size() - 1);

        hotel = new Hotel();
        hotel.city = cities.get(cities.size() - 1);

    }

    public List<Airport> getAirports() {
        return airports;
    }

    public Flight getFlight() {
        return flight;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<FlightBooking> getFlightBookings() {
        return flightBookings;
    }

    public List<HotelBooking> getHotelBookings() {
        return hotelBookings;
    }
}
