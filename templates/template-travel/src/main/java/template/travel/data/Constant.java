package template.travel.data;

import android.content.Context;
import android.content.res.TypedArray;

import template.travel.R;
import template.travel.model.Airport;
import template.travel.model.FlightBooking;
import template.travel.model.FlightResult;
import template.travel.model.HotelBooking;
import template.travel.model.HotelResult;
import template.travel.model.SeatClass;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Constant {

    private static Random rnd = new Random();

    private static int getRandomIndex(Random r, int min, int max) {
        return r.nextInt(max - min) + min;
    }

    private static int getRandomIndexUnique(Random r, int min, int max, int reserved) {
        int value = r.nextInt(max - min) + min;
        if (reserved == value) getRandomIndexUnique(r, min, max, reserved);
        return value;
    }

    public static List<Airport> getAirportData(Context ctx) {
        List<Airport> items = new ArrayList<>();
        String[] code = ctx.getResources().getStringArray(R.array.app_travel_airport_code);
        String[] name = ctx.getResources().getStringArray(R.array.app_travel_airport_name);
        String[] city = ctx.getResources().getStringArray(R.array.app_travel_airport_city);
        for (int i = 0; i < code.length; i++) {
            Airport item = new Airport();
            item.id = i;
            item.code = code[i];
            item.name = name[i];
            item.city = city[i];
            items.add(item);
        }
        return items;
    }

    public static List<FlightResult> getFlightResultData(Context ctx) {
        List<FlightResult> items = new ArrayList<>();
        TypedArray icons = ctx.getResources().obtainTypedArray(R.array.app_travel_airline_logo);
        String[] names = ctx.getResources().getStringArray(R.array.app_travel_airline_name);

        String[] time_origin = ctx.getResources().getStringArray(R.array.app_travel_time_origin);
        String[] time_destination = ctx.getResources().getStringArray(R.array.app_travel_time_destination);
        String[] duration = ctx.getResources().getStringArray(R.array.app_travel_duration);
        String[] price = ctx.getResources().getStringArray(R.array.app_travel_price);
        String[] flight_code = ctx.getResources().getStringArray(R.array.app_travel_airline_flight_code);

        for (int i = 0; i < time_origin.length; i++) {
            int index = getRandomIndex(rnd, 0, names.length - 1);
            FlightResult item = new FlightResult();
            item.id = i;
            item.airline = names[index];
            item.icon = icons.getResourceId(index, -1);
            item.time_origin = time_origin[i];
            item.time_destination = time_destination[i];
            item.duration = duration[i];
            item.price = price[i];
            item.flight_code = flight_code[index];
            items.add(item);
        }
        return items;
    }

    public static List<HotelResult> getHotelResultData(Context ctx) {
        List<HotelResult> items = new ArrayList<>();
        TypedArray image = ctx.getResources().obtainTypedArray(R.array.app_travel_hotel_image);
        String[] name = ctx.getResources().getStringArray(R.array.app_travel_hotel_name);
        int[] rating = ctx.getResources().getIntArray(R.array.app_travel_hotel_rating);
        String[] location = ctx.getResources().getStringArray(R.array.app_travel_hotel_location);
        String[] price = ctx.getResources().getStringArray(R.array.app_travel_hotel_price);

        for (int i = 0; i < name.length; i++) {
            HotelResult item = new HotelResult();
            item.id = i;
            item.name = name[i];
            item.image = image.getResourceId(i, -1);
            item.rating = rating[i];
            item.location = location[i];
            item.price = price[i];
            items.add(item);
        }
        //Collections.shuffle(items);
        return items;
    }

    public static List<String> getCityData(Context ctx) {
        String[] hotel_city = ctx.getResources().getStringArray(R.array.app_travel_hotel_city);
        List<String> items = Arrays.asList(hotel_city);
        return items;
    }

    public static List<FlightBooking> getFlightBookingData(Context ctx) {
        List<FlightBooking> items = new ArrayList<>();
        List<Airport> airportList = getAirportData(ctx);
        List<SeatClass> seatClassList = Arrays.asList(SeatClass.values());
        List<FlightResult> flightResultList = getFlightResultData(ctx);
        for (int i = 0; i < 4; i++) {
            int idx_origin = getRandomIndex(rnd, 0, airportList.size() - 1);
            int idx_destination = getRandomIndexUnique(rnd, 0, airportList.size() - 1, idx_origin);
            int idx_seat_class = getRandomIndex(rnd, 0, seatClassList.size() - 1);
            int idx_flight_result = getRandomIndex(rnd, 0, flightResultList.size() - 1);

            FlightBooking item = new FlightBooking();
            item.id = i;
            item.order_id = getOrderID();
            item.booking_code = getBookingCode();
            item.flight.origin = airportList.get(idx_origin);
            item.flight.destination = airportList.get(idx_destination);
            item.flight.seatClass = seatClassList.get(idx_seat_class);
            item.flight.date = Tools.getAddedDate(i * 20);
            item.flightResult = flightResultList.get(idx_flight_result);
            items.add(item);
        }
        return items;
    }

    public static List<HotelBooking> getHotelBookingData(Context ctx) {
        List<HotelBooking> items = new ArrayList<>();
        List<HotelResult> hotelResultList = getHotelResultData(ctx);
        List<String> cities = Constant.getCityData(ctx);
        for (int i = 0; i < 3; i++) {
            int idx_hotel_result = getRandomIndex(rnd, 0, hotelResultList.size() - 1);
            HotelBooking item = new HotelBooking();
            item.id = i;
            item.order_id = getOrderID();
            item.booking_code = getBookingCode();
            item.hotel.city = cities.get(cities.size() - 1);
            item.hotel.night = 2;
            item.hotel.guest = 2;
            item.hotel.check_in = Tools.getAddedDate(i + 20);
            item.hotelResult = hotelResultList.get(idx_hotel_result);
            items.add(item);
        }
        return items;
    }


    public static String getBookingCode() {
        char[] numbs = "1234567890".toCharArray();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 2; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        for (int i = 0; i < 4; i++) {
            char c = numbs[random.nextInt(numbs.length)];
            sb.append(c);
        }
        for (int i = 0; i < 2; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String getOrderID() {
        char[] numbs = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 7; i++) {
            char c = numbs[random.nextInt(numbs.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
