package it.zerocool.batmacaana.utilities;

import android.location.Location;

import com.google.gson.Gson;

import it.zerocool.batmacaana.model.Place;

/**
 * Created by Marco on 19/01/2015.
 */
public class LocationUtilities {

    public static void setPlaceDistance(Place place, Location current) {
        if (current != null) {
            Gson gson = new Gson();
//            Location location = gson.fromJson(jLocation, Location.class);
            float distance = place.getLocation().distanceTo(current);
            place.setDistanceFromCurrentPosition(distance);
        }
    }
}
