package com.scooter.scooter_nav;

public class Utils {
    final static String TAG = "scooter_app";
    final static int CAMERA_ZOOM = 15;
    final static String ROUTE_ARGUMENT = "com.scooter.scooter_nav_route";
    // navigation triggers
    final static int TURN_APPROACHING_MILESTONE_ID = 5;
    final static int TURN_NOW_MILESTONE_ID = 6;
    final static float TURN_APPROACHING_DISTANCE = 150; // in meters
    final static float TURN_NOW_DISTANCE = 10;          // in meters
    final static float TURN_THRESHOLD = 30;             // determines how sharp a turn must be
    //  to trigger feedback
}
