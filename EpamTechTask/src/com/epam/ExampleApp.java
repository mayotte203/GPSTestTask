package com.epam;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;
import com.epam.impl.MyGPS;

public class ExampleApp {

    public static void main(String[] args) {
        final GpsNavigator navigator = new MyGPS();
        try {
            navigator.readData("D:\\Gps\\road_map.ext");
        } catch(Exception exc) {
            System.out.println(exc.getMessage());
        }
        try {
            final Path path = navigator.findPath("C", "F");
            System.out.println(path);
        } catch(Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
}
